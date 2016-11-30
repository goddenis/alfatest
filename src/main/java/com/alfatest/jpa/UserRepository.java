package com.alfatest.jpa;

import com.alfatest.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByLogin(String login);

    User findByPassword(String password);

    Optional<User> findByLoginAndPassword(String login , String pass);
}
