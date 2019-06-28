package com.netcracker.denisik.converters;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dto.*;
import com.netcracker.denisik.entities.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class StudentConverterTest {
    @Autowired
    private StudentConverter studentConverter;

    private Student student;
    private StudentDTO studentDTO;

    @Before
    public void setUp() {
        User user = User.builder()
                .id(13)
                .name("test")
                .role(Role.STUDENT)
                .password("test")
                .login("test")
                .build();
        Speciality speciality = Speciality.builder()
                .id(13)
                .build();
        Subject subject = Subject.builder()
                .id(3)
                .name("test")
                .build();
        SubjectMark subjectMark = SubjectMark.builder()
                .mark(10)
                .subject(subject)
                .build();
        WriteBook writeBook = WriteBook.builder()
                .budget(true)
                .subjectMarks(Collections.singletonList(subjectMark))
                .build();
        subjectMark.setWriteBook(writeBook);
        student = Student.builderStudent()
                .user(user)
                .groupId(13)
                .speciality(speciality)
                .writeBook(writeBook)
                .build();

        UserDTO userDTO = UserDTO.builder()
                .id(13)
                .roleDTO(RoleDTO.STUDENT)
                .name("test")
                .password("test")
                .login("test")
                .build();
        SubjectDTO subjectDTO = SubjectDTO.builder()
                .id(3)
                .name("test")
                .build();
        SubjectMarkDTO subjectMarkDTO = SubjectMarkDTO.builder()
                .mark(10)
                .subject(subjectDTO)
                .build();
        WriteBookDTO writeBookDTO = WriteBookDTO.builder()
                .budget(true)
                .subjectMarkDTOS(Collections.singletonList(subjectMarkDTO))
                .build();
        studentDTO = StudentDTO.builderStudent()
                .userDTO(userDTO)
                .specialityId(13)
                .groupId(13)
                .writeBook(writeBookDTO)
                .build();
    }

    @After
    public void tearDown() {
        studentDTO = null;
        student = null;
    }

    @Test
    public void toDTO() {
        StudentDTO studentDTO = studentConverter.convert(student);
        Assert.assertEquals(studentDTO.hashCode(), this.studentDTO.hashCode());
    }

    @Test
    public void fromDTO() {
        Student student = studentConverter.convert(studentDTO);
        Assert.assertEquals(student.hashCode(), this.student.hashCode());
    }
}
