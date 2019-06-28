package com.netcracker.denisik.entities;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "faculties")
public class Faculty extends BaseEntity {

    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "faculty", cascade = CascadeType.ALL)
    private List<Speciality> specialities;

    @Builder
    public Faculty(long id, String name, List<Speciality> specialities) {
        super(id);
        this.name = name;
        this.specialities = specialities;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (int) getId();
        result = 31 * result + getName().hashCode();
        result = 31 * result + (getSpecialities() != null ? getSpecialities().size() : 0);
        return result;
    }
}
