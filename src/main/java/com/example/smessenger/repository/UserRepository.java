package com.example.smessenger.repository;

import com.example.smessenger.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Set<Users> findAllByUsernameStartsWith(String username);

    Optional<Users> findByLogin(String login);
}
