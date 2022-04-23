package ir.a2mo.sdk.autoconfigure.configuration;

import ir.a2mo.sdk.autoconfigure.impl.feign.CustomErrorDecoder;
import ir.a2mo.sdk.autoconfigure.impl.feign.CustomErrorDecoderConfig;
import ir.a2mo.sdk.autoconfigure.impl.feign.ExceptionExtractType;
import ir.a2mo.sdk.autoconfigure.impl.feign.aspect.FeignUndeclaredThrowableExceptionAspect;
import ir.a2mo.sdk.autoconfigure.impl.feign.exception.SdkWebServiceException;
import ir.a2mo.sdk.autoconfigure.impl.feign.exception.SdkWebServiceRuntimeException;
import feign.Feign;
import feign.codec.ErrorDecoder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AutoConfiguration for the Feign
 *
 * @author Ali Alimohammadi
 * @since 1/22/2021
 */
@Configuration
@ConditionalOnClass({Feign.class})
public class SdkFeignAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CustomErrorDecoderConfig customErrorDecoderConfig() {
        CustomErrorDecoderConfig customErrorDecoderConfig = new CustomErrorDecoderConfig();
        customErrorDecoderConfig.addPackages("ir.a2mo");
        customErrorDecoderConfig.setExceptionExtractType(ExceptionExtractType.FULL_NAME_REFLECTION);
        customErrorDecoderConfig.setCheckedExceptionClass(SdkWebServiceException.class);
        customErrorDecoderConfig.setUncheckedExceptionClass(SdkWebServiceRuntimeException.class);
        return customErrorDecoderConfig;
    }

    @Bean
    FeignUndeclaredThrowableExceptionAspect undeclaredThrowableExceptionAspect() {
        return new FeignUndeclaredThrowableExceptionAspect();
    }

    @Bean
    @ConditionalOnMissingBean
    public ErrorDecoder customErrorDecoder(CustomErrorDecoderConfig customErrorDecoderConfig) {
        return new CustomErrorDecoder(customErrorDecoderConfig);
    }
}
