package com.example.obs.service.impl;

import com.example.obs.dto.StudentDTO;
import com.example.obs.dto.mapper.StudentMapper;
import com.example.obs.entity.Department;
import com.example.obs.entity.Student;
import com.example.obs.repository.DepartmentRepository;
import com.example.obs.repository.StudentRepository;
import com.example.obs.service.StudentService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final StudentMapper studentMapper;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              DepartmentRepository departmentRepository,
                              StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.departmentRepository = departmentRepository;
        this.studentMapper = studentMapper;
    }

    @Override
    public List<StudentDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
        return studentMapper.toDTO(student);
    }

    @Override
    public StudentDTO getStudentByStudentNumber(String studentNumber) {
        Student student = studentRepository.findByStudentNumber(studentNumber)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with number: " + studentNumber));
        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional
    public StudentDTO saveStudent(StudentDTO studentDTO) {
        Student student = studentMapper.toEntity(studentDTO);

        // Set department
        if (studentDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(studentDTO.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + studentDTO.getDepartmentId()));
            student.setDepartment(department);
        }

        // Handle photo upload
        MultipartFile photoFile = studentDTO.getPhoto();
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                String uploadsDir = "uploads/";
                File uploadDir = new File(uploadsDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String uniqueFilename = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
                String filePath = uploadsDir + uniqueFilename;
                photoFile.transferTo(new File(filePath));
                student.setPhotoPath(filePath);

            } catch (IOException e) {
                throw new RuntimeException("Fotoğraf yüklenirken hata oluştu", e);
            }
        }

        Student savedStudent = studentRepository.save(student);
        return studentMapper.toDTO(savedStudent);
    }

    @Override
    @Transactional
    public StudentDTO updateStudent(Long id, StudentDTO studentDTO) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));

        studentMapper.updateEntityFromDTO(studentDTO, student);

        // Update department
        if (studentDTO.getDepartmentId() != null) {
            Department department = departmentRepository.findById(studentDTO.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + studentDTO.getDepartmentId()));
            student.setDepartment(department);
        }

        // Handle updated photo
        MultipartFile photoFile = studentDTO.getPhoto();
        if (photoFile != null && !photoFile.isEmpty()) {
            try {
                String uploadsDir = "uploads/";
                File uploadDir = new File(uploadsDir);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                String uniqueFilename = UUID.randomUUID() + "_" + photoFile.getOriginalFilename();
                String filePath = uploadsDir + uniqueFilename;
                photoFile.transferTo(new File(filePath));
                student.setPhotoPath(filePath);

            } catch (IOException e) {
                throw new RuntimeException("Fotoğraf güncellenirken hata oluştu", e);
            }
        }

        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toDTO(updatedStudent);
    }

    @Override
    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    @Override
    public List<StudentDTO> getStudentsByDepartment(Long departmentId) {
        return studentRepository.findByDepartmentId(departmentId)
                .stream()
                .map(studentMapper::toDTO)
                .collect(Collectors.toList());
    }
}