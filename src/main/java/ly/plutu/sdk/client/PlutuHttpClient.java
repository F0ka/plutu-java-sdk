package ly.plutu.sdk.client;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface PlutuHttpClient {

    /**
     * Perform a synchronous POST with form parameters.
     * Returns raw response body as string.
     */
    PlutuRawResponse postForm(String url, Map<String, String> formParams, Map<String, String> headers) throws Exception;

    /**
     * Async variant.
     */
    CompletableFuture<PlutuRawResponse> postFormAsync(String url, Map<String, String> formParams, Map<String, String> headers);

}
