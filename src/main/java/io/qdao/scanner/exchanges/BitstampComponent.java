package io.qdao.scanner.exchanges;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.bitstamp.BitstampExchange;
import org.knowm.xchange.bitstamp.dto.marketdata.BitstampTicker;
import org.knowm.xchange.bitstamp.service.BitstampMarketDataServiceRaw;
import org.knowm.xchange.currency.CurrencyPair;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class BitstampComponent extends ExchangeKnownProvider {

    BitstampComponent() {
        super(BitstampExchange.class);
    }

    @Override
    public String getName() {
        return "Bitstamp";
    }

    @Override
    protected void updateData(Exchange exchange) throws IOException {
        final BitstampMarketDataServiceRaw marketDataService = (BitstampMarketDataServiceRaw) exchange.getMarketDataService();
        final BitstampTicker bitstampTicker = marketDataService.getBitstampTicker(CurrencyPair.BTC_USD);
        setPrice(bitstampTicker.getLast().doubleValue());
        setVolume(bitstampTicker.getVolume().doubleValue());
    }

    @Override
    public int getLOrder() {
        return 2;
    }
}
