package ir.a2mo.sdk.sample.server.api.controller;

import ir.a2mo.sdk.sample.server.api.exception.InvalidParameterException;
import ir.a2mo.sdk.sample.server.api.exception.RequiredParameterException;
import ir.a2mo.sdk.sample.server.api.model.GetInfoRequestDto;
import ir.a2mo.sdk.sample.server.api.model.GetInfoResponseDto;
import ir.a2mo.sdk.autoconfigure.impl.feign.annotation.SdkController;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Ali Alimohammadi
 * @since 4/18/2021
 */
@SdkController
@FeignClient(contextId = "customServer-info")
@RequestMapping("/custom-server")
public interface CustomServerRestController {

    @PostMapping(value = "/info",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    GetInfoResponseDto getInfo(
            @RequestBody GetInfoRequestDto request, @RequestHeader HttpHeaders headers) throws InvalidParameterException, RequiredParameterException;
}
