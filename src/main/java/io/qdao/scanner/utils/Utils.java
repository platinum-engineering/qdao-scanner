package io.qdao.scanner.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static final int DEFAULT_DECIMALS = 8;

    public static final BigDecimal HUNDRED = new BigDecimal(100);

    public static final String ZERO_ADDRESS = "0x0000000000000000000000000000000000000000";

    public static final String DATA_ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private static final NumberFormat CURRENCY_FORMAT = new DecimalFormat("0.00");

    private static final NumberFormat CRYPTO_CURRENCY_FORMAT = new DecimalFormat("0.00000000");

    private Utils() {   }

    public static String getCurrentFormattedTime() {
        return TIME_FORMAT.format(new Date());
    }

    public static String currencyFormatter(double value) {
        return CURRENCY_FORMAT.format(value).replace(",", ".");
    }

    public static String cryptoCurrencyFormatter(double value) {
        return CRYPTO_CURRENCY_FORMAT.format(value).replace(",", ".");
    }

    public static String makeValidateFolderPath(String path) {
        return path.endsWith("/") ? path : path.concat("/");
    }

    public static void waitProcess(long millis) {
        try { Thread.sleep(millis);} catch (Throwable ignored) {}
    }

}
