package ir.a2mo.sdk.autoconfigure;

import ir.a2mo.sdk.autoconfigure.impl.ConfigurableApacheHttpClientFactory;
import ir.a2mo.sdk.autoconfigure.configuration.SdkHttpClientAutoConfiguration;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.commons.httpclient.HttpClientConfiguration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import static org.assertj.core.api.Assertions.assertThat;

public class SdkHttpClientAutoConfigurationTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withBean(HttpClientConfiguration.class)
            .withBean(HttpClientBuilder.class);

    @Test
    public void apacheHttpClient() {
        this.contextRunner.withUserConfiguration(SdkHttpClientAutoConfiguration.class)
                .run(ctx -> {
                    assertThat(ctx).hasSingleBean(ApacheHttpClientFactory.class);
                    assertThat(ctx).hasSingleBean(ClientHttpRequestFactory.class);
                    assertThat(ctx.getBean(ApacheHttpClientFactory.class))
                            .isInstanceOf(ConfigurableApacheHttpClientFactory.class);
                    assertThat(ctx.getBean(ClientHttpRequestFactory.class))
                            .isInstanceOf(HttpComponentsClientHttpRequestFactory.class);
                });
    }
}
