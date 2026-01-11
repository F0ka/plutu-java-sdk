package ly.plutu.sdk.service;

import ly.plutu.sdk.service.response.PlutuLocalBankCardsResponse;
import ly.plutu.sdk.service.response.PlutuLocalBankCallback;

import java.math.BigDecimal;
import java.util.Map;

public interface LocalBankCardsService {
    PlutuLocalBankCardsResponse confirm(BigDecimal amount, String invoiceNo, String returnUrl, String customerIp, String lang) throws Exception;

    PlutuLocalBankCallback handleCallback(Map<String, String> parameters);
}
