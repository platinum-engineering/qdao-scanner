package io.qdao.scanner.exchanges;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.currency.CurrencyPair;
import org.knowm.xchange.dto.marketdata.Ticker;
import org.knowm.xchange.kraken.KrakenExchange;
import org.knowm.xchange.service.marketdata.MarketDataService;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class KrakenComponent extends ExchangeKnownProvider  {

    KrakenComponent() {
        super(KrakenExchange.class);
    }

    @Override
    public String getName() {
        return "Kraken";
    }

    @Override
    protected void updateData(Exchange exchange) throws IOException {
        final MarketDataService marketDataService = exchange.getMarketDataService();
        final Ticker ticker = marketDataService.getTicker(CurrencyPair.BTC_USD);
        setPrice(ticker.getLast().doubleValue());
        setVolume(ticker.getVolume().doubleValue());
    }

    @Override
    public int getLOrder() {
        return 1;
    }
}
