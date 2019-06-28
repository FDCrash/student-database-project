package com.netcracker.denisik.configuration;

import com.netcracker.denisik.converters.*;
import com.netcracker.denisik.dao.*;
import com.netcracker.denisik.services.implementations.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class TestConfiguration {
    @Bean
    public FacultyConverter facultyConverter() {
        return new FacultyConverter();
    }

    @Bean
    public SpecialityConverter specialityConverter() {
        return new SpecialityConverter();
    }

    @Bean
    public SubjectConverter subjectConverter() {
        return new SubjectConverter();
    }

    @Bean
    public StudentConverter studentConverter() {
        return new StudentConverter(subjectConverter());
    }

    @Bean
    public UserConverter userConverter() {
        return new UserConverter();
    }

    @Bean
    public FacultyRepository facultyRepository() {
        return mock(FacultyRepository.class);
    }

    @Bean
    public SpecialityRepository specialityRepository() {
        return mock(SpecialityRepository.class);
    }

    @Bean
    public StudentRepository studentRepository() {
        return mock(StudentRepository.class);
    }

    @Bean
    public SubjectRepository subjectRepository() {
        return mock(SubjectRepository.class);
    }

    @Bean
    public UserRepository userRepository() {
        return mock(UserRepository.class);
    }

    @Bean
    public FacultyServiceImpl facultyService() {
        return new FacultyServiceImpl(facultyRepository(), facultyConverter(), specialityRepository());
    }

    @Bean
    public SpecialityServiceImpl specialityService() {
        return new SpecialityServiceImpl(specialityRepository(), specialityConverter(), facultyRepository());
    }

    @Bean
    public StudentServiceImpl studentService() {
        return new StudentServiceImpl(studentRepository(), studentConverter(), specialityRepository(), subjectRepository());
    }

    @Bean
    public SubjectServiceImpl subjectService() {
        return new SubjectServiceImpl(subjectRepository(), subjectConverter());
    }

    @Bean
    public UserServiceImpl userService() {
        return new UserServiceImpl(userConverter(), studentRepository(), userRepository());
    }
}
