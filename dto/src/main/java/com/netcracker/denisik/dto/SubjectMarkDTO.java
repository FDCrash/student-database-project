package com.netcracker.denisik.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SubjectMarkDTO {
    private int mark;
    private SubjectDTO subject;

    @Builder
    public SubjectMarkDTO(int mark, SubjectDTO subject) {
        this.mark = mark;
        this.subject = subject;
    }
}