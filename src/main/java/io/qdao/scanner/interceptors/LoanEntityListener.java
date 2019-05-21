package io.qdao.scanner.interceptors;

import io.qdao.scanner.models.Loan;
import io.qdao.scanner.services.LoansService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Component
public class LoanEntityListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private LoansService loansService;

    @PostPersist
    @PostUpdate
    private void onChangeLoan(Loan loan) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Change Loan: %s", loan.getEthAddress()));
        }
    }
}
