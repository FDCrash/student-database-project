package com.netcracker.denisik.services.implementantions;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dao.FacultyRepository;
import com.netcracker.denisik.dao.SpecialityRepository;
import com.netcracker.denisik.dto.SpecialityDTO;
import com.netcracker.denisik.entities.Faculty;
import com.netcracker.denisik.entities.Speciality;
import com.netcracker.denisik.services.implementations.SpecialityServiceImpl;
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
public class SpecialityServiceImplTest {

    @Autowired
    private SpecialityRepository specialityRepository;
    @Autowired
    private SpecialityServiceImpl specialityService;
    @Autowired
    private FacultyRepository facultyRepository;

    private Speciality speciality;
    private SpecialityDTO specialityDTO;

    @Before
    public void setUp() {
        Faculty faculty = Faculty.builder()
                .id(13)
                .name("test")
                .build();
        speciality = Speciality.builder()
                .id(13)
                .name("test")
                .faculty(faculty)
                .build();
        specialityDTO = SpecialityDTO.builder()
                .id(13)
                .name("test")
                .faculty("test")
                .facultyId(13)
                .build();
    }

    @After
    public void tearDown() {
        speciality = null;
        specialityDTO = null;
    }

    @Test
    public void testGetAll() {
        List<Speciality> mockList = Collections.singletonList(speciality);
        List<SpecialityDTO> expectedAnswer = Collections.singletonList(specialityDTO);

        when(specialityRepository.findAll()).thenReturn(mockList);
        assertEquals(expectedAnswer, specialityService.getAll());
    }

    @Test
    public void testDelete() {
        when(specialityRepository.existsById(13L)).thenReturn(true);
        specialityService.delete(13L);
        verify(specialityRepository, times(1)).delete(13L);
    }

    @Test
    public void testAdd() {
        when(specialityRepository.save(speciality)).thenReturn(speciality);
        when(facultyRepository.existsById(13)).thenReturn(true);
        long id = specialityService.add(specialityDTO);
        assertEquals(id, speciality.getId());
    }

    @Test
    public void get() {
        when(specialityRepository.findOne(13L)).thenReturn(speciality);
        assertEquals(specialityDTO, specialityService.get(13L));
    }
}
