package com.example.burningkey.texts.repository;

import com.example.burningkey.texts.entity.Text;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {
    long count();

    List<Text> findTextsByLanguage(String language);
    List<Text> findTextsByDifficulty(String difficulty);
    List<Text> findTextsByLanguageAndDifficulty(String language, String difficulty);
}
