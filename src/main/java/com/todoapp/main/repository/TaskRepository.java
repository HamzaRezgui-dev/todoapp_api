package com.todoapp.main.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todoapp.main.entity.Task;
import com.todoapp.main.entity.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findByIdAndUser(Long id, User user);

    Page<Task> findByUser(Pageable pageable, User user);

    Page<Task> findByUserAndDescriptionOrTitleContaining(Pageable pageable, User user, String title,String description);

}
