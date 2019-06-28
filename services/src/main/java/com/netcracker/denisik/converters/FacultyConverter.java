package com.netcracker.denisik.converters;

import com.netcracker.denisik.dto.FacultyDTO;
import com.netcracker.denisik.entities.Faculty;
import com.netcracker.denisik.entities.Speciality;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class FacultyConverter {
    public Faculty convert(FacultyDTO facultyDTO) {
        List<Speciality> specialityEntities = new ArrayList<>();
        int quantity;
        quantity = facultyDTO.getSpecialities().size();
        for (int i = 0; i < quantity; i++) {
            specialityEntities.add(
                    Speciality.builder()
                            .name(facultyDTO.getSpecialities().get(i))
                            .build());
        }
        Faculty faculty = Faculty.builder()
                .id(facultyDTO.getId())
                .name(facultyDTO.getName())
                .specialities(specialityEntities)
                .build();
        for (int i = 0; i < quantity; i++) {
            faculty.getSpecialities().get(i).setFaculty(faculty);
        }
        return faculty;
    }

    public FacultyDTO convert(Faculty faculty) {
        if (faculty == null) {
            return null;
        }
        return FacultyDTO.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .specialities(
                        faculty.getSpecialities().stream()
                                .map(Speciality::getName)
                                .collect(Collectors.toList()))
                .build();
    }
}
