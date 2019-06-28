package com.netcracker.denisik.converters;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dto.FacultyDTO;
import com.netcracker.denisik.entities.Faculty;
import com.netcracker.denisik.entities.Speciality;
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
public class FacultyConverterTest {
    @Autowired
    private FacultyConverter facultyConverter;

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
    public void toDTO() {
        FacultyDTO facultyDTO = facultyConverter.convert(faculty);
        Assert.assertEquals(facultyDTO.hashCode(), this.facultyDTO.hashCode());
    }

    @Test
    public void fromDTO() {
        Faculty faculty = facultyConverter.convert(facultyDTO);
        Assert.assertEquals(faculty.hashCode(), this.faculty.hashCode());
    }
}
