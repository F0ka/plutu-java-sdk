package ly.plutu.sdk.exception;

public class PlutuException extends RuntimeException {
    public PlutuException(String message) {
        super(message);
    }

    public PlutuException(String message, Throwable cause) {
        super(message, cause);
    }
}
