package com.service.employee.pojos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmployeeResponse {
    private String employeeId;
    private String name;
    private String email;
    private String phoneNumber;
    private String department;
    private String designation;
    private Double salary;
    private String address;
}