package io.qdao.scanner.interceptors;

import io.qdao.scanner.components.SocketConnector;
import io.qdao.scanner.models.TransfersLog;
import io.qdao.scanner.types.FiatCurrency;
import io.qdao.scanner.utils.Topics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;

@Component
public class TransfersLogEntityListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SocketConnector connector;

    @PostPersist
    @PostUpdate
    private void onChangeTransferLog(TransfersLog tx) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Change Transfer: %s", tx.getHash()));
        }

        final FiatCurrency fiatCurrency = tx.getContract().getFiatCurrency();

        connector.notifyAll(Topics.transfersTopic(fiatCurrency), tx);
    }
}
