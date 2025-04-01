package com.example.obs.service;

import com.example.obs.dto.StudentDTO;

import java.util.List;

public interface StudentService {
    List<StudentDTO> getAllStudents();
    StudentDTO getStudentById(Long id);
    StudentDTO getStudentByStudentNumber(String studentNumber);
    StudentDTO saveStudent(StudentDTO studentDTO);
    StudentDTO updateStudent(Long id, StudentDTO studentDTO);
    void deleteStudent(Long id);
    List<StudentDTO> getStudentsByDepartment(Long departmentId);
}