package ir.a2mo.sdk.autoconfigure;

import ir.a2mo.sdk.autoconfigure.configuration.SdkFeignAutoConfiguration;
import ir.a2mo.sdk.autoconfigure.impl.feign.CustomErrorDecoder;
import feign.Feign;
import feign.codec.ErrorDecoder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.FilteredClassLoader;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

public class SdkFeignAutoConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner();

    @Test
    public void customErrorDecoder() {
        this.contextRunner.withUserConfiguration(SdkFeignAutoConfiguration.class)
                .run(ctx -> {
                    assertThat(ctx).hasSingleBean(CustomErrorDecoder.class);
                    assertThat(ctx.getBean(CustomErrorDecoder.class)).isInstanceOf(ErrorDecoder.class);
                });
    }

    @Test
    public void customErrorDecoder_missingClass() {
        this.contextRunner.withUserConfiguration(SdkFeignAutoConfiguration.class)
                .withClassLoader(new FilteredClassLoader(Feign.class))
                .run(ctx -> assertThat(ctx).doesNotHaveBean(CustomErrorDecoder.class));
    }
}
