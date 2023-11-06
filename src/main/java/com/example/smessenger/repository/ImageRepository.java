package com.example.smessenger.repository;

import com.example.smessenger.entity.Image;
import com.example.smessenger.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    Image findByImage(Byte[] image);
}
