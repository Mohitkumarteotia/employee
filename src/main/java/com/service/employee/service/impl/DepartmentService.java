package com.service.employee.service.impl;

import com.service.employee.config.DepartmentProperties;
import com.service.employee.exception.custom.DepartmentServiceException;
import com.service.employee.pojos.response.DepartmentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final RestTemplate restTemplate;
    private final DepartmentProperties departmentProperties;

    @CircuitBreaker(name = "departmentService", fallbackMethod = "getDepartmentFallback")
    @Retry(name = "departmentService", fallbackMethod = "getDepartmentFallback")
    public DepartmentResponse getDepartment(Long departmentId) {
        String url = departmentProperties.getBaseUrl() + "/" + departmentId;
        log.info("Fetching department details. departmentId={}", departmentId);
        log.debug("Calling Department Service URL={}", url);
        DepartmentResponse response = restTemplate.getForObject(url, DepartmentResponse.class);
        log.info("Successfully fetched department details. departmentId={}, departmentName={}", departmentId, response != null ? response.getDepartmentName() : null);
        return response;
    }

    public DepartmentResponse getDepartmentFallback(Long departmentId, Exception ex) {
        log.info("Department Service unavailable. departmentId={}, error={}", departmentId, ex.getMessage(), ex);
        throw new DepartmentServiceException("Department Service is currently unavailable. Please try again later.");
    }

}
