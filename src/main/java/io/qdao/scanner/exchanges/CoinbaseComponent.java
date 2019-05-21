package io.qdao.scanner.exchanges;

import org.knowm.xchange.Exchange;
import org.knowm.xchange.coinbase.v2.CoinbaseExchange;
import org.knowm.xchange.coinbase.v2.dto.CoinbasePrice;
import org.knowm.xchange.coinbase.v2.service.CoinbaseMarketDataService;
import org.knowm.xchange.currency.Currency;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
class CoinbaseComponent extends ExchangeKnownProvider {

    CoinbaseComponent() {
        super(CoinbaseExchange.class);
    }

    @Override
    public String getName() {
        return "Coinbase";
    }

    @Override
    protected void updateData(Exchange exchange) throws IOException {
        final CoinbaseMarketDataService marketDataService = (CoinbaseMarketDataService) exchange.getMarketDataService();
        final CoinbasePrice spotRate = marketDataService.getCoinbaseSpotRate(Currency.BTC, Currency.USD);
        setPrice(spotRate.getAmount().doubleValue());
    }

    @Override
    public int getLOrder() {
        return 3;
    }
}
