package ly.plutu.sdk.service.response;

import ly.plutu.sdk.model.PlutuApiResponse;

public class PlutuSadadResponse {
    private final PlutuApiResponse original;

    public PlutuSadadResponse(PlutuApiResponse original) {
        this.original = original;
    }

    public PlutuApiResponse getOriginalResponse() {
        return original;
    }

    public String getProcessId() {
        Object v = original.getResult().get("process_id");
        return v != null ? v.toString() : null;
    }

    public String getTransactionId() {
        Object v = original.getResult().get("transaction_id");
        return v != null ? v.toString() : null;
    }
}
