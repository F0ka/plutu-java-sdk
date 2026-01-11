package ly.plutu.sdk.service;

import ly.plutu.sdk.service.response.PlutuTlyncResponse;
import ly.plutu.sdk.service.response.PlutuTlyncCallback;

import java.math.BigDecimal;
import java.util.Map;

public interface TlyncService {
    PlutuTlyncResponse confirm(String mobileNumber, BigDecimal amount, String invoiceNo, String returnUrl, String callbackUrl, String customerIp, String lang) throws Exception;
    PlutuTlyncCallback handleCallback(Map<String, String> parameters);
    PlutuTlyncCallback handleReturn(Map<String, String> parameters);
}
