package com.luke.skeleton.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

public class GsonUtils {

    /**
     * Returns GsonBuilder with default for this project parameters
     */
    public static GsonBuilder getDefaultGsonBuilder() {
        GsonBuilder builder = new GsonBuilder();

        builder.setExclusionStrategies(new AnnotationExclusionStrategy());

        builder.registerTypeAdapter(DateTime.class, new JsonDeserializer<DateTime>() {

            @Override
            public DateTime deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
                String dateString = json.getAsString();
                if (android.text.TextUtils.isEmpty(dateString)) {
                    return null;
                }

                return DateUtils.getIsoDateTimeFormatter().parseDateTime(dateString);
            }
        });

        builder.registerTypeAdapter(DateTime.class, new JsonSerializer<DateTime>() {

            @Override
            public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) {
                    return null;
                }

                String dateString = DateUtils.getIsoDateTimeFormatter().print(src);
                return new JsonPrimitive(dateString);
            }
        });

        builder.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {

            @Override
            public LocalDate deserialize(JsonElement json, Type typeOfT,
                                         JsonDeserializationContext context) throws JsonParseException {
                String timeString = json.getAsString();
                if (android.text.TextUtils.isEmpty(timeString)) {
                    return null;
                }

                return DateUtils.getShortDateTimeFormatter().parseLocalDate(timeString);
            }
        });

        builder.registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {

            @Override
            public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == null) {
                    return null;
                }

                String timeString = DateUtils.getShortDateTimeFormatter().print(src);
                return new JsonPrimitive(timeString);
            }
        });

        return builder;
    }

    /**
     * Returns Gson with default for this project parameters
     */
    public static Gson getDefaultGson() {
        return getDefaultGsonBuilder().create();
    }

    /**
     * An annotation that indicates this member should be excluded from JSON
     * serialization or deserialization.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    public @interface Exclude {}

    private static class AnnotationExclusionStrategy implements ExclusionStrategy {

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return f.getAnnotation(Exclude.class) != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return false;
        }
    }

}
