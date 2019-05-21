package io.qdao.scanner.services;

import io.qdao.scanner.dto.TransferResponseDto;
import io.qdao.scanner.models.Contract;
import io.qdao.scanner.models.TransfersLog;
import io.qdao.scanner.repositories.TransfersLogRepository;
import io.qdao.scanner.types.FiatCurrency;
import io.qdao.scanner.web3j.Web3ContractsProvider;
import io.qdao.scanner.web3j.Web3JClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Service
public class TransfersService {

    @Autowired
    private TransfersLogRepository transfersLogRepository;

    @Autowired
    private Web3ContractsProvider web3ContractsProvider;

    public Set<TransferResponseDto> getMintingTransferHashes(FiatCurrency fiatCurrency) {
        final Contract contract = web3ContractsProvider.getClient(fiatCurrency).getContract();
        final Set<TransfersLog> transfers = transfersLogRepository.getMintingTransfers(contract);

        final Set<TransferResponseDto> response = new HashSet<>();
        transfers.forEach(tx -> response.add(model2dto(tx)));
        return response;
    }

    public TransfersLog getTransferByHash(String hash) {
        return transfersLogRepository.getTransferByHash(hash);
    }

    public boolean isTransferLoanProvided(TransfersLog transfersLog) {
        return transfersLog.getLoan() == null;
    }

    public TransferResponseDto model2dto(TransfersLog log) {
        final TransferResponseDto dto = new TransferResponseDto();
        dto.setUid(log.getUid());
        dto.setAddressFrom(log.getAddressFrom());
        dto.setAddressTo(log.getAddressTo());
        dto.setHash(log.getHash());
        dto.setValue(log.getValue());

        final Web3JClient client = web3ContractsProvider.getClient(FiatCurrency.USD); // TODO: change to correct fiat
        final BigDecimal tokens = client.value2token(log.getValue());
        dto.setTokens(tokens);

        return dto;
    }
}
