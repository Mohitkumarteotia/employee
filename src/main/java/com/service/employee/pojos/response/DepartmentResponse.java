package com.service.employee.pojos.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DepartmentResponse {
    private Long id;
    private String departmentCode;
    private String departmentName;
    private String description;
}