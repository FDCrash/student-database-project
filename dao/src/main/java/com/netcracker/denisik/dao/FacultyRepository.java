package com.netcracker.denisik.dao;

import com.netcracker.denisik.entities.Faculty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacultyRepository extends CrudRepository<Faculty, Long> {
    boolean existsById(long id);

    boolean existsByName(String name);
}