package com.service.employee.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees", uniqueConstraints = {@UniqueConstraint(name = "uk_employee_email", columnNames = "email")})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "department_name", nullable = false)
    private String departmentName;

    @Column(name = "designation", nullable = false)
    private String designation;

    @Column(name = "salary", nullable = false)
    private Double salary;
}