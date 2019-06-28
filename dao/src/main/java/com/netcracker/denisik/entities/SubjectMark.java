package com.netcracker.denisik.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "subjectmark")
public class SubjectMark extends BaseEntity {

    @Column(name = "mark")
    private int mark;

    @ManyToOne
    @JoinColumn(name = "subjects_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "writebook_id")
    private WriteBook writeBook;

    @Builder
    public SubjectMark(long id, int mark, Subject subject, WriteBook writeBook) {
        super(id);
        this.mark = mark;
        this.subject = subject;
        this.writeBook = writeBook;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) getId();
        result = 31 * result + getMark();
        result = 31 * result + getSubject().hashCode();
        result = 31 * result + (getWriteBook() != null ? (int) getWriteBook().getId() : 0);
        return result;
    }
}
