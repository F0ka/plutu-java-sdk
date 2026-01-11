package ly.plutu.sdk;

import ly.plutu.sdk.client.PlutuRawResponse;
import ly.plutu.sdk.service.*;

/**
 * Main entry point for the Plutu SDK (Unofficial).
 * <p>
 * This client provides access to all payment services supported by the Plutu Gateway.
 * </p>
 * <strong>Disclaimer:</strong> This is an unofficial library and is not affiliated with, maintained, or endorsed by Plutu.ly.
 */
public interface PlutuClient {

    /**
     * Access Adfali payment services (Verify & Confirm).
     */
    AdfaliService adfali();

    /**
     * Access Sadad payment services (Verify & Confirm).
     */
    SadadService sadad();

    /**
     * Access Local Bank Cards services (Confirm & Callback).
     */
    LocalBankCardsService localBankCards();

    /**
     * Access T-Lync services (Confirm, Return & Callback).
     */
    TlyncService tlync();

    /**
     * Access MPGS (Mastercard Payment Gateway Services) integration.
     */
    MpgsService mpgs();

    /**
     * Low-level helper for sending raw requests to the gateway.
     * Useful for debugging or accessing new endpoints not yet covered by this SDK.
     *
     * @param gateway The service name (e.g., "adfali", "sadad")
     * @param action The action name (e.g., "verify", "confirm")
     * @param params The form parameters to send
     * @return The raw HTTP response (status code and body)
     * @throws Exception if a network error occurs
     */
    PlutuRawResponse postToGateway(String gateway, String action, java.util.Map<String, String> params) throws Exception;

}
