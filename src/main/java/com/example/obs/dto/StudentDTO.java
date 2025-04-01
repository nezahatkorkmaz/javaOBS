package com.example.obs.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Bu anotasyon tüm getter/setter metotlarını otomatik oluşturur
@NoArgsConstructor
@AllArgsConstructor
public class StudentDTO {
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Student number is required")
    @Pattern(regexp = "^\\d{10}$", message = "Student number must be 10 digits")
    private String studentNumber;

    @Email(message = "Please provide a valid email address")
    private String email;

    private Double gpa;

    // Address fields
    private String city;
    private String district;
    private String street;
    private String postalCode;

    // Department
    private Long departmentId;
    private String departmentName;
}