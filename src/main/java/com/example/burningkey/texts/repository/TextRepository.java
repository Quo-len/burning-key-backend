package com.example.burningkey.texts.repository;

import com.example.burningkey.texts.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {
}
