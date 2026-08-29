package com.service.employee.service.impl;

import com.service.employee.entity.Employee;
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
        log.info("Creating employee with employeeCode={} and departmentId={}", employeeRequest.getEmployeeCode(), employeeRequest.getDepartmentId());
        DepartmentResponse departmentResponse = departmentService.getDepartment(employeeRequest.getDepartmentId());
        Employee employee = persistEmployeeDetails(employeeRequest, departmentResponse.getDepartmentName());
        log.info("Employee Created Successfully for a Employee Code : {}", employee.getEmployeeCode());
        return toEmployeeResponse(employee);
    }

    private Employee persistEmployeeDetails(EmployeeRequest employeeRequest, String departmentName) {
        Employee employee = Employee.builder()
                .employeeCode(employeeRequest.getEmployeeCode())
                .name(employeeRequest.getName())
                .email(employeeRequest.getEmail())
                .phoneNumber(employeeRequest.getPhoneNumber())
                .department(departmentName)
                .designation(employeeRequest.getDesignation())
                .salary(employeeRequest.getSalary())
                .address(employeeRequest.getAddress())
                .build();
        return employeeRepository.save(employee);
    }

    private EmployeeResponse toEmployeeResponse(Employee employee) {
        return EmployeeResponse.builder()
                .employeeCode(employee.getEmployeeCode())
                .name(employee.getName())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .department(employee.getDepartment())
                .designation(employee.getDesignation())
                .salary(employee.getSalary())
                .address(employee.getAddress())
                .build();
    }

    public EmployeeResponse rateLimiterFallback(EmployeeRequest employeeRequest, RequestNotPermitted ex) {
        log.warn("Rate limit exceeded for Employee Service");
        throw new RateLimitExceededException("Too many requests. Please try again after some time.");
    }

}