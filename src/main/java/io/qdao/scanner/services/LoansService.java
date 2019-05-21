package io.qdao.scanner.services;

import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.components.SocketConnector;
import io.qdao.scanner.dto.ContractResponseDto;
import io.qdao.scanner.dto.LoanRequestDto;
import io.qdao.scanner.dto.LoanResponseDto;
import io.qdao.scanner.exceptions.InvalidLoanModel;
import io.qdao.scanner.exchanges.CryptoCurrency;
import io.qdao.scanner.exchanges.RatesProvider;
import io.qdao.scanner.models.Contract;
import io.qdao.scanner.models.Loan;
import io.qdao.scanner.models.TransfersLog;
import io.qdao.scanner.repositories.LoansRepository;
import io.qdao.scanner.types.FiatCurrency;
import io.qdao.scanner.types.LoanStatus;
import io.qdao.scanner.utils.Topics;
import io.qdao.scanner.web3j.Web3ContractsProvider;
import io.qdao.scanner.web3j.Web3JClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class LoansService {
    private static final BigDecimal LIQ_RATE_PERCENT = new BigDecimal(30);
    private static final BigDecimal LIQ_FEE_PERCENT = new BigDecimal(5);
    private static final BigDecimal LIQ_FEE_K = LIQ_FEE_PERCENT.divide(Utils.HUNDRED, Utils.DEFAULT_DECIMALS, RoundingMode.HALF_UP);
    private static final BigDecimal LIQ_RATE_K = Utils.HUNDRED.subtract(LIQ_RATE_PERCENT).divide(Utils.HUNDRED, Utils.DEFAULT_DECIMALS, RoundingMode.HALF_UP);

    private static final Sort DEFAULT_SORT = new Sort(Sort.Direction.ASC, "uid");

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private RatesProvider ratesProvider;

    @Autowired
    private ContractService contractService;

    @Autowired
    private SocketConnector connector;

    @Autowired
    private Web3ContractsProvider web3ContractsProvider;

    @Transactional
    public List<LoanResponseDto> getActualLoansByCurrency(FiatCurrency fiatCurrency) {
        final Contract contract = web3ContractsProvider.getClient(fiatCurrency).getContract();
        final List<Loan> loans = loansRepository.findByStatusAndContract(LoanStatus.STABLE, contract, DEFAULT_SORT);
        final List<LoanResponseDto> res = new ArrayList<>();
        final BigDecimal currentRate = ratesProvider.get(fiatCurrency);
        loans.forEach(l -> {
            final LoanResponseDto dto = model2response(l, currentRate);
            if (dto != null) {
                res.add(dto);
            }
        });
        return res;
    }

    @Transactional
    public List<LoanResponseDto> getLoansByCurrency(FiatCurrency fiatCurrency) {
        final Contract contract = web3ContractsProvider.getClient(fiatCurrency).getContract();
        final List<Loan> loans = loansRepository.findByContract(contract, DEFAULT_SORT);
        final List<LoanResponseDto> res = new ArrayList<>();
        final BigDecimal currentRate = ratesProvider.get(fiatCurrency);
        loans.forEach(l -> {
            final LoanResponseDto dto = model2response(l, currentRate);
            if (dto != null) {
                res.add(dto);
            }
        });
        return res;
    }

    public Loan createNewLoan(LoanRequestDto dto, TransfersLog transfersLog) {
        final FiatCurrency fiatCurrency = transfersLog.getContract().getFiatCurrency();
        final Web3JClient client = web3ContractsProvider.getClient(fiatCurrency);
        final BigDecimal tokens = client.value2token(transfersLog.getValue());
        final BigDecimal percent = dto.getRate().multiply(dto.getValue()).multiply(Utils.HUNDRED).divide(tokens, Utils.DEFAULT_DECIMALS, RoundingMode.HALF_UP);
        final BigDecimal liq = dto.getRate().multiply(LIQ_RATE_K);
        final BigDecimal fee = dto.getValue().multiply(LIQ_FEE_K);
        final BigDecimal diff = dto.getValue().subtract(fee).subtract(tokens.divide(liq, Utils.DEFAULT_DECIMALS, RoundingMode.HALF_UP));

        final Loan loan = new Loan();
        loan.setCollateralizedAddress(dto.getAddress()); // todo: Add check transaction processing
        loan.setCollateralizedCurrency(CryptoCurrency.BTC);
        loan.setCollateralizedValue(dto.getValue());
        loan.setCollateralizedRate(dto.getRate());
        loan.setCollateralizedStartIndex(percent.doubleValue());

        loan.setEthAddress(transfersLog.getAddressTo());
        loan.setTokenValue(tokens);

        loan.setDifferenceToCustomer(diff);
        loan.setLiquidationFee(fee);
        loan.setLiquidationIndex(LIQ_RATE_PERCENT.doubleValue());
        loan.setLiquidationRate(liq);

        loan.setStatus(LoanStatus.STABLE);
        loan.setTransfersLog(transfersLog);
        loan.setUpdatedAt(new Date());

        if (validateLoan(loan)) {
            final Loan r = loansRepository.save(loan);
            try {
                final BigDecimal currentRate = ratesProvider.get(fiatCurrency);
                final LoanResponseDto d = model2response(r, currentRate);
                connector.notifyAll(Topics.loansTopic(fiatCurrency), d);
            } catch (Throwable e) {
                logger.warn(e.getMessage());
            }

            try {
                final ContractResponseDto response = contractService.getActualContract(fiatCurrency);
                connector.notifyAll(Topics.contractTopic(fiatCurrency), response);
            } catch (Throwable e) {
                logger.warn(e.getMessage());
            }
            return r;
        }

        return null;
    }

    private LoanResponseDto model2response(Loan dto, BigDecimal currentRate) {
        final Double currentIndex = makeCurrentIndex(currentRate, dto);
        if (currentIndex == null) {
            return null;
        }
        final LoanResponseDto res = new LoanResponseDto();
        res.setUid(dto.getUid());
        res.setLoanStatus(dto.getStatus());
        res.setCurrency(dto.getCollateralizedCurrency());
        res.setDifferentBeforeLiquidation(dto.getDifferenceToCustomer());
        res.setEnsureAddress(dto.getCollateralizedAddress());
        res.setEnsureAmount(dto.getCollateralizedValue());
        res.setEnsureRate(dto.getCollateralizedRate());
        res.setStartIndex(dto.getCollateralizedStartIndex());
        res.setEthAddress(dto.getEthAddress());
        res.setTokensIssued(dto.getTokenValue());
        res.setLiquidationFee(dto.getLiquidationFee());
        res.setLiquidationIndex(dto.getLiquidationIndex());
        res.setLiquidationRate(dto.getLiquidationRate());
        res.setCreatedAt(dto.getCreatedAt());
        res.setUpdatedAt(dto.getUpdatedAt());

        if (dto.getTransfersLog() == null || dto.getTransfersLog().getContract() == null) {
            throw new InvalidLoanModel();
        } else {
            res.setFiatCurrency(dto.getTransfersLog().getContract().getFiatCurrency());
        }

        res.setCurrentIndex(currentIndex);
        return res;
    }

    private Double makeCurrentIndex(BigDecimal currentRate, Loan dto) {
        if (dto.getCollateralizedValue() == null || dto.getTokenValue() == null) {
            return null;
        }

        final BigDecimal usd = dto.getCollateralizedValue().multiply(currentRate);
        final BigDecimal cr = usd.multiply(Utils.HUNDRED).divide(dto.getTokenValue(), Utils.DEFAULT_DECIMALS, RoundingMode.HALF_UP);
        return cr.doubleValue();
    }

    private boolean validateLoan(Loan loan) {
        return loan.getCollateralizedStartIndex() > 0
                && loan.getLiquidationFee().doubleValue() > 0
                && loan.getLiquidationIndex() > 0
                && loan.getLiquidationRate().doubleValue() > 0
                && loan.getDifferenceToCustomer().doubleValue() > 0;
    }

}
