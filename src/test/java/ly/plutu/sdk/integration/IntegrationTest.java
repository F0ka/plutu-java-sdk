package ly.plutu.sdk.integration;

import ly.plutu.sdk.PlutuClient;
import ly.plutu.sdk.PlutuClientImpl;
import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.client.JavaHttpPlutuClient;
import ly.plutu.sdk.service.response.PlutuAdfaliResponse;
import ly.plutu.sdk.service.response.PlutuLocalBankCardsResponse;
import ly.plutu.sdk.service.response.PlutuMpgsResponse;
import ly.plutu.sdk.service.response.PlutuSadadResponse;
import ly.plutu.sdk.service.response.PlutuTlyncResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.time.Duration;
import java.util.Properties;
import java.util.UUID;

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

    @Test
    void testSadadVerify() {
        if (plutu == null) return;

        try {
            System.out.println("Running Sadad Verify Integration Test...");
            // Using test mobile and birth year from documentation
            PlutuSadadResponse response = plutu.sadad().verify("0913632323", 1999, BigDecimal.valueOf(5.0));

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

    @Test
    void testLocalBankCardsConfirm() {
        if (plutu == null) return;

        try {
            System.out.println("Running Local Bank Cards Confirm Integration Test...");
            String invoiceNo = "INV-" + UUID.randomUUID().toString();
            
            PlutuLocalBankCardsResponse response = plutu.localBankCards().confirm(
                    BigDecimal.valueOf(10.0),
                    invoiceNo,
                    "https://example.com/return",
                    "127.0.0.1",
                    "ar"
            );

            System.out.println("Response Status: " + response.getOriginalResponse().getStatus());
            if (response.getOriginalResponse().getError() != null) {
                System.out.println("Error Code: " + response.getOriginalResponse().getError().getCode());
                System.out.println("Error Message: " + response.getOriginalResponse().getError().getMessage());
            }

            Assertions.assertTrue(response.getOriginalResponse().isSuccessful(), "API call failed");
            Assertions.assertNotNull(response.getRedirectUrl(), "Redirect URL should not be null");
            System.out.println("Success! Redirect URL: " + response.getRedirectUrl());

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Integration test failed with exception: " + e.getMessage());
        }
    }

    @Test
    void testMpgsConfirm() {
        if (plutu == null) return;

        try {
            System.out.println("Running MPGS Confirm Integration Test...");
            String invoiceNo = "INV-" + UUID.randomUUID().toString();

            PlutuMpgsResponse response = plutu.mpgs().confirm(
                    BigDecimal.valueOf(10.0),
                    invoiceNo,
                    "https://example.com/return",
                    "127.0.0.1",
                    "ar"
            );

            System.out.println("Response Status: " + response.getOriginalResponse().getStatus());
            if (response.getOriginalResponse().getError() != null) {
                System.out.println("Error Code: " + response.getOriginalResponse().getError().getCode());
                System.out.println("Error Message: " + response.getOriginalResponse().getError().getMessage());
            }

            Assertions.assertTrue(response.getOriginalResponse().isSuccessful(), "API call failed");
            Assertions.assertNotNull(response.getRedirectUrl(), "Redirect URL should not be null");
            System.out.println("Success! Redirect URL: " + response.getRedirectUrl());

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Integration test failed with exception: " + e.getMessage());
        }
    }

    @Test
    void testTlyncConfirm() {
        if (plutu == null) return;

        try {
            System.out.println("Running T-Lync Confirm Integration Test...");
            String invoiceNo = "INV-" + UUID.randomUUID().toString();

            PlutuTlyncResponse response = plutu.tlync().confirm(
                    "0913632323", // Using test mobile number
                    BigDecimal.valueOf(10.0),
                    invoiceNo,
                    "https://example.com/return",
                    "https://example.com/callback",
                    "127.0.0.1",
                    "ar"
            );

            System.out.println("Response Status: " + response.getOriginalResponse().getStatus());
            if (response.getOriginalResponse().getError() != null) {
                System.out.println("Error Code: " + response.getOriginalResponse().getError().getCode());
                System.out.println("Error Message: " + response.getOriginalResponse().getError().getMessage());
            }

            Assertions.assertTrue(response.getOriginalResponse().isSuccessful(), "API call failed");
            Assertions.assertNotNull(response.getRedirectUrl(), "Redirect URL should not be null");
            System.out.println("Success! Redirect URL: " + response.getRedirectUrl());

        } catch (Exception e) {
            e.printStackTrace();
            Assertions.fail("Integration test failed with exception: " + e.getMessage());
        }
    }
}
