package ly.plutu.sdk.service.response;

import java.util.Map;

public class PlutuTlyncCallback {
    private final Map<String, String> parameters;

    public PlutuTlyncCallback(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public boolean isApprovedTransaction() {
        return "1".equals(parameters.get("approved"));
    }

    public boolean isCanceledTransaction() {
        return "1".equals(parameters.get("canceled"));
    }

    public String getParameter(String key) {
        return parameters.get(key);
    }
}
