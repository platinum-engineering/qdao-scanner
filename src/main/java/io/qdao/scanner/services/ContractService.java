package io.qdao.scanner.services;

import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.dto.ContractResponseDto;
import io.qdao.scanner.exchanges.RatesProvider;
import io.qdao.scanner.models.Contract;
import io.qdao.scanner.repositories.ContractRepository;
import io.qdao.scanner.repositories.LoansRepository;
import io.qdao.scanner.types.FiatCurrency;
import io.qdao.scanner.web3j.Web3ContractsProvider;
import io.qdao.scanner.web3j.Web3JClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ContractService {

    @Autowired
    private Web3ContractsProvider web3ContractsProvider;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private LoansRepository loansRepository;

    @Autowired
    private RatesProvider ratesProvider;

    public ContractResponseDto getActualContract(FiatCurrency fiatCurrency) {
        final BigDecimal currentRate = ratesProvider.get(fiatCurrency);
        return getActualContract(currentRate, fiatCurrency);
    }

    private ContractResponseDto getActualContract(BigDecimal currentRate, FiatCurrency fiatCurrency) {
        final Web3JClient client = web3ContractsProvider.getClient(fiatCurrency);
        return model2dto(client, currentRate);
    }

    private ContractResponseDto model2dto(Web3JClient client, BigDecimal currentRate) {
        final Contract contract = client.getContract();
        if (contract == null) {
            return null;
        }
        final BigDecimal totalSupply = contract.getTotalSupply();
        if (totalSupply == null) {
            return null;
        }
        final BigDecimal tokens = client.value2token(totalSupply);
        final ContractResponseDto dto = new ContractResponseDto();
        dto.setUid(contract.getUid());
        dto.setAddress(contract.getAddress());
        dto.setName(contract.getName());
        dto.setTotalSupply(totalSupply);
        dto.setTotalIssuedTokens(tokens);
        dto.setDecimals(contract.getDecimals());
        dto.setSymbol(contract.getSymbol());

        final BigDecimal currentTC = loansRepository.actualTotalCollateralized();
        final BigDecimal tc = currentTC == null ? BigDecimal.ZERO : currentTC;
        final BigDecimal usd = tc.multiply(currentRate);
        final BigDecimal cr = usd.multiply(Utils.HUNDRED).divide(tokens, Utils.DEFAULT_DECIMALS, RoundingMode.HALF_UP);
        dto.setCollateralIndex(cr.doubleValue());
        dto.setTotalCollateralized(tc);
        dto.setTotalCollateralizedUsd(usd);
        return dto;
    }
}
