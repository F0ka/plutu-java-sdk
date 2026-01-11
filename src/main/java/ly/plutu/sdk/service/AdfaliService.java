package ly.plutu.sdk.service;

import ly.plutu.sdk.service.response.PlutuAdfaliResponse;

import java.math.BigDecimal;

public interface AdfaliService {

    PlutuAdfaliResponse verify(String mobileNumber, BigDecimal amount) throws Exception;

    PlutuAdfaliResponse confirm(String processId, String code, BigDecimal amount, String invoiceNo, String customerIp) throws Exception;

}
