package io.github.devcavin.jpadomainmodeling.repository;

import io.github.devcavin.jpadomainmodeling.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class StudentRepositoryTest {
    @Autowired
    private final StudentRepository studentRepository;

    public StudentRepositoryTest(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Test
    public void saveStudent() {
        Student student = new Student(
                "First",
                "Last",
                "student@gmail.com",
                "Guardian Name",
                "gurdianmail@gmail.com",
                "0712345689"
        );

        studentRepository.save(student);
    }
}
