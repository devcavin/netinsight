package io.github.devcavin.jpadomainmodeling.service;

import io.github.devcavin.jpadomainmodeling.dto.response.StudentResponse;
import io.github.devcavin.jpadomainmodeling.entity.Student;
import io.github.devcavin.jpadomainmodeling.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) { this.studentRepository = studentRepository; }

    public Optional<StudentResponse> byId(Long id) {
        Student student = studentRepository.findById(id).orElse(null);
        return  Optional.ofNullable(student).map(StudentResponse::fromResponse);
    }
}