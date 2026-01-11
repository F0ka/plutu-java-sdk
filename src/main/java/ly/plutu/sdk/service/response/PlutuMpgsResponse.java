package ly.plutu.sdk.service.response;

import ly.plutu.sdk.model.PlutuApiResponse;

public class PlutuMpgsResponse {
    private final PlutuApiResponse original;

    public PlutuMpgsResponse(PlutuApiResponse original) {
        this.original = original;
    }

    public PlutuApiResponse getOriginalResponse() {
        return original;
    }

    public String getRedirectUrl() {
        Object v = original.getResult().get("redirect_url");
        return v != null ? v.toString() : null;
    }
}
