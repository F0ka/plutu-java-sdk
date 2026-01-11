package ly.plutu.sdk.service.impl;

import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.service.response.PlutuLocalBankCardsResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

import static org.mockito.ArgumentMatchers.any;

class LocalBankCardsServiceImplTest {

    private LocalBankCardsServiceImpl localBankCardsService;
    private BiFunction<String, Map<String, String>, PlutuRawResponse> mockCaller;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        PlutuConfig config = new PlutuConfig();
        config.setApiKey("test-key");
        config.setAccessToken("test-token");
        config.setSecretKey("test-secret");
        
        mockCaller = (BiFunction<String, Map<String, String>, PlutuRawResponse>) Mockito.mock(BiFunction.class);
        localBankCardsService = new LocalBankCardsServiceImpl(config, mockCaller);
    }

    @Test
    void testConfirmSuccess() throws Exception {
        String jsonResponse = "{"
                + "\"status\": 200,"
                + "\"result\": { \"redirect_url\": \"https://example.com/pay\" }"
                + "}";
        PlutuRawResponse raw = new PlutuRawResponse(200, jsonResponse);
        
        Mockito.when(mockCaller.apply(any(), any())).thenReturn(raw);

        PlutuLocalBankCardsResponse response = localBankCardsService.confirm(
            BigDecimal.TEN, 
            "INV-001", 
            "https://example.com/return", 
            "127.0.0.1", 
            "ar"
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals("https://example.com/pay", response.getRedirectUrl());
    }

    @Test
    void testConfirmValidationError() {
        // Missing secret key scenario
        PlutuConfig config = new PlutuConfig();
        config.setApiKey("k");
        config.setAccessToken("t");
        LocalBankCardsServiceImpl service = new LocalBankCardsServiceImpl(config, mockCaller);

        Assertions.assertThrows(
            ly.plutu.sdk.exception.InvalidSecretKeyException.class,
            () -> service.confirm(BigDecimal.TEN, "INV-1", "http://x", null, null)
        );
    }
}
