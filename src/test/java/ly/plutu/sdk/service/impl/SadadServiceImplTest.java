package ly.plutu.sdk.service.impl;

import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.service.response.PlutuSadadResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Map;
import java.util.function.BiFunction;

import static org.mockito.ArgumentMatchers.any;

class SadadServiceImplTest {

    private SadadServiceImpl sadadService;
    private BiFunction<String, Map<String, String>, PlutuRawResponse> mockCaller;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        PlutuConfig config = new PlutuConfig();
        config.setApiKey("test-key");
        config.setAccessToken("test-token");
        
        mockCaller = (BiFunction<String, Map<String, String>, PlutuRawResponse>) Mockito.mock(BiFunction.class);
        sadadService = new SadadServiceImpl(config, mockCaller);
    }

    @Test
    void testVerifySuccess() throws Exception {
        String jsonResponse = "{"
                + "\"status\": 200,"
                + "\"result\": { \"process_id\": \"12345\", \"status\": \"PENDING\" }"
                + "}";
        PlutuRawResponse raw = new PlutuRawResponse(200, jsonResponse);
        
        Mockito.when(mockCaller.apply(any(), any())).thenReturn(raw);

        PlutuSadadResponse response = sadadService.verify("0912345678", 1990, BigDecimal.TEN);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("12345", response.getProcessId());
    }

    @Test
    void testConfirmSuccess() throws Exception {
        String jsonResponse = "{"
                + "\"status\": 200,"
                + "\"result\": { \"process_id\": \"12345\", \"status\": \"COMPLETED\" }"
                + "}";
        PlutuRawResponse raw = new PlutuRawResponse(200, jsonResponse);
        
        Mockito.when(mockCaller.apply(any(), any())).thenReturn(raw);

        PlutuSadadResponse response = sadadService.confirm("12345", "123456", BigDecimal.TEN, "INV-001", "127.0.0.1");

        Assertions.assertNotNull(response);
        Assertions.assertTrue(response.isSuccessful());
    }
}
