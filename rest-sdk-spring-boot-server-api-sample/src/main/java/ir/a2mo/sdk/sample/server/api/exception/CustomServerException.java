package ir.a2mo.sdk.sample.server.api.exception;

import ir.a2mo.sdk.autoconfigure.impl.feign.exception.SdkWebServiceException;
import lombok.NoArgsConstructor;

/**
 * @author Ali Alimohammadi
 * @since 4/18/2021
 */
@NoArgsConstructor
public abstract class CustomServerException extends SdkWebServiceException {
    private static final long serialVersionUID = 6543428451282914405L;

    public CustomServerException(String message) {
        super(message);
    }

    public CustomServerException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getErrorCode() {
        return this.getClass().getSimpleName();
    }
}
