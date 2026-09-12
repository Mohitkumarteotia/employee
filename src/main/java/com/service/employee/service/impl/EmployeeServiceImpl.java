package com.service.employee.service.impl;

import com.service.employee.entity.Employee;
import com.service.employee.exception.custom.DepartmentNotFoundException;
import com.service.employee.exception.custom.RateLimitExceededException;
import com.service.employee.pojos.constant.ServiceConstants;
import com.service.employee.pojos.request.EmployeeRequest;
import com.service.employee.pojos.response.DepartmentResponse;
import com.service.employee.pojos.response.EmployeeResponse;
import com.service.employee.repository.EmployeeRepository;
import com.service.employee.service.EmployeeService;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentService departmentService;

    @Override
    @Transactional
    @RateLimiter(name = ServiceConstants.DEPARTMENT_SERVICE, fallbackMethod = "rateLimiterFallback")
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        String departmentName = "";
        log.info("Creating employee for a departmentId : {}", employeeRequest.getDepartmentId());
        DepartmentResponse departmentResponse = departmentService.getDepartment(employeeRequest.getDepartmentId());
        departmentName = departmentResponse.getDepartmentName();
        if (departmentName == null ||departmentName.isBlank()) {
            throw new DepartmentNotFoundException("DepartmentName not found");
        }
        Employee employee = persistEmployeeDetails(employeeRequest, departmentName);
        log.info("Employee Created Successfully for a EmployeeId : {}", employee.getId());
        return mapToResponse(employee);
    }

    private Employee persistEmployeeDetails(EmployeeRequest employeeRequest, String departmentName) {
        Employee employee = Employee.builder()
                .name(employeeRequest.getName())
                .email(employeeRequest.getEmail())
                .departmentName(departmentName)
                .designation(employeeRequest.getDesignation())
                .salary(employeeRequest.getSalary())
                .build();
        return employeeRepository.save(employee);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .name(employee.getName())
                .email(employee.getEmail())
                .department(employee.getDepartmentName())
                .designation(employee.getDesignation())
                .salary(employee.getSalary())
                .build();
    }

    public EmployeeResponse rateLimiterFallback(EmployeeRequest employeeRequest, RequestNotPermitted ex) {
        log.warn("Rate limit exceeded for Employee Service");
        throw new RateLimitExceededException("Too many requests. Please try again after some time.");
    }

}