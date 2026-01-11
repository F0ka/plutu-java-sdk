package ly.plutu.sdk.service.impl;

import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.service.response.PlutuTlyncResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

import static org.mockito.ArgumentMatchers.any;

class TlyncServiceImplTest {

    private TlyncServiceImpl tlyncService;
    private BiFunction<String, Map<String, String>, PlutuRawResponse> mockCaller;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        PlutuConfig config = new PlutuConfig();
        config.setApiKey("test-key");
        config.setAccessToken("test-token");
        config.setSecretKey("test-secret");
        
        mockCaller = (BiFunction<String, Map<String, String>, PlutuRawResponse>) Mockito.mock(BiFunction.class);
        tlyncService = new TlyncServiceImpl(config, mockCaller);
    }

    @Test
    void testConfirmSuccess() throws Exception {
        String jsonResponse = "{"
                + "\"status\": 200,"
                + "\"result\": { \"redirect_url\": \"https://tlync.example.com/pay\" }"
                + "}";
        PlutuRawResponse raw = new PlutuRawResponse(200, jsonResponse);
        
        Mockito.when(mockCaller.apply(any(), any())).thenReturn(raw);

        PlutuTlyncResponse response = tlyncService.confirm(
            "0912345678",
            BigDecimal.valueOf(15.0), 
            "INV-TLYNC-001", 
            "https://example.com/return", 
            "https://example.com/callback",
            null, 
            "ar"
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals("https://tlync.example.com/pay", response.getRedirectUrl());
    }
}
