package io.github.devcavin.jpadomainmodeling.dto.request;

public record CreateStudentRequest(
        String firstName,
        String lastName,
        String studentEmail,
        String guardianEmail,
        String guardianName,
        String guardianMobile
) { }
