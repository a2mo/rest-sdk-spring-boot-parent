package ir.a2mo.sdk.autoconfigure.impl.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.a2mo.sdk.autoconfigure.impl.feign.exception.SdkWebServiceException;
import ir.a2mo.sdk.autoconfigure.impl.feign.exception.SdkWebServiceRuntimeException;

import java.util.*;

/**
 * @author Ali Alimohammadi
 * @since 5/10/2021
 */
public class CustomErrorDecoderConfig {
    private ExceptionExtractType exceptionExtractType;
    private Map<String, Class<? extends Exception>> exceptionMap = new HashMap<>();
    private Class<? extends SdkWebServiceException> checkedExceptionClass;
    private Class<? extends SdkWebServiceRuntimeException> uncheckedExceptionClass;
    private List<String> scanPackageList = new ArrayList<>();
    private ObjectMapper objectMapper;

    public ExceptionExtractType getExceptionExtractType() {
        return exceptionExtractType;
    }

    public void setExceptionExtractType(ExceptionExtractType exceptionExtractType) {
        this.exceptionExtractType = exceptionExtractType;
    }

    public Map<String, Class<? extends Exception>> getExceptionMap() {
        return exceptionMap;
    }

    public void setExceptionMap(Map<String, Class<? extends Exception>> exceptionMap) {
        this.exceptionMap = exceptionMap;
    }

    public Class<? extends SdkWebServiceException> getCheckedExceptionClass() {
        return checkedExceptionClass;
    }

    public void setCheckedExceptionClass(Class<? extends SdkWebServiceException> checkedExceptionClass) {
        this.checkedExceptionClass = checkedExceptionClass;
    }

    public Class<? extends SdkWebServiceRuntimeException> getUncheckedExceptionClass() {
        return uncheckedExceptionClass;
    }

    public void setUncheckedExceptionClass(Class<? extends SdkWebServiceRuntimeException> uncheckedExceptionClass) {
        this.uncheckedExceptionClass = uncheckedExceptionClass;
    }

    public List<String> getScanPackageList() {
        return scanPackageList;
    }

    public CustomErrorDecoderConfig addPackage(String packageName) {
        if (this.scanPackageList == null) {
            this.scanPackageList = new ArrayList<>();
        }
        this.scanPackageList.add(packageName);
        return this;
    }

    public CustomErrorDecoderConfig addPackages(String... packageNames) {
        if (this.scanPackageList == null) {
            this.scanPackageList = new ArrayList<>();
        }
        this.scanPackageList.addAll(Arrays.asList(packageNames));
        return this;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
}
