package io.qdao.scanner.utils;

import io.qdao.scanner.types.FiatCurrency;

public final class Topics {

    private Topics() { }

    public static final String ENDPOINT_CONNECT = "/connect";
    public static final String SUBSCRIBE_USER_PREFIX = "/private";
    public static final String SUBSCRIBE_USER_REPLY = "/reply";
    public static final String SUBSCRIBE_QUEUE = "/queue";
    public static final String ENDPOINT_REGISTER = "/register";

    public static final String TOPIC_TIME = SUBSCRIBE_QUEUE + "/v1/system/time";
    public static final String TOPIC_RATE = SUBSCRIBE_QUEUE + "/v1/system/rate";

    private static final String TOPIC_CONTRACT = SUBSCRIBE_QUEUE + "/v1/%s/contract";
    private static final String TOPIC_TRANSFERS = SUBSCRIBE_QUEUE + "/v1/%s/transfers";
    private static final String TOPIC_LOANS = SUBSCRIBE_QUEUE + "/v1/%s/loans";

    public static String transfersTopic(FiatCurrency fiatCurrency) {
        return String.format(TOPIC_TRANSFERS, fiatCurrency.name());
    }

    public static String contractTopic(FiatCurrency fiatCurrency) {
        return String.format(TOPIC_CONTRACT, fiatCurrency.name());
    }

    public static String loansTopic(FiatCurrency fiatCurrency) {
        return String.format(TOPIC_LOANS, fiatCurrency.name());
    }
}
