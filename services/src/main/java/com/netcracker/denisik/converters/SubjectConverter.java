package com.netcracker.denisik.converters;

import com.netcracker.denisik.dto.SubjectDTO;
import com.netcracker.denisik.entities.Subject;
import org.springframework.stereotype.Component;

@Component
public class SubjectConverter {
    public Subject convert(SubjectDTO subjectDTO) {
        return Subject.builder()
                .id(subjectDTO.getId())
                .name(subjectDTO.getName())
                .build();
    }

    public SubjectDTO convert(Subject subject) {
        if (subject == null) {
            return null;
        }
        return SubjectDTO.builder()
                .id(subject.getId())
                .name(subject.getName())
                .build();
    }
}
