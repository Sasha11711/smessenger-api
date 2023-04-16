package com.example.smessenger.repository;

import com.example.smessenger.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    List<Users> findAllByUsernameStartsWith(String username);

    Optional<Users> findByLogin(String login);
}
