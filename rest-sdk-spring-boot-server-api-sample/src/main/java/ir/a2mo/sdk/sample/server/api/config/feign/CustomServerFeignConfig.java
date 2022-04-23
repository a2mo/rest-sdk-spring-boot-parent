package ir.a2mo.sdk.sample.server.api.config.feign;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ir.a2mo.sdk.sample.server.api.config.properties.CustomServerClientConfig;
import ir.a2mo.sdk.sample.server.api.controller.CustomServerRestController;
import ir.a2mo.sdk.sample.server.api.config.CustomServerClientConfigurationException;
import ir.a2mo.sdk.sample.server.api.exception.CustomServerException;
import ir.a2mo.sdk.autoconfigure.HttpClientProperties;
import ir.a2mo.sdk.autoconfigure.impl.ConfigurableApacheHttpClientFactory;
import ir.a2mo.sdk.autoconfigure.impl.feign.CustomErrorDecoder;
import ir.a2mo.sdk.autoconfigure.impl.feign.CustomErrorDecoderConfig;
import ir.a2mo.sdk.autoconfigure.impl.feign.ExceptionExtractType;
import ir.a2mo.sdk.autoconfigure.impl.feign.exception.SdkWebServiceRuntimeException;
import feign.*;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import feign.httpclient.ApacheHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.commons.httpclient.ApacheHttpClientFactory;
import org.springframework.cloud.commons.httpclient.DefaultApacheHttpClientConnectionManagerFactory;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Ali Alimohammadi
 * @since 4/18/2021
 */
@Configuration
@EnableFeignClients
@Slf4j
public class CustomServerFeignConfig {

