package com.challenge.tasks.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.challenge.tasks.dto.CreateTaskRequest;
import com.challenge.tasks.dto.TaskResponse;
import com.challenge.tasks.dto.UpdateStatusRequest;
import com.challenge.tasks.dto.UpdateTaskRequest;
import com.challenge.tasks.exception.TaskNotFoundException;
import com.challenge.tasks.model.Task;
import com.challenge.tasks.model.TaskStatus;
import com.challenge.tasks.repository.TaskRepository;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional
    public TaskResponse create(CreateTaskRequest request) {
        Task task = new Task();
        task.setTitle(request.title().trim());
        task.setDescription(normalizeDescription(request.description()));
        task.setStatus(request.status() == null ? TaskStatus.TODO : request.status());
        task.setDueDate(request.dueDate());
        return toResponse(taskRepository.save(task));
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> findAll() {
        return taskRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse findById(UUID id) {
        return toResponse(getTask(id));
    }

    @Transactional
    public TaskResponse update(UUID id, UpdateTaskRequest request) {
        Task task = getTask(id);
        task.setTitle(request.title().trim());
        task.setDescription(normalizeDescription(request.description()));
        task.setStatus(request.status());
        task.setDueDate(request.dueDate());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public TaskResponse updateStatus(UUID id, UpdateStatusRequest request) {
        Task task = getTask(id);
        task.setStatus(request.status());
        return toResponse(taskRepository.save(task));
    }

    @Transactional
    public void delete(UUID id) {
        Task task = getTask(id);
        taskRepository.delete(task);
    }

    private Task getTask(UUID id) {
        return taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
    }

    private String normalizeDescription(String description) {
        if (description == null || description.isBlank()) {
            return null;
        }
        return description.trim();
    }

    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
