package com.netcracker.denisik.services.implementantions;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dao.FacultyRepository;
import com.netcracker.denisik.dao.SpecialityRepository;
import com.netcracker.denisik.dto.FacultyDTO;
import com.netcracker.denisik.entities.Faculty;
import com.netcracker.denisik.entities.Speciality;
import com.netcracker.denisik.services.implementations.FacultyServiceImpl;
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
public class FacultyServiceImplTest {

    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private FacultyServiceImpl facultyService;
    @Autowired
    private SpecialityRepository specialityRepository;

    private Faculty faculty;
    private FacultyDTO facultyDTO;

    @Before
    public void setUp() {
        Speciality speciality = Speciality.builder()
                .id(13)
                .name("test")
                .build();
        faculty = Faculty.builder()
                .id(13)
                .name("test")
                .specialities(Collections.singletonList(speciality))
                .build();
        speciality.setFaculty(faculty);
        facultyDTO = FacultyDTO.builder()
                .id(13)
                .name("test")
                .specialities(Collections.singletonList(speciality.getName()))
                .build();
    }

    @After
    public void tearDown() {
        facultyDTO = null;
        faculty = null;
    }

    @Test
    public void testGetAll() {
        List<Faculty> mockList = Collections.singletonList(faculty);
        List<FacultyDTO> expectedAnswer = Collections.singletonList(facultyDTO);

        when(facultyRepository.findAll()).thenReturn(mockList);
        assertEquals(expectedAnswer, facultyService.getAll());
    }

    @Test
    public void testDelete() {
        when(facultyRepository.existsById(13L)).thenReturn(true);
        facultyService.delete(13L);
        verify(facultyRepository, times(1)).delete(13L);
    }

    @Test
    public void testAdd() {
        when(facultyRepository.save(faculty)).thenReturn(faculty);
        long id = facultyService.add(facultyDTO);
        assertEquals(id, faculty.getId());
    }

    @Test
    public void testGet() {
        when(facultyRepository.findOne(13L)).thenReturn(faculty);
        assertEquals(facultyDTO, facultyService.get(13L));
    }

    @Test
    public void testCheckSpecialities() {
        when(specialityRepository.existsByName("test")).thenReturn(false);
        facultyService.checkSpecialities(facultyDTO.getSpecialities());
        verify(specialityRepository, times(0)).getByName("test");
    }
}
