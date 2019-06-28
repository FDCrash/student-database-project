package com.netcracker.denisik.converters;

import com.netcracker.denisik.configuration.TestConfiguration;
import com.netcracker.denisik.dto.SubjectDTO;
import com.netcracker.denisik.entities.Subject;
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
public class SubjectConverterTest {

    @Autowired
    private SubjectConverter subjectConverter;

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
    public void toDTO() {
        SubjectDTO subjectDTO = subjectConverter.convert(subject);
        Assert.assertEquals(subjectDTO.hashCode(), this.subjectDTO.hashCode());
    }

    @Test
    public void fromDTO() {
        Subject subject = subjectConverter.convert(subjectDTO);
        Assert.assertEquals(subject.hashCode(), this.subject.hashCode());
    }
}