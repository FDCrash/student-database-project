package com.netcracker.denisik.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "writebook")
public class WriteBook extends BaseEntity {

    @OneToMany(mappedBy = "writeBook", cascade = CascadeType.ALL)
    private List<SubjectMark> subjectMarks;

    @Column(name = "budget")
    private boolean budget;

    @OneToOne(mappedBy = "writeBook")
    private Student student;

    @Builder
    public WriteBook(long id, List<SubjectMark> subjectMarks, boolean budget, Student student) {
        super(id);
        this.subjectMarks = subjectMarks;
        this.budget = budget;
        this.student = student;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) getId();
        result = 31 * result + this.getSubjectMarks().hashCode();
        result = 31 * result + (isBudget() ? 1 : 0);
        result = 31 * result + (getStudent() != null ? (int) getStudent().getGroupId() : 0);
        return result;
    }
}
