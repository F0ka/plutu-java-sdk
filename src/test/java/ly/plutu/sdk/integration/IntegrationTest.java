package ly.plutu.sdk.integration;

import ly.plutu.sdk.PlutuClient;
import ly.plutu.sdk.PlutuClientImpl;
import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.JavaHttpPlutuClient;
import ly.plutu.sdk.service.response.PlutuAdfaliResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Properties;

public class IntegrationTest {

    private static PlutuClient plutu;

    @BeforeAll
    static void setup() throws Exception {
        // Disable hostname verification for the test
        System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");

        Properties env = new Properties();
        try (FileInputStream fis = new FileInputStream(".env")) {
            env.load(fis);
        } catch (Exception e) {
            System.err.println("Skipping integration tests: .env file not found.");
            return;
        }

        PlutuConfig config = new PlutuConfig();
        config.setApiKey(env.getProperty("PLUTU_API_KEY"));
        config.setAccessToken(env.getProperty("PLUTU_ACCESS_TOKEN"));
        config.setSecretKey(env.getProperty("PLUTU_SECRET_KEY"));
        // Use production URL which appears to handle test credentials correctly
        // config.setBaseUrl("https://api.plutus.ly/api"); 

        // Use standard client (Production SSL should be valid)
        JavaHttpPlutuClient httpClient = new JavaHttpPlutuClient(Duration.ofSeconds(60));
        plutu = new PlutuClientImpl(config, httpClient);
    }

    @Test
    void testAdfaliVerify() {
        if (plutu == null) return; // Skip if config failed

        try {
            System.out.println("Running Adfali Verify Integration Test...");
            // Using a valid test mobile number from documentation
            PlutuAdfaliResponse response = plutu.adfali().verify("0913632323", BigDecimal.valueOf(5.0));
            
            System.out.println("Response Status: " + response.getOriginalResponse().getStatus());
            if (response.getOriginalResponse().getError() != null) {
                System.out.println("Error Code: " + response.getOriginalResponse().getError().getCode());
                System.out.println("Error Message: " + response.getOriginalResponse().getError().getMessage());
            }

            Assertions.assertTrue(response.isSuccessful(), "API call failed");
            Assertions.assertNotNull(response.getProcessId(), "Process ID should not be null");
            System.out.println("Success! Process ID: " + response.getProcessId());

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Integration test failed with exception: " + e.getMessage());
        }
    }
}
