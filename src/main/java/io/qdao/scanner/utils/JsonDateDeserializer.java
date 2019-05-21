package io.qdao.scanner.utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class JsonDateDeserializer implements JsonDeserializer<Date>, JsonSerializer<Date> {

    private static final DateFormat DEFAULT_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final DateFormat ICO8601FORMAT = new SimpleDateFormat(Utils.DATA_ISO_8601_PATTERN);

    static {
        // Default time zone, if not set (use ico8601 format)
        DEFAULT_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public JsonDateDeserializer() {
    }

    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final String s = json.getAsJsonPrimitive().getAsString();
        try {
            return DEFAULT_FORMAT.parse(s);
        } catch (ParseException e) {
            try {
                return ICO8601FORMAT.parse(s);
            } catch (ParseException ex) {
                throw new JsonParseException(ex);
            }
        }
    }

    @Override
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        if (src == null) {
            return null;
        }
        return new JsonPrimitive(ICO8601FORMAT.format(src));
    }
}
