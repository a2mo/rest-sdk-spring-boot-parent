package ir.a2mo.sdk.autoconfigure.impl.feign.exception;

import java.util.Map;

/**
 * @author Ali Alimohammadi
 * @since 5/10/2021
 */
public interface SdkWebServiceBaseException {

    String getErrorType();

    String getErrorCode();

    String getMessage();

    Map<String, Object> getErrorParam();

    SdkWebServiceBaseException addErrorParam(String key, Object value);
}
