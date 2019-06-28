package com.netcracker.denisik.converters;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dto.SpecialityDTO;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfiguration.class)
public class SpecialityConverterTest {
    @Autowired
    SpecialityConverter specialityConverter;

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
        specialityDTO = null;
        speciality = null;
    }

    @Test
    public void toDTO() {
        SpecialityDTO specialityDTO = specialityConverter.convert(speciality);

        Assert.assertEquals(specialityDTO.hashCode(), this.specialityDTO.hashCode());
        Assert.assertEquals(specialityDTO.getFaculty(), this.specialityDTO.getFaculty());
        Assert.assertEquals(specialityDTO.getFacultyId(), this.specialityDTO.getFacultyId());
    }

    @Test
    public void fromDTO() {
        Speciality speciality = specialityConverter.convert(specialityDTO);
        Assert.assertEquals(speciality.hashCode(), this.speciality.hashCode());
        Assert.assertEquals(speciality.getFaculty().getId(), speciality.getFaculty().getId());
    }
}
