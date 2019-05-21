package io.qdao.scanner.dto;

import io.qdao.scanner.utils.Utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RatesResponseDto implements Serializable {

    private final Double commonRate;

    private final BigDecimal commonKrwRate; // TODO: move to array

    private final String formattedCommonRate;

    private final String formattedCommonKrwRate;

    private final List<ExchangeRateDto> exchanges = new ArrayList<>();

    public RatesResponseDto(Double commonRate, List<ExchangeRateDto> exchanges, BigDecimal commonKrwRate) {
        this.commonRate = commonRate;
        this.commonKrwRate = commonKrwRate;
        this.formattedCommonRate = Utils.currencyFormatter(commonRate);;
        this.formattedCommonKrwRate = Utils.currencyFormatter(commonKrwRate.doubleValue());
        this.exchanges.addAll(exchanges);
    }

    public Double getCommonRate() {
        return commonRate;
    }

    public List<ExchangeRateDto> getExchanges() {
        return exchanges;
    }

    public String getFormattedCommonRate() {
        return formattedCommonRate;
    }

    public BigDecimal getCommonKrwRate() {
        return commonKrwRate;
    }

    public String getFormattedCommonKrwRate() {
        return formattedCommonKrwRate;
    }
}
