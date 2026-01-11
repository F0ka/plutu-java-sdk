package ly.plutu.sdk.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import ly.plutu.sdk.exception.PlutuException;
import ly.plutu.sdk.exception.PlutuHttpException;
import ly.plutu.sdk.model.PlutuApiResponse;
import ly.plutu.sdk.model.PlutuError;

import java.io.IOException;

public final class PlutuResponseHandler {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private PlutuResponseHandler() {}

    public static PlutuApiResponse parseOrThrow(PlutuRawResponse raw) throws IOException, PlutuException {
        // Parse the body as PlutuApiResponse
        PlutuApiResponse response;
        try {
            response = MAPPER.readValue(raw.getBody(), PlutuApiResponse.class);
        } catch (IOException e) {
            // If we can't parse the response but it was an error code, throw generic http exception
            if (raw.getStatusCode() >= 400) {
                PlutuError err = new PlutuError();
                err.setStatus(raw.getStatusCode());
                err.setMessage("Failed to parse error response: " + raw.getBody());
                throw new PlutuHttpException(raw.getStatusCode(), err);
            }
            throw e; // rethrow mapping exception for 200s that are invalid
        }

        // Logic from PHP SDK: check status code logic
        // "isSuccessful" means statusCode == 200.
        // If not 200, we consider it an error and throw.
        
        // Sometimes the top-level status property is the truth, sometimes inside error object.
        // We use the parsed object logic.
        
        if (raw.getStatusCode() >= 400 || !response.isSuccessful()) {
            PlutuError err = response.getError();
            if (err == null) {
                // Fallback if error object is missing but status says error
                err = new PlutuError();
                err.setStatus(raw.getStatusCode());
                err.setCode("UNKNOWN_ERROR");
                err.setMessage("Request failed with status " + raw.getStatusCode());
            }
            // Use the status from the raw response if the parsed error status is missing/0
            if (err.getStatus() <= 0) {
                err.setStatus(raw.getStatusCode());
            }

            throw new PlutuHttpException(err.getStatus(), err);
        }

        return response;
    }
}
