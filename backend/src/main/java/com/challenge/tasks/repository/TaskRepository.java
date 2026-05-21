package com.challenge.tasks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.challenge.tasks.model.Task;

import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {
}
