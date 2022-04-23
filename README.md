# rest-sdk-spring-boot-starter

This project provides a Spring-Boot Starter that enables the additional configuration of the used Httpclients and
FeignClient.

## Usage

The `rest-sdk-spring-boot-starter` brings most of the required configuration with it, therefore you only need to
add it as a maven dependency and enable the desired Httpclient.

```
<dependency>
  <groupId>ir.a2mo</groupId>
  <artifactId>rest-sdk-spring-boot-starter</artifactId>
  <version>latest-version</version>
</dependency>
```

### Feign

This project consists of two main parts, one of which is feign client.\
All configuration of feign client is described
in [feign-client](https://cloud.spring.io/spring-cloud-openfeign/reference/html/).

While aim to produce SDK, add custom error decoder to mapping error object that be received from service provider to
exception.

Note that the errors returned by a service provider must follow the following standard:

```
{
    "errorType": "validation",
    "errorCode": "InvalidParameterException",
    "message": "invalid user name",
    "errorParam": {
        "username": "1ali$!"
    }
}
```
Also, You can use [ErrorObject](./rest-sdk-spring-boot-autoconfigure/src/main/java/ir/a2mo/sdk/autoconfigure/impl/feign/ErrorObject.java) instead of making object on your own .\
Keep in mind that you need to initialize conversion map.

### Make exceptionMap manually

`key = errorType.errorCode`

`value = ExceptionName.class`

```
private static Map<String, Class<? extends Exception>> exceptionMap = new HashMap<>();
static {
        exceptionMap.put("validation.InvalidParameterException", InvalidParameterException.class);
} 
```

### Add exception packages to scan automatically

```
    public CustomErrorDecoderConfig customErrorDecoderConfig() {
        CustomErrorDecoderConfig customErrorDecoderConfig = new CustomErrorDecoderConfig();
        customErrorDecoderConfig.getScanPackageList().add(CustomServerException.class.getPackageName());
        return customErrorDecoderConfig;
    }
```

If an error key not defined in the map and http status code is 4xx, error decoder returns `UnknownException`.It has
another parameter besides standard parameters called jsonResponse that is response body of the error.

This conversion just do for http status that are equals or greater than 400 and less than 500. It
returns `InternalServerErrorException` for other http status error code.

## Configuration

All configuration values are prefixed by `sdk.client` (e.g. `sdk.client.timeouts.connectionTimeout`).

It's possible to configure the proxy based on the requested hostnames using the `hostPattern` config.

| Config | Description | Default | Example                                | 
|---|---|---|----------------------------------------|
| baseServiceUrl | Base service url (required) | | `localhost:8080/example`               |
| sslContext | SSL Version (optional) | `TLSv1.2` | `TLSv1.1`                              |
| timeouts.connectionTimeout (optional) | Connection Timeout in ms | 5000 |                                        |
| timeouts.socketTimeout (optional) |  Socket Timeout in ms | 10000 |                                        |
| proxies[] (optional) | Configuration for used proxy servers | |                                        |
| proxies[].hostPatterns | Pattern for matching the hostname, empty matches all  | | `a2mo.*`                               |
| proxies[].proxyHost | Hostname or IP of the Proxy | | `192.168.7.130` or `corp-proxy.domain` |
| proxies[].proxyPort | Port of the Proxy (optional) | |                                        |
| proxies[].proxyUser | Proxy user name (optional) | | `testUser`                             |
| proxies[].proxyPassword | Proxy password (optional) | | `testPassword`                         |
| keystore.path | Keystore file path | | `classpath:keystore.jks`               |
| keystore.password | Keystore password | | `changeit`                             |
| keystore.type | Keystore type (optional) | `JKS` | `PKCS12`                               |
| truststore.path | Truststore file path | | `classpath:truststore.jks`             |
| truststore.password | Truststore password | | `changeit`                             |
| truststore.type | Truststore type (optional) | `JKS` | `PKCS12`                               |

Example:

```
sdk:
  client:
    proxies:
      - hostPatterns: ["a2mo.ir]
        proxyHost: localhost
        proxyPort: 3333
        proxyUser: testUser
        proxyPassword: testPassword

    timeouts:
      connectionTimeout: 5000
      socketTimeout: 10000
```

If you need more than one client with different setting or custom prefix for setting, You can define classes that
inherit HttpClientProperties class.

```
public class CustomClientConfig extends HttpClientProperties {
}
```

```
@Bean
@ConfigurationProperties(prefix = "custom.client")
public CustomClientConfig customClientConfig() {
    return new CustomClientConfig();
}
```

## Sample Project

You can find a sample project which configure `Feign` to use `Apache HttpClient`
in rest-sdk-spring-boot-sample

