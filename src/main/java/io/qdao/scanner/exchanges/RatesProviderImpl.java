package io.qdao.scanner.exchanges;

import com.google.gson.Gson;
import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.configs.RabbitmqConfiguration;
import io.qdao.scanner.dto.RatesResponseDto;
import io.qdao.scanner.services.ExchangesService;
import io.qdao.scanner.types.FiatCurrency;
import io.qdao.scanner.utils.CoinMarketCap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
class RatesProviderImpl implements RatesProvider {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ExchangesService exchangesService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Gson gson;

    private Map<FiatCurrency, BigDecimal> rates = Collections.synchronizedMap(new HashMap<>());

    @PostConstruct
    @Override
    public void update() {
        final BigDecimal current = exchangesService.makeCommonCurrentRate();
        rates.put(FiatCurrency.USD, current);

        final CoinMarketCap.Ticker ticker = updateCurrency(FiatCurrency.KRW);
        if (ticker != null && ticker.getPriceKrw() != null && ticker.getPriceUsd() != null) {
            final BigDecimal rateUsd = ticker.getPriceUsd();
            final BigDecimal rateKrw = ticker.getPriceKrw();


            final BigDecimal value = rateKrw.divide(rateUsd, Utils.DEFAULT_DECIMALS, RoundingMode.HALF_UP).multiply(current);
            rates.put(FiatCurrency.KRW, value);

            rabbitTemplate.setExchange(RabbitmqConfiguration.FIAT_RATES_FANOUT);
            final String message = gson.toJson(rates);
            rabbitTemplate.convertAndSend(message);
        } else {
            logger.error("Incorrect information form CoinMarketCap. Please check it.");
        }

    }

    @Override
    public BigDecimal get(FiatCurrency fiatCurrency) {
        return rates.get(fiatCurrency);
    }

    @Override
    public RatesResponseDto fullDto() {
        return exchangesService.makeActualDto(this);
    }

    private CoinMarketCap.Ticker updateCurrency(FiatCurrency fiatCurrency) {
        try {
            final CoinMarketCap cap = new CoinMarketCap();
            return cap.getTickerById("bitcoin", fiatCurrency);
        } catch (CoinMarketCap.CoinMarketCapException e) {
            logger.error(String.format("Error update coinmarketcap ticker: %s", e.getMessage()));
        }

        return null;
    }
}
