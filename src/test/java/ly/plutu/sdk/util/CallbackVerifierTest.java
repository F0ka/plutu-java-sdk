package ly.plutu.sdk.util;

import ly.plutu.sdk.exception.InvalidCallbackHashException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class CallbackVerifierTest {

    @Test
    void testVerifyValidHash() {
        // Mock data
        String secretKey = "mySecretKey";
        Map<String, String> params = new HashMap<>();
        params.put("amount", "10.00");
        params.put("currency", "LYD");
        params.put("transaction_id", "12345");
        
        // Manual HMAC-SHA256 calculation for "amount=10.00&currency=LYD&transaction_id=12345" using "mySecretKey"
        // Expected hash (computed externally or via known correct impl): 
        // Hash: DA57B229A482033D5CD877240D50C892EC0BB490B66981AE7FC2AB2310D55A50
        String expectedHash = "DA57B229A482033D5CD877240D50C892EC0BB490B66981AE7FC2AB2310D55A50"; 
        
        params.put("hashed", expectedHash);
        
        List<String> keys = List.of("amount", "currency", "transaction_id");

        Assertions.assertDoesNotThrow(() -> CallbackVerifier.verify(params, keys, secretKey));
    }

    @Test
    void testVerifyInvalidHash() {
        String secretKey = "mySecretKey";
        Map<String, String> params = new HashMap<>();
        params.put("amount", "10.00");
        params.put("hashed", "INVALID_HASH");

        List<String> keys = List.of("amount");

        Assertions.assertThrows(InvalidCallbackHashException.class, () -> 
            CallbackVerifier.verify(params, keys, secretKey)
        );
    }
    
    @Test
    void testVerifyMissingHashedParam() {
        String secretKey = "mySecretKey";
        Map<String, String> params = new HashMap<>();
        params.put("amount", "10.00");

        List<String> keys = List.of("amount");

        Assertions.assertThrows(InvalidCallbackHashException.class, () -> 
            CallbackVerifier.verify(params, keys, secretKey)
        );
    }
}
