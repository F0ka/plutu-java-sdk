package ly.plutu.sdk.service.response;

import ly.plutu.sdk.model.PlutuApiResponse;

import java.util.Map;

public class PlutuAdfaliResponse {
    private final PlutuApiResponse original;

    public PlutuAdfaliResponse(PlutuApiResponse original) {
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

    public boolean isSuccessful() {
        return original.isSuccessful();
    }
}
