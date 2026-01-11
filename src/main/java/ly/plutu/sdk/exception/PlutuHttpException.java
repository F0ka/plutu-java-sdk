package ly.plutu.sdk.exception;

import ly.plutu.sdk.model.PlutuError;

public class PlutuHttpException extends PlutuException {

    private final int statusCode;
    private final PlutuError error;

    public PlutuHttpException(int statusCode, PlutuError error) {
        super(formatMessage(statusCode, error));
        this.statusCode = statusCode;
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public PlutuError getError() {
        return error;
    }

    private static String formatMessage(int statusCode, PlutuError error) {
        if (error != null && error.getMessage() != null) {
            return String.format("HTTP %d: %s (Code: %s)", statusCode, error.getMessage(), error.getCode());
        }
        return String.format("HTTP %d: Unknown error", statusCode);
    }
}
