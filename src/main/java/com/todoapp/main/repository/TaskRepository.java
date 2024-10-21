package com.todoapp.main.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.todoapp.main.entity.Task;
import com.todoapp.main.entity.User;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByUser(User user);

    Task findByIdAndUser(Long id, User user);

    Page<Task> findAll(Pageable pageable);

    List<Task> findAll(Sort sort);

}
