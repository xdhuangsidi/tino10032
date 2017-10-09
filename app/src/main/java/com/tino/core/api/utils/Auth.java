package com.tino.core.api.utils;

import java.net.URI;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class Auth {
    public static final String FormMime = "application/x-www-form-urlencoded";
    public static final String JsonMime = "application/json";
    private static final String[] deprecatedPolicyFields = new String[]{"asyncOps"};
    private static final String[] policyFields = new String[]{"callbackUrl", "callbackBody", "callbackHost", "callbackBodyType", "callbackFetchKey", "returnUrl", "returnBody", "endUser", "saveKey", "insertOnly", "detectMime", "mimeLimit", "fsizeLimit", "fsizeMin", "persistentOps", "persistentNotifyUrl", "persistentPipeline"};
    private final String accessKey;
    private final SecretKeySpec secretKey;

    private Auth(String accessKey, SecretKeySpec secretKeySpec) {
        this.accessKey = accessKey;
        this.secretKey = secretKeySpec;
    }

    public static Auth create(String accessKey, String secretKey) {
        if (!StringUtils.isNullOrEmpty(accessKey) && !StringUtils.isNullOrEmpty(secretKey)) {
            return new Auth(accessKey, new SecretKeySpec(StringUtils.utf8Bytes(secretKey), "HmacSHA1"));
        }
        throw new IllegalArgumentException("empty key");
    }

    private static void copyPolicy(final StringMap policy, StringMap originPolicy, final boolean strict) {
        if (originPolicy != null) {
            originPolicy.forEach(new StringMap.Consumer() {
                public void accept(String key, Object value) {
                    if (StringUtils.inStringArray(key, Auth.deprecatedPolicyFields)) {
                        throw new IllegalArgumentException(key + " is deprecated!");
                    } else if (!strict || StringUtils.inStringArray(key, Auth.policyFields)) {
                        policy.put(key, value);
                    }
                }
            });
        }
    }

    private Mac createMac() {
        try {
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(this.secretKey);
            return mac;
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e);
        }
    }

    public String sign(byte[] data) {
        return this.accessKey + ":" + UrlSafeBase64.encodeToString(createMac().doFinal(data));
    }

    public String sign(String data) {
        return sign(StringUtils.utf8Bytes(data));
    }

    public String signWithData(byte[] data) {
        String s = UrlSafeBase64.encodeToString(data);
        return sign(StringUtils.utf8Bytes(s)) + ":" + s;
    }

    public String signWithData(String data) {
        return signWithData(StringUtils.utf8Bytes(data));
    }

    public String signRequest(String urlString, byte[] body, String contentType) {
        URI uri = URI.create(urlString);
        String path = uri.getRawPath();
        String query = uri.getRawQuery();
        Mac mac = createMac();
        mac.update(StringUtils.utf8Bytes(path));
        if (!(query == null || query.length() == 0)) {
            mac.update((byte) 63);
            mac.update(StringUtils.utf8Bytes(query));
        }
        mac.update((byte) 10);
        if (body != null && body.length > 0 && !StringUtils.isNullOrEmpty(contentType) && (contentType.equals(FormMime) || contentType.equals("application/json"))) {
            mac.update(body);
        }
        return this.accessKey + ":" + UrlSafeBase64.encodeToString(mac.doFinal());
    }

    public boolean isValidCallback(String originAuthorization, String url, byte[] body, String contentType) {
        return ("QBox " + signRequest(url, body, contentType)).equals(originAuthorization);
    }

    public String privateDownloadUrl(String baseUrl) {
        return privateDownloadUrl(baseUrl, 3600);
    }

    public String privateDownloadUrl(String baseUrl, long expires) {
        return privateDownloadUrlWithDeadline(baseUrl, (System.currentTimeMillis() / 1000) + expires);
    }

    String privateDownloadUrlWithDeadline(String baseUrl, long deadline) {
        StringBuilder b = new StringBuilder();
        b.append(baseUrl);
        if (baseUrl.indexOf("?") > 0) {
            b.append("&e=");
        } else {
            b.append("?e=");
        }
        b.append(deadline);
        String token = sign(StringUtils.utf8Bytes(b.toString()));
        b.append("&token=");
        b.append(token);
        return b.toString();
    }

    public String uploadToken(String bucket) {
        return uploadToken(bucket, null, 3600, null, true);
    }

    public String uploadToken(String bucket, String key) {
        return uploadToken(bucket, key, 3600, null, true);
    }

    public String uploadToken(String bucket, String key, long expires, StringMap policy) {
        return uploadToken(bucket, key, expires, policy, true);
    }

    public String uploadToken(String bucket, String key, long expires, StringMap policy, boolean strict) {
        return uploadTokenWithDeadline(bucket, key, (System.currentTimeMillis() / 1000) + expires, policy, strict);
    }

    String uploadTokenWithDeadline(String bucket, String key, long deadline, StringMap policy, boolean strict) {
        String scope = bucket;
        if (key != null) {
            scope = bucket + ":" + key;
        }
        StringMap x = new StringMap();
        copyPolicy(x, policy, strict);
        x.put("scope", scope);
        x.put("deadline", Long.valueOf(deadline));
        return signWithData(StringUtils.utf8Bytes(Json.encode(x)));
    }

    public StringMap authorization(String url, byte[] body, String contentType) {
        return new StringMap().put("Authorization", "QBox " + signRequest(url, body, contentType));
    }

    public StringMap authorization(String url) {
        return authorization(url, null, null);
    }
}
