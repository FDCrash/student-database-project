package com.netcracker.denisik.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SpecialityDTO extends BaseDTO {
    private String name;
    private String faculty;
    private long facultyId;

    @Builder
    public SpecialityDTO(long id, String name, String faculty, long facultyId) {
        super(id);
        this.name = name;
        this.faculty = faculty;
        this.facultyId = facultyId;
    }
}
