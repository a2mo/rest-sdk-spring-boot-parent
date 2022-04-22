package ir.a2mo.sdk.sample.server.rest.controller;

import ir.a2mo.sdk.sample.server.api.controller.CustomServerRestController;
import ir.a2mo.sdk.sample.server.api.exception.InvalidParameterException;
import ir.a2mo.sdk.sample.server.api.exception.RequiredParameterException;
import ir.a2mo.sdk.sample.server.api.model.GetInfoRequestDto;
import ir.a2mo.sdk.sample.server.api.model.GetInfoResponseDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ali Alimohammadi
 * @since 4/18/2021
 */
@RestController
@SuppressWarnings("ResultOfMethodCallIgnored")
public class CustomServerRestControllerImpl implements CustomServerRestController {

    @Override
    @ResponseStatus(HttpStatus.OK)
    public GetInfoResponseDto getInfo(GetInfoRequestDto request, HttpHeaders headers) throws InvalidParameterException, RequiredParameterException {
        if (request.getSsn() != null) {
            if (request.getSsn().isEmpty()) {
                RequiredParameterException requiredParameterException = new RequiredParameterException("ssn is empty");
                requiredParameterException.addErrorParam("ssn", "notEmpty");
                throw requiredParameterException;
            }
            Integer.parseInt(request.getSsn());
            GetInfoResponseDto getInfoResponseDto = new GetInfoResponseDto();
            getInfoResponseDto.setFirstName("ali");
            getInfoResponseDto.setLastName("alimohammadi");
            return getInfoResponseDto;
        } else {
            InvalidParameterException invalidParameterException = new InvalidParameterException("ssn is null");
            invalidParameterException.addErrorParam("ssn", "notNull");
            throw invalidParameterException;
        }
    }
}
