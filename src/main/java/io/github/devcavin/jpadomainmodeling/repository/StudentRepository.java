package io.github.devcavin.jpadomainmodeling.repository;

import io.github.devcavin.jpadomainmodeling.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
}
