package io.github.devcavin.jpadomainmodeling.dto.response;

import io.github.devcavin.jpadomainmodeling.entity.Student;

public record StudentResponse(
        Long studentId,
        String firstName,
        String lastName,
        String studentEmail,
        String guardianName,
        String guardianEmail,
        String guardianMobile
) {
    public static StudentResponse fromResponse(Student student) {
        return new StudentResponse(
                student.getStudentId(),
                student.getFirstName(),
                student.getLastName(),
                student.getStudentEmail(),
                student.getStudentEmail(),
                student.getGuardianName(),
                student.getGuardianMobile()
        );
    }
}
