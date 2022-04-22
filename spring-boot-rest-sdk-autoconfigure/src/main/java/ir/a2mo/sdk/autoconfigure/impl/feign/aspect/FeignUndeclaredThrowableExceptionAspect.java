package ir.a2mo.sdk.autoconfigure.impl.feign.aspect;

import ir.a2mo.sdk.autoconfigure.impl.feign.exception.FeignClientRequestExecuteException;
import feign.FeignException;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;

import java.lang.reflect.UndeclaredThrowableException;

/**
 * @author Ali Alimohammadi
 * @since 4/18/2021
 */
@Aspect
public class FeignUndeclaredThrowableExceptionAspect {

    @AfterThrowing(pointcut = "@within(ir.a2mo.sdk.autoconfigure.impl.feign.annotation.SdkController) ||" +
            " @annotation(ir.a2mo.sdk.autoconfigure.impl.feign.annotation.SdkController)",
            throwing = "exception")
    public void processCoreException(Exception exception) throws Throwable {
        if (exception instanceof UndeclaredThrowableException) {
            throw ((UndeclaredThrowableException) exception).getUndeclaredThrowable();
        } else if (exception instanceof FeignException) {
            throw new FeignClientRequestExecuteException(exception.getMessage(), exception);
        } else {
            throw exception;
        }
    }
}
