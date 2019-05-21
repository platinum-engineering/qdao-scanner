package io.qdao.scanner.repositories;

import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.models.Contract;
import io.qdao.scanner.models.TransfersLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface TransfersLogRepository extends JpaRepository<TransfersLog, Long> {

    Optional<TransfersLog> findByUid(Long uid);

    Optional<TransfersLog> findFirstByHash(String hash);

    Set<TransfersLog> findByAddressFrom(String addressFrom);

    Set<TransfersLog> findByAddressTo(String addressTo);

    Set<TransfersLog> findByAddressFromAndContractAndLoanIsNullOrderByUid(String addressFrom, Contract contract);

    default Set<TransfersLog> getMintingTransfers(Contract contract) {
        return findByAddressFromAndContractAndLoanIsNullOrderByUid(Utils.ZERO_ADDRESS, contract);
    }

    default Set<TransfersLog> getBurningTransfers() {
        return findByAddressTo(Utils.ZERO_ADDRESS);
    }

    default TransfersLog getTransferByHash(String hash) {
        if (hash == null) {
            return new TransfersLog();
        }

        return findFirstByHash(hash).orElse(new TransfersLog());
    }
}
