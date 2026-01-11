package ly.plutu.sdk;

import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.service.*;

public interface PlutuClient {

    AdfaliService adfali();
    SadadService sadad();
    LocalBankCardsService localBankCards();
    TlyncService tlync();
    MpgsService mpgs();

    // low-level helper returning raw HTTP body & status
    PlutuRawResponse postToGateway(String gateway, String action, java.util.Map<String, String> params) throws Exception;

}
