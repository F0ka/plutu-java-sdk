package ly.plutu.sdk.service.impl;

import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.client.PlutuResponseHandler;
import ly.plutu.sdk.service.AdfaliService;
import ly.plutu.sdk.service.response.PlutuAdfaliResponse;
import ly.plutu.sdk.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class AdfaliServiceImpl implements AdfaliService {

    private final PlutuConfig config;
    private final BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync;

    public AdfaliServiceImpl(PlutuConfig config, BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync) {
        this.config = config;
        this.callerSync = callerSync;
    }

    @Override
    public PlutuAdfaliResponse verify(String mobileNumber, BigDecimal amount) throws Exception {
        ValidationUtils.requireApiKey(config.getApiKey());
        ValidationUtils.requireAccessToken(config.getAccessToken());
        ValidationUtils.validateMobileNumber(mobileNumber);
        ValidationUtils.validateAmount(amount);

        Map<String, String> params = new HashMap<>();
        params.put("mobile_number", mobileNumber);
        params.put("amount", amount.toPlainString());

        String url = String.format("%s/v1/transaction/edfali/verify", config.getBaseUrl());
        PlutuRawResponse raw = callerSync.apply(url, params);
        
        return new PlutuAdfaliResponse(PlutuResponseHandler.parseOrThrow(raw));
    }

    @Override
    public PlutuAdfaliResponse confirm(String processId, String code, BigDecimal amount, String invoiceNo, String customerIp) throws Exception {
        ValidationUtils.requireApiKey(config.getApiKey());
        ValidationUtils.requireAccessToken(config.getAccessToken());
        ValidationUtils.validateProcessId(processId);
        ValidationUtils.validateCode(code, 4);
        ValidationUtils.validateAmount(amount);
        ValidationUtils.validateInvoiceNo(invoiceNo);

        Map<String, String> params = new HashMap<>();
        params.put("process_id", processId);
        params.put("code", code);
        params.put("amount", amount.toPlainString());
        params.put("invoice_no", invoiceNo);
        if (customerIp != null && !customerIp.isEmpty()) params.put("customer_ip", customerIp);

        String url = String.format("%s/v1/transaction/edfali/confirm", config.getBaseUrl());
        PlutuRawResponse raw = callerSync.apply(url, params);
        
        return new PlutuAdfaliResponse(PlutuResponseHandler.parseOrThrow(raw));
    }
}
