package com.netcracker.denisik.dao;

import com.netcracker.denisik.entities.Student;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends CrudRepository<Student, Long> {

    Student getByWriteBookId(long writeBook_id);

    List<Student> getAllBySpecialityName(String speciality);

    List<Student> getAllBySpecialityFacultyName(String faculty);

    List<Student> getAllByGroupId(long groupId);

    boolean existsById(long id);
}
