package com.challenge.tasks.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.challenge.tasks.dto.CreateTaskRequest;
import com.challenge.tasks.dto.UpdateStatusRequest;
import com.challenge.tasks.exception.TaskNotFoundException;
import com.challenge.tasks.model.Task;
import com.challenge.tasks.model.TaskStatus;
import com.challenge.tasks.repository.TaskRepository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    TaskRepository taskRepository;

    @InjectMocks
    TaskService taskService;

    @Test
    void createDefaultsStatusToTodo() {
        OffsetDateTime dueDate = OffsetDateTime.parse("2026-05-25T17:00:00Z");
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task task = invocation.getArgument(0);
            task.setId(UUID.randomUUID());
            task.setCreatedAt(OffsetDateTime.now());
            task.setUpdatedAt(OffsetDateTime.now());
            return task;
        });

        var response = taskService.create(new CreateTaskRequest(" Task ", " Description ", null, dueDate));

        assertThat(response.title()).isEqualTo("Task");
        assertThat(response.description()).isEqualTo("Description");
        assertThat(response.status()).isEqualTo(TaskStatus.TODO);
        assertThat(response.dueDate()).isEqualTo(dueDate);
    }

    @Test
    void updateStatusChangesOnlyStatus() {
        UUID id = UUID.randomUUID();
        Task task = sampleTask(id);
        when(taskRepository.findById(id)).thenReturn(Optional.of(task));
        when(taskRepository.save(task)).thenReturn(task);

        var response = taskService.updateStatus(id, new UpdateStatusRequest(TaskStatus.DONE));

        assertThat(response.status()).isEqualTo(TaskStatus.DONE);
        verify(taskRepository).save(task);
    }

    @Test
    void findByIdThrowsWhenMissing() {
        UUID id = UUID.randomUUID();
        when(taskRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.findById(id))
                .isInstanceOf(TaskNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    private Task sampleTask(UUID id) {
        Task task = new Task();
        task.setId(id);
        task.setTitle("Task");
        task.setDescription("Description");
        task.setStatus(TaskStatus.TODO);
        task.setDueDate(OffsetDateTime.parse("2026-05-25T17:00:00Z"));
        task.setCreatedAt(OffsetDateTime.now());
        task.setUpdatedAt(OffsetDateTime.now());
        return task;
    }
}
