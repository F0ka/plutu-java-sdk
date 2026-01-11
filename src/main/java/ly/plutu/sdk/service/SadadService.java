package ly.plutu.sdk.service;

import ly.plutu.sdk.service.response.PlutuSadadResponse;

import java.math.BigDecimal;

public interface SadadService {
    PlutuSadadResponse verify(String mobileNumber, int birthYear, BigDecimal amount) throws Exception;
    PlutuSadadResponse confirm(String processId, String code, BigDecimal amount, String invoiceNo, String customerIp) throws Exception;
}
