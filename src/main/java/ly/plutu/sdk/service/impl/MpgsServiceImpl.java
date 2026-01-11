package ly.plutu.sdk.service.impl;

import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.client.PlutuResponseHandler;
import ly.plutu.sdk.service.MpgsService;
import ly.plutu.sdk.service.response.PlutuMpgsCallback;
import ly.plutu.sdk.service.response.PlutuMpgsResponse;
import ly.plutu.sdk.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class MpgsServiceImpl implements MpgsService {

    private final PlutuConfig config;
    private final BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync;

    public MpgsServiceImpl(PlutuConfig config, BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync) {
        this.config = config;
        this.callerSync = callerSync;
    }

    @Override
    public PlutuMpgsResponse confirm(BigDecimal amount, String invoiceNo, String returnUrl, String customerIp, String lang) throws Exception {
        ValidationUtils.requireSecretKey(config.getSecretKey());
        ValidationUtils.validateAmount(amount);
        ValidationUtils.validateInvoiceNo(invoiceNo);
        ValidationUtils.validateUrl(returnUrl);

        Map<String, String> params = new HashMap<>();
        params.put("amount", amount.toPlainString());
        params.put("invoice_no", invoiceNo);
        params.put("return_url", returnUrl);
        if (lang != null && !lang.isEmpty()) params.put("lang", lang);
        if (customerIp != null && !customerIp.isEmpty()) params.put("customer_ip", customerIp);

        String url = String.format("%s/v1/transaction/mpgs/confirm", config.getBaseUrl());
        PlutuRawResponse raw = callerSync.apply(url, params);
        
        return new PlutuMpgsResponse(PlutuResponseHandler.parseOrThrow(raw));
    }

    @Override
    public PlutuMpgsCallback handleCallback(Map<String, String> parameters) {
        ValidationUtils.requireSecretKey(config.getSecretKey());
        return new PlutuMpgsCallback(parameters);
    }
}
