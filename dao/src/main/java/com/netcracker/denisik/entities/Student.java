package com.netcracker.denisik.entities;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@NoArgsConstructor
@Table(name = "students")
@PrimaryKeyJoinColumn(name = "id")
public class Student extends User {

    @Column(name = "id")
    private long id;

    @Column(name = "groupId")
    private long groupId;

    @ManyToOne
    @JoinColumn(name = "specialities_id")
    private Speciality speciality;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "writebook_id")
    private WriteBook writeBook;

    @Builder(builderMethodName = "builderStudent")
    public Student(User user, long groupId, Speciality speciality, WriteBook writeBook) {
        super(user.getId(), user.getRole(), user.getPassword(), user.getLogin(), user.getName());
        this.id = user.getId();
        this.groupId = groupId;
        this.speciality = speciality;
        this.writeBook = writeBook;
    }
}
