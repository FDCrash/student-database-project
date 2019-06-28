package com.netcracker.denisik.dao;

import com.netcracker.denisik.entities.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User getByLogin(String login);

    boolean existsByLogin(String login);

    boolean existsById(long id);
}
