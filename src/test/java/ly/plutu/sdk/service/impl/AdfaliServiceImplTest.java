package ly.plutu.sdk.service.impl;

import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.service.response.PlutuAdfaliResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

class AdfaliServiceImplTest {

    private AdfaliServiceImpl adfaliService;
    private BiFunction<String, Map<String, String>, PlutuRawResponse> mockCaller;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        PlutuConfig config = new PlutuConfig();
        config.setApiKey("test-key");
        config.setAccessToken("test-token");
        
        mockCaller = (BiFunction<String, Map<String, String>, PlutuRawResponse>) Mockito.mock(BiFunction.class);
        adfaliService = new AdfaliServiceImpl(config, mockCaller);
    }

    @Test
    void testVerifySuccess() throws Exception {
        // Mock successful JSON response
        String jsonResponse = "{"
                + "\"status\": 200,"
                + "\"result\": { \"process_id\": \"998877\" }"
                + "}";
        PlutuRawResponse raw = new PlutuRawResponse(200, jsonResponse);
        
        Mockito.when(mockCaller.apply(any(), any())).thenReturn(raw);

        PlutuAdfaliResponse response = adfaliService.verify("0912345678", BigDecimal.TEN);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("998877", response.getProcessId());
    }

    @Test
    void testVerifyError() {
        // Mock error JSON response
        String jsonError = "{"
                + "\"status\": 400,"
                + "\"error\": { \"code\": \"INVALID_MOBILE\", \"message\": \"Bad mobile\" }"
                + "}";
        PlutuRawResponse raw = new PlutuRawResponse(400, jsonError);
        
        Mockito.when(mockCaller.apply(any(), any())).thenReturn(raw);

        // Expect exception
        ly.plutu.sdk.exception.PlutuHttpException ex = Assertions.assertThrows(
            ly.plutu.sdk.exception.PlutuHttpException.class, 
            () -> adfaliService.verify("0912345678", BigDecimal.TEN)
        );
        
        Assertions.assertEquals(400, ex.getStatusCode());
        Assertions.assertEquals("INVALID_MOBILE", ex.getError().getCode());
    }
}
