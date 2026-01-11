package ly.plutu.sdk.model;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Generic API envelope mapping. The PHP SDK wraps responses similarly.
 */
public class PlutuApiResponse {

    @JsonProperty("status")
    private Integer status;

    @JsonProperty("result")
    private Map<String, Object> result = new HashMap<>();

    @JsonProperty("error")
    private PlutuError error;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public PlutuError getError() {
        return error;
    }

    public void setError(PlutuError error) {
        this.error = error;
    }

    public boolean isSuccessful() {
        return status != null && status == 200;
    }

    public Optional<Map<String, Object>> getResultOptional() {
        return Optional.ofNullable(result);
    }

    public String getErrorMessage() {
        return error != null ? error.getMessage() : null;
    }

    public String getErrorCode() {
        return error != null ? error.getCode() : null;
    }
}
