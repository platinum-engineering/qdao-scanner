package io.qdao.scanner.dto;

import io.qdao.scanner.exchanges.CryptoCurrency;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

public class CurrencyItemDto implements Serializable {

    private CryptoCurrency currency;

    private BigDecimal volume;

    public CryptoCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(CryptoCurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrencyItemDto that = (CurrencyItemDto) o;
        return currency == that.currency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(currency);
    }
}
