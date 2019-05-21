package io.qdao.scanner.exchanges;

import io.qdao.scanner.dto.RatesResponseDto;
import io.qdao.scanner.types.FiatCurrency;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public interface RatesProvider {

    void update();

    BigDecimal get(FiatCurrency fiatCurrency);

    RatesResponseDto fullDto();
}
