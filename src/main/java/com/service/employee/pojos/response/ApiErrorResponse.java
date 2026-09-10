package com.service.employee.pojos.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String error;          // short reason phrase e.g. "Bad Request"
    private String message;        // human-readable summary
    private String path;
    private String traceId;        // correlation id for support/log tracing
    private Map<String, String> fieldErrors;  // only for validation failures
}