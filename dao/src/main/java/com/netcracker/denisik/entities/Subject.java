package com.netcracker.denisik.entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@Entity
@Table(name = "subjects")
public class Subject extends BaseEntity {

    @Column(name = "subject", unique = true)
    private String name;

    @OneToMany(mappedBy = "subject")
    private List<SubjectMark> subjectMarks;

    @Builder
    public Subject(long id, String name, List<SubjectMark> subjectMarks) {
        super(id);
        this.name = name;
        this.subjectMarks = subjectMarks;
    }
}
