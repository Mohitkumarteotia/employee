package com.service.employee.service.impl;

import com.service.employee.entity.Employee;
import com.service.employee.pojos.request.EmployeeRequest;
import com.service.employee.pojos.response.DepartmentResponse;
import com.service.employee.pojos.response.EmployeeResponse;
import com.service.employee.repository.EmployeeRepository;
import com.service.employee.service.EmployeeService;
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
    public EmployeeResponse createEmployee(EmployeeRequest employeeRequest) {
        log.info("Creating employee with employeeId={} and departmentId={}", employeeRequest.getEmployeeId(), employeeRequest.getDepartmentId());
        DepartmentResponse departmentResponse = departmentService.getDepartment(employeeRequest.getDepartmentId());
        Employee employee = persistEmployeeDetails(employeeRequest, departmentResponse.getDepartmentName());
        log.info("Employee Created Successfully : {}", employee.getEmployeeId());
        return toEmployeeResponse(employee);
    }

    private Employee persistEmployeeDetails(EmployeeRequest employeeRequest, String departmentName) {
        Employee employee = Employee.builder()
                .employeeId(employeeRequest.getEmployeeId())
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
        return EmployeeResponse.builder().employeeId(employee.getEmployeeId()).name(employee.getName()).email(employee.getEmail()).phoneNumber(employee.getPhoneNumber()).department(employee.getDepartment()).designation(employee.getDesignation()).salary(employee.getSalary()).address(employee.getAddress()).build();
    }

}