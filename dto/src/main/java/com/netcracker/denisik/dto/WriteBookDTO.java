package com.netcracker.denisik.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class WriteBookDTO extends BaseDTO {
    private List<SubjectMarkDTO> subjectMarkDTOS;
    private boolean budget;

    @Builder
    public WriteBookDTO(long id, List<SubjectMarkDTO> subjectMarkDTOS, boolean budget) {
        super(id);
        this.subjectMarkDTOS = subjectMarkDTOS;
        this.budget = budget;
    }

    @Override
    @JsonIgnore
    public void setId(long id) {
        super.setId(id);
    }
}
