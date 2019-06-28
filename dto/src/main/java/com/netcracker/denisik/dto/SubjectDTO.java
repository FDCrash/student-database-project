package com.netcracker.denisik.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class SubjectDTO extends BaseDTO {
    String name;

    @Builder
    public SubjectDTO(long id, String name) {
        super(id);
        this.name = name;
    }
}
