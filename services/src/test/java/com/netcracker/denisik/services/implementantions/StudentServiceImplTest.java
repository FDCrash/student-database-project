package com.netcracker.denisik.services.implementantions;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dao.SpecialityRepository;
import com.netcracker.denisik.dao.StudentRepository;
import com.netcracker.denisik.dao.SubjectRepository;
import com.netcracker.denisik.dto.*;
import com.netcracker.denisik.entities.*;
import com.netcracker.denisik.services.implementations.StudentServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class StudentServiceImplTest {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private StudentServiceImpl studentService;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SpecialityRepository specialityRepository;

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
        student = null;
        studentDTO = null;
    }

    @Test
    public void testGetAll() {
        List<Student> mockList = Collections.singletonList(student);
        List<StudentDTO> expectedAnswer = Collections.singletonList(studentDTO);

        when(studentRepository.findAll()).thenReturn(mockList);
        assertEquals(expectedAnswer, studentService.getAll());
    }

    @Test
    public void testGetAllByGroup() {
        List<Student> mockList = Collections.singletonList(student);
        List<StudentDTO> expectedAnswer = Collections.singletonList(studentDTO);

        when(studentRepository.getAllByGroupId(13)).thenReturn(mockList);
        assertEquals(expectedAnswer, studentService.getAllByGroup(13));
    }

    @Test
    public void testGetAllBySpeciality() {
        List<Student> mockList = Collections.singletonList(student);
        List<StudentDTO> expectedAnswer = Collections.singletonList(studentDTO);

        when(studentRepository.getAllBySpecialityName("test")).thenReturn(mockList);
        assertEquals(expectedAnswer, studentService.getAllBySpeciality("test"));
    }

    @Test
    public void testDelete() {
        when(studentRepository.existsById(13L)).thenReturn(true);
        studentService.delete(13L);
        verify(studentRepository, times(1)).delete(13L);
    }

    @Test
    public void testAdd() {
        when(studentRepository.save(student)).thenReturn(student);
        when(specialityRepository.existsById(13L)).thenReturn(true);
        when(subjectRepository.existsById(3L)).thenReturn(true);
        long id = studentService.add(studentDTO);
        assertEquals(id, student.getId());
    }

    @Test
    public void get() {
        when(studentRepository.findOne(13L)).thenReturn(student);
        assertEquals(studentDTO, studentService.get(13L));
    }

    @Test
    public void checkSubjectTest() {
        when(subjectRepository.existsById(3L)).thenReturn(true);
        studentService.checkSubjects(studentDTO.getWriteBook().getSubjectMarkDTOS());
        verify(subjectRepository, times(0)).findOne(3L);
    }
}
