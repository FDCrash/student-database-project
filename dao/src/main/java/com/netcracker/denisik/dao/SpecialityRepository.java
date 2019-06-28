package com.netcracker.denisik.dao;

import com.netcracker.denisik.entities.Speciality;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialityRepository extends CrudRepository<Speciality, Long> {
    Speciality getByName(String name);

    boolean existsByName(String name);

    boolean existsById(long id);
}
