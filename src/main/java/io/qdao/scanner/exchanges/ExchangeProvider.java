package io.qdao.scanner.exchanges;

import org.springframework.stereotype.Component;

@Component
public interface ExchangeProvider {

    String getName();

    Double getPrice();

    Double getVolume();

    void disconnect();

    void refresh();

    default int getLOrder() {
        return 0;
    }
}
