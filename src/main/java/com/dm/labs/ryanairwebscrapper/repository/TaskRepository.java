package com.dm.labs.ryanairwebscrapper.repository;

import com.dm.labs.ryanairwebscrapper.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
