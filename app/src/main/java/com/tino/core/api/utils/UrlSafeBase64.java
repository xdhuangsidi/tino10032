package com.tino.core.api.utils;

import com.bumptech.glide.load.Key;

import java.nio.charset.Charset;

public final class UrlSafeBase64 {
    private UrlSafeBase64() {
    }

    public static String encodeToString(String data) {
        return encodeToString(data.getBytes(Charset.forName(Key.STRING_CHARSET_NAME)));
    }

    public static String encodeToString(byte[] data) {
        return Base64.encodeToString(data, 10);
    }

    public static byte[] decode(String data) {
        return Base64.decode(data, 10);
    }
}
