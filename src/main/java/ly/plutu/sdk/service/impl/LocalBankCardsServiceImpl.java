package ly.plutu.sdk.service.impl;

import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.client.PlutuResponseHandler;
import ly.plutu.sdk.service.LocalBankCardsService;
import ly.plutu.sdk.service.response.PlutuLocalBankCardsResponse;
import ly.plutu.sdk.service.response.PlutuLocalBankCallback;
import ly.plutu.sdk.util.ValidationUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class LocalBankCardsServiceImpl implements LocalBankCardsService {

    private final PlutuConfig config;
    private final BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync;

    public LocalBankCardsServiceImpl(PlutuConfig config, BiFunction<String, Map<String, String>, PlutuRawResponse> callerSync) {
        this.config = config;
        this.callerSync = callerSync;
    }

    @Override
    public PlutuLocalBankCardsResponse confirm(BigDecimal amount, String invoiceNo, String returnUrl, String customerIp, String lang) throws Exception {
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

        String url = String.format("%s/v1/transaction/localbankcards/confirm", config.getBaseUrl());
        PlutuRawResponse raw = callerSync.apply(url, params);
        
        return new PlutuLocalBankCardsResponse(PlutuResponseHandler.parseOrThrow(raw));
    }

    @Override
    public PlutuLocalBankCallback handleCallback(Map<String, String> parameters) {
        ValidationUtils.requireSecretKey(config.getSecretKey());
        // in PHP SDK, callback verifies hash and filtered params; we only return callback wrapper here
        return new PlutuLocalBankCallback(parameters);
    }
}
