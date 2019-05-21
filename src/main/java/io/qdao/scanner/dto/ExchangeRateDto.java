package io.qdao.scanner.dto;

import java.io.Serializable;

public class ExchangeRateDto implements Serializable {

    private final String name;

    private final Double price;

    private final String formattedPrice;

    private final int lOrder;

    public ExchangeRateDto(String name, Double price, String formattedPrice, int lOrder) {
        this.name = name;
        this.price = price;
        this.formattedPrice = formattedPrice;
        this.lOrder = lOrder;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public int getlOrder() {
        return lOrder;
    }
}
