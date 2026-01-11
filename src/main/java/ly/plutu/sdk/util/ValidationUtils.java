package ly.plutu.sdk.util;

import ly.plutu.sdk.exception.*;

import java.math.BigDecimal;
import java.time.Year;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private static final Pattern MOBILE_PATTERN = Pattern.compile("^09[1-6][0-9]{7}$");
    private static final Pattern SADAD_MOBILE_PATTERN = Pattern.compile("^09[13][0-9]{7}$");
    private static final Pattern INVOICE_PATTERN = Pattern.compile("^[A-Za-z0-9._-]+$");

    private ValidationUtils() {}

    public static void requireApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) throw new InvalidApiKeyException("Api key is not configured");
    }

    public static void requireAccessToken(String token) {
        if (token == null || token.trim().isEmpty()) throw new InvalidAccessTokenException("Access token is not configured");
    }

    public static void requireSecretKey(String secret) {
        if (secret == null || secret.trim().isEmpty()) throw new InvalidSecretKeyException("Secret key is not configured");
    }

    public static void validateMobileNumber(String mobile) {
        if (mobile == null || !MOBILE_PATTERN.matcher(mobile).matches()) throw new InvalidArgumentException("Invalid mobile number format");
    }

    public static void validateSadadMobileNumber(String mobile) {
        if (mobile == null || !SADAD_MOBILE_PATTERN.matcher(mobile).matches()) throw new InvalidArgumentException("Invalid Sadad mobile number format");
    }

    public static void validateBirthYear(int year) {
        int current = Year.now().getValue();
        if (year < 1940 || year > current - 12) throw new InvalidArgumentException("Invalid birth year");
    }

    public static void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new InvalidArgumentException("Invalid amount");
    }

    public static void validateProcessId(String processId) {
        if (processId == null || !processId.matches("^\\d+$")) throw new InvalidArgumentException("Invalid process id");
    }

    public static void validateCode(String code, int length) {
        if (code == null || !code.matches("^\\d{" + length + "}$")) throw new InvalidArgumentException("Invalid code length");
    }

    public static void validateInvoiceNo(String invoice) {
        if (invoice == null || invoice.trim().isEmpty() || !INVOICE_PATTERN.matcher(invoice).matches()) throw new InvalidArgumentException("Invalid invoice number");
    }

    public static void validateUrl(String url) {
        try {
            java.net.URI.create(url).toURL();
        } catch (Exception ex) {
            throw new InvalidArgumentException("Invalid URL: " + url);
        }
    }
}
