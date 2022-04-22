package ir.a2mo.sdk.sample.server.rest;

import ir.a2mo.sdk.sample.server.api.config.feign.CustomServerFeignConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Ali Alimohammadi
 * @since 4/18/2021
 */
@SpringBootApplication(scanBasePackages = "ir.a2mo.sdk.sample.server.rest", exclude = CustomServerFeignConfig.class)
public class RestServerSpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(RestServerSpringBootApplication.class, args);
    }
}
