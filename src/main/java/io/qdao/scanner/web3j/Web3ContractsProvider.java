package io.qdao.scanner.web3j;


import io.qdao.scanner.components.QueueTasksExecutor;
import io.qdao.scanner.models.Contract;
import io.qdao.scanner.models.TransfersLog;
import io.qdao.scanner.repositories.ContractRepository;
import io.qdao.scanner.repositories.TransfersLogRepository;
import io.qdao.scanner.types.FiatCurrency;
import io.reactivex.disposables.Disposable;
import io.reactivex.plugins.RxJavaPlugins;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
public class Web3ContractsProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Value("${ethereum.contracts.usdq.address}")
    private String usdqContractAddress;

    @Value("${ethereum.contracts.usdq.network}")
    private NetworkType usdqNetworkType;

    @Value("${ethereum.contracts.krwq.address}")
    private String krwqContractAddress;

    @Value("${ethereum.contracts.krwq.network}")
    private NetworkType krwqNetworkType;

    @Value("${ethereum.infura.project-id}")
    private String infuraProjectId;

    @Value("${ethereum.infura.project-secret}")
    private String infuraProjectSecret;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private TransfersLogRepository transfersLogRepository;

    @Autowired
    private Web3jDisposableManager web3jDisposableManager;

    @Autowired
    private TaskExecutor taskExecutor;

    @Autowired
    private QueueTasksExecutor queueTasksExecutor;

    private final Map<FiatCurrency, Web3JClient> contractCache = Collections.synchronizedMap(new HashMap<>());

    @PostConstruct
    void construct() {
        RxJavaPlugins.setErrorHandler(e -> {
            logger.warn(String.format("RxJavaPlugins error: %s", e.getMessage()));
        });

        final Map<FiatCurrency, Contract> caches = contractsFactory();

        logger.info(String.format("Contract connection configuration success. Found %d contracts. Start connection.", caches.size()));

        for (Map.Entry<FiatCurrency, Contract> entry : caches.entrySet()) {
            final Contract contract = entry.getValue();

            if (contract == null) {
                throw new NullPointerException("Fatal error contract information factory.");
            }

            try {
                final Web3JClient client = new Web3JClient(contract, infuraProjectId);
                contractCache.put(entry.getKey(), client);
                queueTasksExecutor.execute(() -> transferExecute(client));
            } catch (Exception e) {
                logger.error(String.format("Error create web3 client for %s. Error message: %s", contract.getAddress(), e.getLocalizedMessage()));
            }
        }
        contractRepository.saveAll(caches.values());
        logger.info(String.format("Success connection to %d smart-contracts", contractCache.size()));
    }

    public Web3JClient getClient(FiatCurrency fiatCurrency) {
        return contractCache.get(fiatCurrency);
    }

    private Map<FiatCurrency, Contract> contractsFactory() {
        final Contract usdqContract = contractRepository.getContract(usdqContractAddress, FiatCurrency.USD, usdqNetworkType);
        final Contract krwqContract = contractRepository.getContract(krwqContractAddress, FiatCurrency.KRW, krwqNetworkType);
        final Map<FiatCurrency, Contract> caches = new HashMap<>();
        caches.put(FiatCurrency.USD, usdqContract);
        caches.put(FiatCurrency.KRW, krwqContract);
        return caches;
    }

    private void transferExecute(Web3JClient client) {
        final Disposable disposable = client.webSocketEthTransferContract(tx -> queueTasksExecutor.execute(() -> {
            final TransfersLog transfersLog = transfersLogRepository.getTransferByHash(tx.getHash());
            tx.setUid(transfersLog.getUid());
            transfersLogRepository.save(tx);
            updateContractTotalSupply(client);
        }));

        web3jDisposableManager.add(disposable);

        final Set<TransfersLog> mintingTransfers = transfersLogRepository.getMintingTransfers(client.getContract());

        logger.info(String.format("Empty minting transfers: %d", mintingTransfers.size()));
    }

    private void updateContractTotalSupply(Web3JClient client) {
        final Contract current = client.getContract();
        final BigDecimal currentTotalSupply = current.getTotalSupply();
        try {
            final Contract contract = client.updateContractTotalSupply(current);

            if (!contract.getTotalSupply().equals(currentTotalSupply)) {
                contractRepository.save(contract);
            }
        } catch (Exception e) {
            logger.error(String.format("Error update contract information %s. Error message: %s", current.getAddress(), e.getMessage()));
        }
    }

}