    @Bean({"customServer-objectMapper"})
    public ObjectMapper customServerObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return objectMapper;
    }

    @Bean
    @ConfigurationProperties(prefix = "custom-service")
    @ConditionalOnMissingBean
    public CustomServerClientConfig customServerClientConfig() {
        return new CustomServerClientConfig();
    }

    @Bean("customServer-apacheHttpClientFactory")
    public ApacheHttpClientFactory apacheHttpClientFactory(
            @Qualifier("customServer-httpClientBuilder") HttpClientBuilder builder,
            @Qualifier("customerServer-connectionManagerFactory") ApacheHttpClientConnectionManagerFactory clientConnectionManagerFactory,
            CustomServerClientConfig customServerClientConfig) {
        return new ConfigurableApacheHttpClientFactory(builder, clientConnectionManagerFactory, customServerClientConfig);
    }

    @Bean("customServer-clientHttpRequestFactory")
    public ClientHttpRequestFactory clientHttpRequestFactory(
            @Qualifier("customServer-apacheHttpClientFactory") ApacheHttpClientFactory apacheHttpClientFactory) {
        return new HttpComponentsClientHttpRequestFactory(apacheHttpClientFactory.createBuilder().build());
    }

    @Bean("customServer-httpclient")
    public CloseableHttpClient httpClient(
            @Qualifier("customServer-apacheHttpClientFactory") ApacheHttpClientFactory apacheHttpClientFactory) {
        return apacheHttpClientFactory.createBuilder().build();
    }

    @Bean("customerServer-connectionManagerFactory")
    public ApacheHttpClientConnectionManagerFactory connectionManagerFactory() {
        return new DefaultApacheHttpClientConnectionManagerFactory();
    }

    @Bean("customServer-feignClient")
    public Client feignClient(@Qualifier("customServer-httpclient") HttpClient httpClient) {
        return new ApacheHttpClient(httpClient);
    }

    @Bean("customServer-requestInterceptor")
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Accept", ContentType.APPLICATION_JSON.getMimeType());
            requestTemplate.header("Content-Type", ContentType.APPLICATION_JSON.getMimeType());
        };
    }

    @Bean("customServer-requestInterceptors")
    public List<RequestInterceptor> requestInterceptors(
            CustomServerClientConfig customServerClientConfig,
            @Qualifier("customServer-requestInterceptor")
                    RequestInterceptor requestInterceptor) {
        List<RequestInterceptor> requestInterceptors = new ArrayList<>();
        requestInterceptors.add(requestInterceptor);
        HttpClientProperties.AuthorizationConfiguration authorizationConfiguration =
                customServerClientConfig.getAuthorizationConfiguration();
        if (customServerClientConfig.getAuthorizationConfiguration().isEnable()) {
            requestInterceptors.add(new BasicAuthRequestInterceptor(authorizationConfiguration.getUsername(),
                    authorizationConfiguration.getPassword(), StandardCharsets.UTF_8));
        }
        return requestInterceptors;
    }

    @Bean("customServer-feignContract")
    public Contract feignContract() {
        return new SpringMvcContract();
    }

    @Bean("customServer-feignEncoder")
    public Encoder feignEncoder(@Qualifier("customServer-objectMapper")
                                            ObjectMapper objectMapper) {
        return new SpringFormEncoder(new SpringEncoder(getHttpMessageConverter(objectMapper)));
    }

    @Bean("customServer-feignDecoder")
    public Decoder feignDecoder(@Qualifier("customServer-objectMapper")
                                         ObjectMapper objectMapper) {
        return new ResponseEntityDecoder(new SpringDecoder(getHttpMessageConverter(objectMapper)));
    }

    private ObjectFactory<HttpMessageConverters> getHttpMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter =
                new MappingJackson2HttpMessageConverter(objectMapper);
        return () -> new HttpMessageConverters(mappingJackson2HttpMessageConverter);
    }

    @Bean("customServer-feignErrorDecoderConfig")
    public CustomErrorDecoderConfig customErrorDecoderConfig(@Qualifier("customServer-objectMapper") ObjectMapper objectMapper) {
        CustomErrorDecoderConfig customErrorDecoderConfig = new CustomErrorDecoderConfig();
        customErrorDecoderConfig.getScanPackageList().add("ir.a2mo.sdk.sample.server.api.exception");
        customErrorDecoderConfig.setExceptionExtractType(ExceptionExtractType.EXCEPTION_IDENTIFIER_FIELDS);
        customErrorDecoderConfig.setCheckedExceptionClass(CustomServerException.class);
        customErrorDecoderConfig.setUncheckedExceptionClass(SdkWebServiceRuntimeException.class);
        customErrorDecoderConfig.setObjectMapper(objectMapper);
        return customErrorDecoderConfig;
    }

    @Bean("customServer-feignErrorDecoder")
    public CustomErrorDecoder customErrorDecoder(@Qualifier("customServer-feignErrorDecoderConfig") CustomErrorDecoderConfig customErrorDecoderConfig) {
        return new CustomErrorDecoder(customErrorDecoderConfig);
    }

    @Bean("customServer-httpClientBuilder")
    public HttpClientBuilder apacheHttpClientBuilder() {
        return HttpClientBuilder.create();
    }

    @Bean("customServer-retryer")
    @ConditionalOnMissingBean(
            name = {"customServer-retryer"}
    )
    public Retryer retryer() {
        return Retryer.NEVER_RETRY;
    }


    @Bean("customServer-feignLoggerLevel")
    @ConditionalOnMissingBean(
            name = {"customServer-feignLoggerLevel"}
    )
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean("customServer-feignOption")
    Request.Options options(CustomServerClientConfig customServerClientConfig) {
        HttpClientProperties.ConnectionConfiguration connectionConfiguration = customServerClientConfig
                .getConnectionConfiguration();
        return new Request.Options(
                connectionConfiguration.getConnectionTimeout(), TimeUnit.MILLISECONDS,
                connectionConfiguration.getSocketTimeout(), TimeUnit.MILLISECONDS, connectionConfiguration
                .isFollowRedirects());
    }

    @Bean
    public CustomServerRestController clientServerRestController(
            CustomServerClientConfig customServerClientConfig,
            @Qualifier("customServer-feignClient") Client feignClient,
            @Qualifier("customServer-feignOption") Request.Options options,
            @Qualifier("customServer-requestInterceptors") List<RequestInterceptor> requestInterceptors,
            @Qualifier("customServer-feignContract") Contract feignContract,
            @Qualifier("customServer-feignDecoder") Decoder springDecoder,
            @Qualifier("customServer-feignEncoder") Encoder feignFormEncoder,
            @Qualifier("customServer-retryer") Retryer retryer,
            @Qualifier("customServer-feignLoggerLevel") Logger.Level logLevel,
            @Qualifier("customServer-feignErrorDecoder") CustomErrorDecoder customErrorDecoder) {

        if (customServerClientConfig.getBaseServiceUrl() == null) {
            log.error("baseServiceUrl of customServer web service must be set and not be null or empty.");
            throw new CustomServerClientConfigurationException
                    ("baseServiceUrl of customServer web service must be set and not be null or empty.");
        }

        return Feign.builder().client(feignClient)
                .options(options)
                .encoder(feignFormEncoder)
                .decoder(springDecoder)
                .errorDecoder(customErrorDecoder)
                .contract(feignContract)
                .requestInterceptors(requestInterceptors)
                .retryer(retryer)
                .logger(new Slf4jLogger(CustomServerRestController.class))
                .logLevel(logLevel)
                .target(CustomServerRestController.class, customServerClientConfig.getBaseServiceUrl());
    }
}
