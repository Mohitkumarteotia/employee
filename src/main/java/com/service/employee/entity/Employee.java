package com.service.employee.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employees", uniqueConstraints = {@UniqueConstraint(name = "uk_employee_employee_id", columnNames = "employee_id"), @UniqueConstraint(name = "uk_employee_email", columnNames = "email")})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, unique = true)
    private String employeeId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String designation;

    @Column(nullable = false)
    private Double salary;

    @Column(nullable = false, length = 500)
    private String address;
}