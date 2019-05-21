package io.qdao.scanner.services;

import io.qdao.scanner.utils.Utils;
import io.qdao.scanner.dto.ExchangeRateDto;
import io.qdao.scanner.dto.RatesResponseDto;
import io.qdao.scanner.exchanges.ExchangeProvider;
import io.qdao.scanner.exchanges.RatesProvider;
import io.qdao.scanner.types.FiatCurrency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class ExchangesService {

    @Autowired
    private List<ExchangeProvider> exchanges;

    public RatesResponseDto makeActualDto(RatesProvider ratesProvider) {
        final List<ExchangeRateDto> dtos = new ArrayList<>();

        long count = 0;
        double commonRate = 0d;
        for (ExchangeProvider exchange : exchanges) {
            final String name = exchange.getName();
            final Double price = exchange.getPrice();
            if (price != null) {
                commonRate += price;
                count += 1;
            }

            final int lOrder = exchange.getLOrder();
            dtos.add(exc2dto(name, price, lOrder));
        }

        if (count > 0) {
            commonRate /= count;
        }

        dtos.sort(Comparator.comparing(ExchangeRateDto::getlOrder));

        final BigDecimal krw = ratesProvider.get(FiatCurrency.KRW);
        return new RatesResponseDto(commonRate, dtos, krw);
    }

    public BigDecimal makeCommonCurrentRate() {
        long count = 0;
        double commonRate = 0d;
        for (ExchangeProvider exchange : exchanges) {
            final Double price = exchange.getPrice();
            if (price != null) {
                commonRate += price;
                count += 1;
            }
        }

        if (count > 0) {
            commonRate /= count;
        }

        return new BigDecimal(commonRate);
    }

    public ExchangeRateDto exc2dto(String name, Double price, int lOrder) {
        final String formatted;
        if (price != null) {
            formatted = Utils.currencyFormatter(price);
        } else {
            formatted = null;
        }
        return new ExchangeRateDto(name, price, formatted, lOrder);
    }
}
