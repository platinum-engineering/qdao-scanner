package io.qdao.scanner.interceptors;

import io.qdao.scanner.exchanges.ExchangeProvider;
import io.qdao.scanner.web3j.Web3jDisposableManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
class ApplicationInterceptor implements DisposableBean {

    @Autowired
    private Set<ExchangeProvider> exchanges;

    @Autowired
    private Web3jDisposableManager web3jDisposableManager;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void destroy() throws Exception {
        for (ExchangeProvider exchange : exchanges) {
            try {
                exchange.disconnect();
            } catch (Throwable e) {
                logger.warn(String.format("Exchange provider[%s] disconnect error: %s", exchange.getClass().getName(), e.getMessage()));
            }
        }

        try {
            web3jDisposableManager.disposeAll();
        } catch (Throwable e) {
            logger.warn(String.format("Web3j dispose error: %s", e.getMessage()));
        }

        logger.info("Application is destroy.");
    }
}
