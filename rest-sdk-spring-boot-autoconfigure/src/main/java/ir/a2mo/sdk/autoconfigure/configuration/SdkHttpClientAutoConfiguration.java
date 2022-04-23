package ir.a2mo.sdk.autoconfigure.configuration;

import ir.a2mo.sdk.autoconfigure.HttpClientProperties;
import ir.a2mo.sdk.autoconfigure.impl.ConfigurableApacheHttpClientFactory;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.commons.httpclient.DefaultApacheHttpClientConnectionManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

/**
 * AutoConfiguration for the HttpClient
 *
 * @author Ali Alimohammadi
 * @since 1/22/2021
 */
@Configuration
public class SdkHttpClientAutoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "sdk.client")
    @ConditionalOnMissingBean(type = "HttpClientProperties")
    public HttpClientProperties httpClientProperties() {
        return new HttpClientProperties();
    }


    @Bean
    @ConditionalOnMissingBean(type = "ApacheHttpClientFactory")
    public ApacheHttpClientFactory apacheHttpClientFactory(HttpClientBuilder apacheHttpClientBuilder,
                                                           ApacheHttpClientConnectionManagerFactory connectionManagerFactory,
                                                           HttpClientProperties httpClientProperties) {
        return new ConfigurableApacheHttpClientFactory(apacheHttpClientBuilder, connectionManagerFactory, httpClientProperties);
    }

    @Bean
    @ConditionalOnMissingBean(type = "ClientHttpRequestFactory")
    public ClientHttpRequestFactory clientHttpRequestFactory(ApacheHttpClientFactory apacheHttpClientFactory) {
        return new HttpComponentsClientHttpRequestFactory(apacheHttpClientFactory.createBuilder().build());
    }

    @Bean
    @ConditionalOnMissingBean(type = "HttpClientBuilder")
    public HttpClientBuilder apacheHttpClientBuilder() {
        return HttpClientBuilder.create();
    }

    @Bean
    @ConditionalOnMissingBean(type = "ApacheHttpClientConnectionManagerFactory")
    public ApacheHttpClientConnectionManagerFactory connectionManagerFactory() {
        return new DefaultApacheHttpClientConnectionManagerFactory();
    }
}
