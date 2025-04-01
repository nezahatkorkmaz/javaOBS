package com.example.obs.controller;

import com.example.obs.dto.StudentDTO;
import com.example.obs.entity.Department;
import com.example.obs.repository.DepartmentRepository;
import com.example.obs.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public StudentController(StudentService studentService, DepartmentRepository departmentRepository) {
        this.studentService = studentService;
        this.departmentRepository = departmentRepository;
    }

    @GetMapping
    public String listStudents(Model model) {
        List<StudentDTO> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return "student-list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        model.addAttribute("departments", departmentRepository.findAll());
        return "student-form";
    }

    @PostMapping("/save")
    public String saveStudent(@Valid @ModelAttribute("student") StudentDTO studentDTO,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findAll());
            return "student-form";
        }

        studentService.saveStudent(studentDTO);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        StudentDTO student = studentService.getStudentById(id);
        model.addAttribute("student", student);
        model.addAttribute("departments", departmentRepository.findAll());
        return "student-form";
    }

    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable Long id,
                                @Valid @ModelAttribute("student") StudentDTO studentDTO,
                                BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("departments", departmentRepository.findAll());
            return "student-form";
        }

        studentService.updateStudent(id, studentDTO);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return "redirect:/students";
    }

    @GetMapping("/department/{departmentId}")
    public String getStudentsByDepartment(@PathVariable Long departmentId, Model model) {
        List<StudentDTO> students = studentService.getStudentsByDepartment(departmentId);
        Department department = departmentRepository.findById(departmentId).orElse(null);

        model.addAttribute("students", students);
        model.addAttribute("department", department);
        return "student-list";
    }
}