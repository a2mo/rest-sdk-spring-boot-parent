package ir.a2mo.sdk.autoconfigure.impl.feign.annotation;

import java.lang.annotation.*;

/**
 * @author Ali Alimohammadi
 * @since 11/16/2021
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface SdkController {
}
