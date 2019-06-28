package com.netcracker.denisik.services.implementantions;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dao.SubjectRepository;
import com.netcracker.denisik.dto.SubjectDTO;
import com.netcracker.denisik.entities.Subject;
import com.netcracker.denisik.services.implementations.SubjectServiceImpl;
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
public class SubjectServiceImplTest {
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private SubjectServiceImpl subjectService;

    private Subject subject;
    private SubjectDTO subjectDTO;

    @Before
    public void setUp() {
        subject = Subject.builder()
                .id(13)
                .name("test")
                .build();
        subjectDTO = SubjectDTO.builder()
                .id(13)
                .name("test")
                .build();
    }

    @After
    public void tearDown() {
        subject = null;
        subjectDTO = null;
    }

    @Test
    public void testGetAll() {
        List<Subject> mockList = Collections.singletonList(subject);
        List<SubjectDTO> expectedAnswer = Collections.singletonList(subjectDTO);

        when(subjectRepository.findAll()).thenReturn(mockList);
        assertEquals(expectedAnswer, subjectService.getAll());
    }

    @Test
    public void testDelete() {
        when(subjectRepository.existsById(13L)).thenReturn(true);
        subjectService.delete(13L);
        verify(subjectRepository, times(1)).delete(13L);
    }

    @Test
    public void testAdd() {
        when(subjectRepository.save(subject)).thenReturn(subject);
        long id = subjectService.add(subjectDTO);
        assertEquals(id, subject.getId());
    }

    @Test
    public void testGet() {
        when(subjectRepository.findOne(13L)).thenReturn(subject);
        assertEquals(subjectDTO, subjectService.get(13L));
    }
}
