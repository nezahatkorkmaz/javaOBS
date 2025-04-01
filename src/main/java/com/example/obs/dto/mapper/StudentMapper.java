package com.example.obs.dto.mapper;

import com.example.obs.dto.StudentDTO;
import com.example.obs.entity.Address;
import com.example.obs.entity.Department;
import com.example.obs.entity.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentDTO toDTO(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setStudentNumber(student.getStudentNumber());
        dto.setEmail(student.getEmail());
        dto.setGpa(student.getGpa());
        dto.setPhotoPath(student.getPhotoPath()); // Eklenen satır

        if (student.getAddress() != null) {
            dto.setCity(student.getAddress().getCity());
            dto.setDistrict(student.getAddress().getDistrict());
            dto.setStreet(student.getAddress().getStreet());
            dto.setPostalCode(student.getAddress().getPostalCode());
        }

        if (student.getDepartment() != null) {
            dto.setDepartmentId(student.getDepartment().getId());
            dto.setDepartmentName(student.getDepartment().getName());
        }

        return dto;
    }

    public Student toEntity(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setStudentNumber(dto.getStudentNumber());
        student.setEmail(dto.getEmail());
        student.setGpa(dto.getGpa());
        student.setPhotoPath(dto.getPhotoPath()); // Eklenen satır

        Address address = new Address();
        address.setCity(dto.getCity());
        address.setDistrict(dto.getDistrict());
        address.setStreet(dto.getStreet());
        address.setPostalCode(dto.getPostalCode());
        student.setAddress(address);

        return student;
    }


    public void updateEntityFromDTO(StudentDTO dto, Student student) {
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setStudentNumber(dto.getStudentNumber());
        student.setEmail(dto.getEmail());
        student.setGpa(dto.getGpa());
        student.setPhotoPath(dto.getPhotoPath());

        // Update Address
        if (student.getAddress() == null) {
            student.setAddress(new Address());
        }
        student.getAddress().setCity(dto.getCity());
        student.getAddress().setDistrict(dto.getDistrict());
        student.getAddress().setStreet(dto.getStreet());
        student.getAddress().setPostalCode(dto.getPostalCode());

        // Department will be updated by service
    }
}