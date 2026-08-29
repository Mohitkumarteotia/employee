package com.service.employee.service.impl;

import com.service.employee.config.DepartmentProperties;
import com.service.employee.exception.custom.DepartmentServiceException;
import com.service.employee.exception.custom.RateLimitExceededException;
import com.service.employee.pojos.constant.ServiceConstants;
import com.service.employee.pojos.response.DepartmentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
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

    @Retry(name = ServiceConstants.DEPARTMENT_SERVICE, fallbackMethod = "departmentFallback")
    @CircuitBreaker(name = ServiceConstants.DEPARTMENT_SERVICE, fallbackMethod = "departmentFallback")
    @RateLimiter(name = ServiceConstants.DEPARTMENT_SERVICE, fallbackMethod = "rateLimiterFallback")
    public DepartmentResponse getDepartment(Long departmentId) {
        String url = String.format("%s/%s", departmentProperties.getBaseUrl(), departmentId);
        log.info("Fetching department details. departmentId={}", departmentId);
        DepartmentResponse response = restTemplate.getForObject(url, DepartmentResponse.class);
        log.info("Department details fetched successfully. departmentId={}, departmentName={}", departmentId, response.getDepartmentName());
        return response;
    }


    public DepartmentResponse departmentFallback(Long departmentId, Exception ex) {
        log.error("Department Service unavailable. departmentId={}, exceptionType={}, message={}", departmentId, ex.getClass().getSimpleName(), ex.getMessage(), ex);
        throw new DepartmentServiceException("Department Service is currently unavailable. Please try again later.");
    }

    public DepartmentResponse rateLimiterFallback(Long departmentId, RequestNotPermitted ex) {
        log.warn("Rate limit exceeded for Department Service. departmentId={}", departmentId);
        throw new RateLimitExceededException("Too many requests. Please try again after some time.");
    }

}