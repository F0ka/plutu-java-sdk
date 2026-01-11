package ly.plutu.sdk.service.impl;

import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.client.PlutuResponseHandler;
import ly.plutu.sdk.service.TlyncService;
import ly.plutu.sdk.service.response.PlutuTlyncCallback;
import ly.plutu.sdk.service.response.PlutuTlyncResponse;
import ly.plutu.sdk.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class TlyncServiceImpl implements TlyncService {

    private final PlutuConfig config;
    private final BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync;

    public TlyncServiceImpl(PlutuConfig config, BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync) {
        this.config = config;
        this.callerSync = callerSync;
    }

    @Override
    public PlutuTlyncResponse confirm(String mobileNumber, BigDecimal amount, String invoiceNo, String returnUrl, String callbackUrl, String customerIp, String lang) throws Exception {
        ValidationUtils.requireSecretKey(config.getSecretKey());
        ValidationUtils.validateMobileNumber(mobileNumber);
        ValidationUtils.validateAmount(amount);
        ValidationUtils.validateInvoiceNo(invoiceNo);
        ValidationUtils.validateUrl(returnUrl);
        ValidationUtils.validateUrl(callbackUrl);

        Map<String, String> params = new HashMap<>();
        params.put("mobile_number", mobileNumber);
        params.put("amount", amount.toPlainString());
        params.put("invoice_no", invoiceNo);
        params.put("return_url", returnUrl);
        params.put("callback_url", callbackUrl);
        if (lang != null && !lang.isEmpty()) params.put("lang", lang);
        if (customerIp != null && !customerIp.isEmpty()) params.put("customer_ip", customerIp);

        String url = String.format("%s/v1/transaction/tlync/confirm", config.getBaseUrl());
        PlutuRawResponse raw = callerSync.apply(url, params);
        
        return new PlutuTlyncResponse(PlutuResponseHandler.parseOrThrow(raw));
    }

    @Override
    public PlutuTlyncCallback handleCallback(Map<String, String> parameters) {
        ValidationUtils.requireSecretKey(config.getSecretKey());
        return new PlutuTlyncCallback(parameters);
    }

    @Override
    public PlutuTlyncCallback handleReturn(Map<String, String> parameters) {
        ValidationUtils.requireSecretKey(config.getSecretKey());
        return new PlutuTlyncCallback(parameters);
    }
}
