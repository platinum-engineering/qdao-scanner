package io.qdao.scanner.web3j;

import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.models.Contract;
import io.qdao.scanner.models.TransfersLog;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.web3j.contracts.eip20.generated.ERC20;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.parity.Parity;
import org.web3j.protocol.websocket.WebSocketService;
import org.web3j.tx.ClientTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Date;

public class Web3JClient implements Disposable {

    private static final DefaultGasProvider DEFAULT_GAS_PROVIDER = new DefaultGasProvider();

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final Web3j web3j;
    private final Parity parity;
    private final ERC20 erc20;

    private Contract contractCache;

    private BigDecimal tokenDecimalsScalar = BigDecimal.ONE;

    Web3JClient(Contract contract, String infuraProjectId) throws Exception {
        if (contract == null || contract.getAddress() == null || contract.getAddress().isEmpty()) {
            throw new NullPointerException("Smart-contract address is empty. Please check application configuration.");
        }

        logger.info(String.format("Smart-contact address: %s", contract.getAddress()));

        final String wssInfuraUrl = contract.getNetworkType().getWssUrl(infuraProjectId);
        final WebSocketService web3jService = new WebSocketService(wssInfuraUrl, true);
        web3jService.connect();

        this.web3j = Web3j.build(web3jService);

        final HttpService httpService = new HttpService(contract.getNetworkType().getHttpsUrl(infuraProjectId));
        this.parity = Parity.build(httpService);
        asyncLoggingClientVersion(this.parity);

        final String smartContractAddress = contract.getAddress();

        final TransactionManager tm = new ClientTransactionManager(web3j, smartContractAddress);
        this.erc20 = ERC20.load(smartContractAddress, web3j, tm, DEFAULT_GAS_PROVIDER);

        updateContractInformation(contract);

        logger.info(String.format("Success update information about %s smart-contract", smartContractAddress));
    }

    public Web3j getWeb3j() {
        return web3j;
    }

    public Parity getParity() {
        return parity;
    }

    public ERC20 getErc20() {
        return erc20;
    }

    public Contract getContract() {
        return contractCache;
    }

    public Disposable webSocketEthTransferContract(Consumer<? super TransfersLog> onTransferEvent) {
        return erc20.transferEventFlowable(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST).subscribe(
                e -> makeEtaTransferContractResponse(e, onTransferEvent),
                e -> logger.error(String.format("Contract transfer web socket error: %s", e.getMessage()))
        );
    }

    public BigDecimal value2token(BigDecimal value) {
        return value.divide(tokenDecimalsScalar, Utils.DEFAULT_DECIMALS, RoundingMode.HALF_UP);
    }

    private void updateContractInformation(Contract contract) throws Exception {
        this.contractCache = contract;

        final BigInteger decimals = erc20.decimals().send();
        final String symbol = erc20.symbol().send();
        final String name = erc20.name().send();
        contractCache.setDecimals(decimals.intValue());
        contractCache.setName(name);
        contractCache.setSymbol(symbol);

        updateContractTotalSupply();

        tokenDecimalsScalar = new BigDecimal(Math.pow(10, decimals.intValue()));
    }

    public Contract updateContractTotalSupply(Contract contract) throws Exception {
        this.contractCache = contract;
        return updateContractTotalSupply();
    }

    public Contract updateContractTotalSupply() throws Exception {
        final BigInteger totalSupply = erc20.totalSupply().send();
        contractCache.setTotalSupply(new BigDecimal(totalSupply));
        contractCache.setUpdatedAt(new Date());

        return contractCache;
    }

    public boolean isMintingTransfer(TransfersLog tx) {
        return tx.getAddressFrom().equals(Utils.ZERO_ADDRESS);
    }

    public boolean isBurningTransfer(TransfersLog tx) {
        return tx.getAddressTo().equals(Utils.ZERO_ADDRESS);
    }

    @Override
    public void dispose() {
        web3j.shutdown();
        parity.shutdown();
    }

    @Override
    public boolean isDisposed() {
        return false;
    }

    private void makeEtaTransferContractResponse(ERC20.TransferEventResponse response, Consumer<? super TransfersLog> onTransferEvent) {

        final String from = response._from;
        final String to = response._to;
        final BigDecimal value = new BigDecimal(response._value);

        final String hash;

        if (response.log == null) {
            hash = null;
        } else {
            hash = response.log.getTransactionHash();
        }

        final TransfersLog transfersLog = makeTransferLog(contractCache, hash, from, to, value);
        try {
            onTransferEvent.accept(transfersLog);
        } catch (Exception e) {
            logger.warn(String.format("Error transfer event callback: %s", e.getMessage()));
        }

        logger.info(String.format("{tx: %s} %s -> %s [%.08f]", hash, from, to, value2token(value)));
    }

    private TransfersLog makeTransferLog(Contract contract, String hash, String from, String to, BigDecimal value) {
        final TransfersLog transfersLog = new TransfersLog();
        transfersLog.setAddressFrom(from);
        transfersLog.setAddressTo(to);
        transfersLog.setContract(contract);
        transfersLog.setValue(value);
        transfersLog.setHash(hash);
        return transfersLog;
    }

    private void asyncLoggingClientVersion(Parity parity) {
        parity.netVersion().sendAsync().thenApplyAsync(netVersion -> {
            logger.info(String.format("Connect to ethereum node. Net version is %s", netVersion ));
            return netVersion;
        });

        parity.web3ClientVersion().sendAsync().thenApplyAsync(web3ClientVersion -> {
            final String web3Version = web3ClientVersion.getWeb3ClientVersion();
            logger.info(String.format("Connect to ethereum node. Web3 client version is %s", web3Version));
            return web3ClientVersion;
        });
    }
}
