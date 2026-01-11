# Unofficial Plutu Java SDK

A robust, strongly-typed, and thread-safe Java SDK for the [Plutu](https://plutu.ly) payment gateway.

**⚠️ DISCLAIMER: This is an UNOFFICIAL library. It is not created, maintained, or endorsed by Plutu. Use it at your own risk.**

## Features

- **Strongly Typed**: Request and response objects for all services (Adfali, Sadad, Local Bank Cards, T-Lync, MPGS).
- **Validation**: Client-side validation mirrors the official PHP SDK to catch errors early (regex for mobile numbers, amount checks, etc.).
- **Async & Sync**: Built on Java 11 `HttpClient`, offering both blocking and `CompletableFuture` based async APIs.
- **Secure**: Implements HMAC-SHA256 callback verification for secure payment confirmation.
- **Robust Error Handling**: Maps API error codes to typed `PlutuHttpException` for easy handling.

## Requirements

- Java 11 or higher.

## Installation

**Using Maven**
Add the dependency to your `pom.xml`:

```xml
<dependency>
    <groupId>io.github.f0ka</groupId>
    <artifactId>plutu-java-sdk</artifactId>
    <version>0.1.0</version>
</dependency>
```

**Using Gradle**
Add the dependency to your `build.gradle`:

```gradle
implementation 'io.github.f0ka:plutu-java-sdk:0.1.0'
```

## Usage

### 1. Configuration

Create a configuration instance with your credentials.

```java
import ly.plutu.sdk.PlutuConfig;
import ly.plutu.sdk.PlutuClient;
import ly.plutu.sdk.PlutuClientImpl;
import ly.plutu.sdk.client.JavaHttpPlutuClient;

import java.time.Duration;

// Setup config
PlutuConfig config = new PlutuConfig();
config.setApiKey("YOUR_API_KEY");
config.setAccessToken("YOUR_ACCESS_TOKEN");
config.setSecretKey("YOUR_SECRET_KEY"); // Required for callbacks & some services

// Create client
JavaHttpPlutuClient httpClient = new JavaHttpPlutuClient(Duration.ofSeconds(60));
PlutuClient plutu = new PlutuClientImpl(config, httpClient);
```

### 2. Adfali Example

**Verify (Send OTP)**

```java
import java.math.BigDecimal;

try {
    var response = plutu.adfali().verify("0912345678", BigDecimal.valueOf(5.0));
    String processId = response.getProcessId();
    System.out.println("OTP sent. Process ID: " + processId);
} catch (Exception e) {
    e.printStackTrace();
}
```

**Confirm (Complete Payment)**

```java
try {
    var response = plutu.adfali().confirm(
        "PROCESS_ID", 
        "1234", // OTP Code
        BigDecimal.valueOf(5.0), 
        "INV-001", 
        "127.0.0.1" // Optional Customer IP
    );
    System.out.println("Paid! Transaction ID: " + response.getTransactionId());
} catch (PlutuHttpException e) {
    System.err.println("API Error: " + e.getError().getMessage());
}
```

### 3. Local Bank Cards Example

```java
var response = plutu.localBankCards().confirm(
    BigDecimal.valueOf(10.0), 
    "INV-002", 
    "https://your-site.com/return", 
    null, 
    "ar"
);

String redirectUrl = response.getRedirectUrl();
// Redirect user to: redirectUrl
```

### 4. Handling Callbacks

Securely verify the callback parameters from Plutu.

```java
import java.util.Map;

// Assuming you have the query parameters in a Map<String, String>
Map<String, String> params = request.getParameterMap(); // ... convert to Map<String, String>

try {
    var callback = plutu.localBankCards().handleCallback(params);
    
    if (callback.isApprovedTransaction()) {
        System.out.println("Payment Approved!");
        System.out.println("Invoice: " + callback.getParameter("invoice_no"));
    }
} catch (InvalidCallbackHashException e) {
    System.err.println("Security Alert: Callback hash mismatch!");
}
```

## Services Supported

- **Adfali** (Verify & Confirm)
- **Sadad** (Verify & Confirm)
- **Local Bank Cards** (Confirm & Callback)
- **T-Lync** (Confirm, Callback & Return)
- **MPGS** (Confirm & Callback)

## License

MIT License. See [LICENSE](LICENSE) file.
