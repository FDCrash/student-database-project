package com.netcracker.denisik.converters;

import com.netcracker.denisik.dto.SpecialityDTO;
import com.netcracker.denisik.entities.Faculty;
import com.netcracker.denisik.entities.Speciality;
import org.springframework.stereotype.Component;

@Component
public class SpecialityConverter {
    public Speciality convert(SpecialityDTO specialityDTO) {
        Faculty faculty = Faculty.builder()
                .id(specialityDTO.getFacultyId())
                .build();
        return Speciality.builder()
                .id(specialityDTO.getId())
                .name(specialityDTO.getName())
                .faculty(faculty)
                .build();

    }

    public SpecialityDTO convert(Speciality speciality) {
        if (speciality == null) {
            return null;
        }
        return SpecialityDTO.builder()
                .id(speciality.getId())
                .name(speciality.getName())
                .faculty(speciality.getFaculty().getName())
                .facultyId(speciality.getFaculty().getId())
                .build();
    }
}