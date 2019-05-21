package io.qdao.scanner.exchanges;

public enum CryptoCurrency {

    BTC(CryptoCurrency.BTC_ACRONYM);

    private final String acronym;

    private static final String BTC_ACRONYM = "BTC";

    private CryptoCurrency(String acronum) {
        this.acronym = acronum;
    }

    public String getAcronym() {
        return acronym;
    }
}
