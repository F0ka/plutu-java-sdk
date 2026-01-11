package ly.plutu.sdk;

import com.fasterxml.jackson.databind.ObjectMapper;
import ly.plutu.sdk.client.PlutuHttpClient;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.service.*;
import ly.plutu.sdk.service.impl.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class PlutuClientImpl implements PlutuClient {

    private final PlutuConfig config;
    private final PlutuHttpClient httpClient;
    private final ObjectMapper mapper = new ObjectMapper();

    private final AdfaliService adfaliService;
    private final SadadService sadadService;
    private final LocalBankCardsService localBankCardsService;
    private final TlyncService tlyncService;
    private final MpgsService mpgsService;

    public PlutuClientImpl(PlutuConfig config, PlutuHttpClient httpClient) {
        this.config = config;
        this.httpClient = httpClient;

        BiFunction<String, Map<String, String>, PlutuRawResponse> caller = this::callPostForm;

        this.adfaliService = new AdfaliServiceImpl(config, caller);
        this.sadadService = new SadadServiceImpl(config, caller);
        this.localBankCardsService = new LocalBankCardsServiceImpl(config, caller);
        this.tlyncService = new TlyncServiceImpl(config, caller);
        this.mpgsService = new MpgsServiceImpl(config, caller);
    }

    @Override
    public AdfaliService adfali() {
        return adfaliService;
    }

    @Override
    public SadadService sadad() {
        return sadadService;
    }

    @Override
    public LocalBankCardsService localBankCards() {
        return localBankCardsService;
    }

    @Override
    public TlyncService tlync() {
        return tlyncService;
    }

    @Override
    public MpgsService mpgs() {
        return mpgsService;
    }

    @Override
    public PlutuRawResponse postToGateway(String gateway, String action, java.util.Map<String, String> params) throws Exception {
        String url = String.format("%s/v1/transaction/%s/%s", config.getBaseUrl(), gateway, action);
        return callPostForm(url, params);
    }

    private PlutuRawResponse callPostForm(String url, Map<String, String> params) {
        try {
            Map<String, String> headers = new HashMap<>();
            headers.put("Accept", "application/json");
            headers.put("X-API-KEY", config.getApiKey());
            headers.put("Authorization", "Bearer " + config.getAccessToken());
            return httpClient.postForm(url, params, headers);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
