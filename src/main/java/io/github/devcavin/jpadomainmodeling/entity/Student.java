package io.github.devcavin.jpadomainmodeling.entity;

import io.github.devcavin.jpadomainmodeling.dto.request.CreateStudentRequest;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(
        name = "students",
        uniqueConstraints = @UniqueConstraint(name = "student_email_unique", columnNames = "student_email")
)
public class Student {
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "students_seq")
    @SequenceGenerator(name = "students_seq", sequenceName = "students_seq", allocationSize = 1)
    private Long studentId;
    private String firstName;
    private String lastName;

    @Column(nullable = false)
    private String studentEmail;
    private String guardianName;
    private String guardianEmail;
    private String guardianMobile;


    public Student(Long id, String firstName, String lastName, String studentEmail, String guardianName,
                   String guardianEmail, String guardianMobile) {
        this.studentId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentEmail = studentEmail;
        this.guardianName = guardianName;
        this.guardianEmail = guardianEmail;
        this.guardianMobile = guardianMobile;
    }

    public Student() {}

    public Student(String student, String name, String mail, String guardianName, String mail1, String number) {
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getGuardianName() {
        return guardianName;
    }

    public void setGuardianName(String guardianName) {
        this.guardianName = guardianName;
    }

    public String getGuardianEmail() {
        return guardianEmail;
    }

    public void setGuardianEmail(String guardianEmail) {
        this.guardianEmail = guardianEmail;
    }

    public String getGuardianMobile() {
        return guardianMobile;
    }

    public void setGuardianMobile(String guardianMobile) {
        this.guardianMobile = guardianMobile;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(studentId);
    }

    @Override
    public String toString() {
        return String.format("Student{studentId=%s, firstName='%s', lastName='%s', studentEmail='%s', guardianEmail='%s', " +
                        "guardianName='%s', " +
                        "guardianMobile='%s'}",
                studentId, firstName, lastName, studentEmail, guardianEmail, guardianName, guardianMobile);
    }

    // create student request entity
    public Student toEntity(CreateStudentRequest requestDto) {
        Student student = new Student();
        student.setStudentId(studentId);
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setStudentEmail(studentEmail);
        student.setGuardianName(guardianName);
        student.setGuardianEmail(guardianEmail);
        student.setGuardianMobile(guardianMobile);
        return student;
    }
}
