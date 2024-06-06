package com.example.burningkey.user_lessons.repository;

import com.example.burningkey.user_lessons.entity.UserLesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface UserLessonRepository extends JpaRepository<UserLesson, Long> {

    List<UserLesson> findAllByUser_Id(Long userId);
    List<UserLesson> findAllByDate(LocalDateTime date); // get all lessons of all users for leader board of particular day
    List<UserLesson> findAllByUser_IdAndDate(Long userId, LocalDateTime date);
}
