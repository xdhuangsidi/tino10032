package com.tino.core.api.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Map;

public final class Json {
    private Json() {
    }

    public static String encode(StringMap map) {
        return new Gson().toJson(map.map());
    }

    public static <T> T decode(String json, Class<T> classOfT) {
        return new Gson().fromJson(json,  classOfT);
    }

    public static StringMap decode(String json) {
        return new StringMap((Map) new Gson().fromJson(json, new TypeToken<Map<String, Object>>() {
        }.getType()));
    }
}
