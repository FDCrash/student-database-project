package com.netcracker.denisik.dao;

import com.netcracker.denisik.entities.Subject;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectRepository extends CrudRepository<Subject, Long> {
    Subject getByName(String name);

    boolean existsById(long id);

    boolean existsByName(String name);
}
