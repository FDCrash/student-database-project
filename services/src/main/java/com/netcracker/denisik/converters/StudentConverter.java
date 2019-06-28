package com.netcracker.denisik.converters;

import com.netcracker.denisik.dto.*;
import com.netcracker.denisik.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StudentConverter {
    private SubjectConverter subjectConverter;

    @Autowired
    public StudentConverter(SubjectConverter subjectConverter) {
        this.subjectConverter = subjectConverter;
    }

    public Student convert(StudentDTO studentDTO) {
        List<SubjectMark> subjectMarkEntities = new ArrayList<>();
        User user = User.builder()
                .id(studentDTO.getId())
                .login(studentDTO.getLogin())
                .password(studentDTO.getPassword())
                .role(Role.STUDENT)
                .name(studentDTO.getName())
                .build();
        for (SubjectMarkDTO subjectMarkDTO : studentDTO.getWriteBook().getSubjectMarkDTOS()) {
            subjectMarkEntities.add(
                    SubjectMark.builder()
                            .mark(subjectMarkDTO.getMark())
                            .subject(subjectConverter.convert(subjectMarkDTO.getSubject()))
                            .build());
        }
        WriteBook writeBook = WriteBook.builder()
                .budget(studentDTO.getWriteBook().isBudget())
                .subjectMarks(subjectMarkEntities)
                .build();
        Speciality speciality = Speciality.builder()
                .id(studentDTO.getSpecialityId())
                .build();
        Student student = Student.builderStudent()
                .user(user)
                .groupId(studentDTO.getGroupId())
                .writeBook(writeBook)
                .speciality(speciality)
                .build();
        for (SubjectMark subjectMark : student.getWriteBook().getSubjectMarks()) {
            subjectMark.setWriteBook(student.getWriteBook());
        }
        return student;
    }

    public StudentDTO convert(Student student) {
        if (student == null) {
            return null;
        }
        List<SubjectMarkDTO> subjectMarkDTOS = new ArrayList<>();
        UserDTO userDTO = UserDTO.builder()
                .id(student.getId())
                .roleDTO(RoleDTO.STUDENT)
                .login(student.getLogin())
                .password(student.getPassword())
                .name(student.getName())
                .build();
        for (SubjectMark subjectMark : student.getWriteBook().getSubjectMarks()) {
            subjectMarkDTOS.add(SubjectMarkDTO.builder()
                    .mark(subjectMark.getMark())
                    .subject(subjectConverter.convert(subjectMark.getSubject()))
                    .build());
        }
        return StudentDTO.builderStudent()
                .userDTO(userDTO)
                .groupId(student.getGroupId())
                .speciality(student.getSpeciality().getName())
                .specialityId(student.getSpeciality().getId())
                .writeBook(
                        WriteBookDTO.builder()
                                .id(student.getWriteBook().getId())
                                .budget(student.getWriteBook().isBudget())
                                .subjectMarkDTOS(subjectMarkDTOS)
                                .build())
                .build();
    }
}
