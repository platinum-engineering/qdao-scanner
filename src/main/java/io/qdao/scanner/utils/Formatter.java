package io.qdao.scanner.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Formatter {

    private static final String LOCALIZE_KEY_PREFIX = "lang.";

    @Autowired
    private Messages messages;

    public String format(String template, Object... args) {
        if (template == null) { return null; }

        String msg = template;
        if (template.startsWith(LOCALIZE_KEY_PREFIX)) {
            final String key = template.substring(LOCALIZE_KEY_PREFIX.length());
            msg = messages.get(key, Locale.US);
        }

        final StringBuilder sb = new StringBuilder();
        final int len = msg.length();
        int j = 0;
        for (int i = 0; i < len; i++) {
            final char ch = msg.charAt(i);
            boolean isArg = false;
            if (ch == '$' && i < len - 1 && j < args.length) {
                final char chNext = msg.charAt(i + 1);
                if (chNext == '@') {
                    isArg = true;

                    Object object = args[j];
                    final String th = templateAtClass(object);
                    sb.append(th);

                    i += 1;
                    j += 1;
                }
            }

            if (!isArg) {
                sb.append(ch);
            }
        }

        return args.length > 0 ? String.format(sb.toString(), args) : sb.toString();
    }

    private String templateAtClass(Object object) {
        final Class cl = object.getClass();
        if (String.class.equals(cl)) {
            return "%s";
        } else if (Integer.class.equals(cl) || Long.class.equals(cl)) {
            return "%d";
        } else if (Float.class.equals(cl) || Double.class.equals(cl)) {
            return "%f";
        }
        return object.toString();
    }

}
