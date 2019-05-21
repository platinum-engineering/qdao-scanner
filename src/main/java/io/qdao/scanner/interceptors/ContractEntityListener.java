package io.qdao.scanner.interceptors;

import io.qdao.scanner.components.SocketConnector;
import io.qdao.scanner.models.Contract;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Component
public class ContractEntityListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketConnector connector;

    @PostPersist
    @PostUpdate
    private void onChangeContract(Contract contract) throws Exception {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Change contract: %s", contract.getAddress()));
        }
    }
}
