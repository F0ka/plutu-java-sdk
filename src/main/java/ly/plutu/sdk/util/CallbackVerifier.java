package ly.plutu.sdk.util;

import ly.plutu.sdk.exception.InvalidCallbackHashException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Verifies callback SHA256 HMAC hash similar to PHP implementation.
 */
public final class CallbackVerifier {

    private CallbackVerifier() {}

    public static void verify(Map<String, String> parameters, List<String> keysInOrder, String secretKey) {
        String provided = parameters.get("hashed");
        if (provided == null || provided.isEmpty()) throw new InvalidCallbackHashException("Missing hashed parameter");

        String data = buildQuery(parameters, keysInOrder);
        String generated = hmacSha256Hex(secretKey, data).toUpperCase();
        if (!constantTimeEquals(generated, provided)) {
            throw new InvalidCallbackHashException("Hash verification failed");
        }
    }

    private static String buildQuery(Map<String, String> params, List<String> keys) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String k : keys) {
            if (!params.containsKey(k)) continue;
            if (!first) sb.append('&');
            first = false;
            sb.append(URLEncoder.encode(k, StandardCharsets.UTF_8)).append('=')
                    .append(URLEncoder.encode(params.get(k), StandardCharsets.UTF_8));
        }
        return sb.toString();
    }

    private static String hmacSha256Hex(String secret, String data) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : raw) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean constantTimeEquals(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int res = 0;
        for (int i = 0; i < a.length(); i++) res |= a.charAt(i) ^ b.charAt(i);
        return res == 0;
    }
}
