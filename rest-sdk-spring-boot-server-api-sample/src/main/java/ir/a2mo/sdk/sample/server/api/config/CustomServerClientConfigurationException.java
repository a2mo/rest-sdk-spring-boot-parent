package ir.a2mo.sdk.sample.server.api.config;

import ir.a2mo.sdk.autoconfigure.impl.feign.exception.FeignConfigurationException;

/**
 * @author Ali Alimohammadi
 * @since 4/18/2021
 */
public class CustomServerClientConfigurationException extends FeignConfigurationException {
    private static final long serialVersionUID = -2443361687977670214L;

    public CustomServerClientConfigurationException(String message) {
        super(message);
    }

    public CustomServerClientConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}
