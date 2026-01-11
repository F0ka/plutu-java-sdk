package ly.plutu.sdk.service.impl;

import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.client.PlutuResponseHandler;
import ly.plutu.sdk.service.SadadService;
import ly.plutu.sdk.service.response.PlutuSadadResponse;
import ly.plutu.sdk.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class SadadServiceImpl implements SadadService {

    private final PlutuConfig config;
    private final BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync;

    public SadadServiceImpl(PlutuConfig config, BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync) {
        this.config = config;
        this.callerSync = callerSync;
    }

    @Override
    public PlutuSadadResponse verify(String mobileNumber, int birthYear, BigDecimal amount) throws Exception {
        ValidationUtils.requireApiKey(config.getApiKey());
        ValidationUtils.requireAccessToken(config.getAccessToken());
        ValidationUtils.validateSadadMobileNumber(mobileNumber);
        ValidationUtils.validateBirthYear(birthYear);
        ValidationUtils.validateAmount(amount);

        Map<String, String> params = new HashMap<>();
        params.put("mobile_number", mobileNumber);
        params.put("birth_year", String.valueOf(birthYear));
        params.put("amount", amount.toPlainString());

        String url = String.format("%s/v1/transaction/sadadapi/verify", config.getBaseUrl());
        PlutuRawResponse raw = callerSync.apply(url, params);
        
        return new PlutuSadadResponse(PlutuResponseHandler.parseOrThrow(raw));
    }

    @Override
    public PlutuSadadResponse confirm(String processId, String code, BigDecimal amount, String invoiceNo, String customerIp) throws Exception {
        ValidationUtils.requireApiKey(config.getApiKey());
        ValidationUtils.requireAccessToken(config.getAccessToken());
        ValidationUtils.validateProcessId(processId);
        ValidationUtils.validateCode(code, 6);
        ValidationUtils.validateAmount(amount);
        ValidationUtils.validateInvoiceNo(invoiceNo);

        Map<String, String> params = new HashMap<>();
        params.put("process_id", processId);
        params.put("code", code);
        params.put("amount", amount.toPlainString());
        params.put("invoice_no", invoiceNo);
        if (customerIp != null && !customerIp.isEmpty()) params.put("customer_ip", customerIp);

        String url = String.format("%s/v1/transaction/sadadapi/confirm", config.getBaseUrl());
        PlutuRawResponse raw = callerSync.apply(url, params);
        
        return new PlutuSadadResponse(PlutuResponseHandler.parseOrThrow(raw));
    }
}
