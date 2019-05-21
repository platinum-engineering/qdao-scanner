package io.qdao.scanner.repositories;

import io.qdao.scanner.models.Contract;
import io.qdao.scanner.models.Loan;
import io.qdao.scanner.models.TransfersLog;
import io.qdao.scanner.types.LoanStatus;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface LoansRepository extends JpaRepository<Loan, Long> {

    Loan findFirstByTransfersLog(TransfersLog transfersLog);

    List<Loan> findByStatus(LoanStatus status, Sort sort);

    @Query("SELECT l FROM Loan l INNER JOIN l.transfersLog t INNER JOIN t.contract c WHERE c=:contract")
    List<Loan> findByContract(@Param("contract") Contract contract, Sort sort);

    @Query("SELECT l FROM Loan l INNER JOIN l.transfersLog t INNER JOIN t.contract c WHERE l.status=:status AND c=:contract")
    List<Loan> findByStatusAndContract(@Param("status") LoanStatus status, @Param("contract") Contract contract, Sort sort);

    @Query("SELECT SUM(l.collateralizedValue) FROM Loan l WHERE l.status=:status")
    BigDecimal totalCollateralizedByStatus(@Param("status") LoanStatus status);

    default BigDecimal actualTotalCollateralized() {
        return totalCollateralizedByStatus(LoanStatus.STABLE);
    }
}
