package ly.plutu.sdk.service;

import ly.plutu.sdk.service.response.PlutuMpgsResponse;
import ly.plutu.sdk.service.response.PlutuMpgsCallback;

import java.math.BigDecimal;
import java.util.Map;

public interface MpgsService {
    PlutuMpgsResponse confirm(BigDecimal amount, String invoiceNo, String returnUrl, String customerIp, String lang) throws Exception;
    PlutuMpgsCallback handleCallback(Map<String, String> parameters);
}
