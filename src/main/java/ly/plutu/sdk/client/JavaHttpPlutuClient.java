package ly.plutu.sdk.client;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.CompletableFuture;

/**
 * Minimal HTTP client implementation using Java 11 HttpClient.
 * Sends `application/x-www-form-urlencoded` bodies to match the PHP SDK.
 */
public class JavaHttpPlutuClient implements PlutuHttpClient {

    private final HttpClient httpClient;
    private final Duration timeout;

    public JavaHttpPlutuClient(Duration timeout) {
        this.timeout = timeout;
        this.httpClient = HttpClient.newBuilder().connectTimeout(timeout).build();
    }

    @Override
    public PlutuRawResponse postForm(String url, Map<String, String> formParams, Map<String, String> headers) throws IOException, InterruptedException {
        String encoded = encodeForm(formParams);
        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(encoded));

        if (headers != null) {
            headers.forEach(b::header);
        }

        HttpRequest req = b.build();
        HttpResponse<String> resp = httpClient.send(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
        return new PlutuRawResponse(resp.statusCode(), resp.body());
    }

    @Override
    public CompletableFuture<PlutuRawResponse> postFormAsync(String url, Map<String, String> formParams, Map<String, String> headers) {
        String encoded = encodeForm(formParams);
        HttpRequest.Builder b = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(timeout)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(encoded));

        if (headers != null) {
            headers.forEach(b::header);
        }

        HttpRequest req = b.build();
        return httpClient.sendAsync(req, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8))
                .thenApply(r -> new PlutuRawResponse(r.statusCode(), r.body()));
    }

    private String encodeForm(Map<String, String> form) {
        if (form == null || form.isEmpty()) return "";
        StringJoiner joiner = new StringJoiner("&");
        form.forEach((k, v) -> {
            if (v == null) v = "";
            joiner.add(URLEncoder.encode(k, StandardCharsets.UTF_8) + "=" + URLEncoder.encode(v, StandardCharsets.UTF_8));
        });
        return joiner.toString();
    }
}
