package com.challenge.tasks.controller;

import com.challenge.tasks.dto.CreateTaskRequest;
import com.challenge.tasks.model.TaskStatus;
import com.challenge.tasks.repository.TaskRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.OffsetDateTime;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:tasks-test;DB_CLOSE_DELAY=-1;MODE=PostgreSQL",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TaskRepository taskRepository;

    @Test
    void createAndRetrieveTask() throws Exception {
        taskRepository.deleteAll();

        String body = objectMapper.writeValueAsString(new CreateTaskRequest(
                "Submit assignment",
                "Finish implementation",
                TaskStatus.TODO,
                OffsetDateTime.parse("2026-05-25T17:00:00Z")
        ));

        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Submit assignment"))
                .andExpect(jsonPath("$.status").value("TODO"))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void getAllTasksReturnsList() throws Exception {
        taskRepository.deleteAll();
        createTask("One");
        createTask("Two");

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void createRejectsMissingTitle() throws Exception {
        String body = objectMapper.writeValueAsString(Map.of(
                "description", "No title",
                "status", "TODO",
                "dueDate", "2026-05-25T17:00:00Z"
        ));

        mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void updateStatusWorks() throws Exception {
        taskRepository.deleteAll();
        String id = createTask("Status task");

        mockMvc.perform(patch("/api/tasks/{id}/status", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DONE\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DONE"));
    }

    @Test
    void deleteTaskWorks() throws Exception {
        taskRepository.deleteAll();
        String id = createTask("Delete task");

        mockMvc.perform(delete("/api/tasks/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/tasks/{id}", id))
                .andExpect(status().isNotFound());
    }

    private String createTask(String title) throws Exception {
        String body = objectMapper.writeValueAsString(new CreateTaskRequest(
                title,
                null,
                TaskStatus.TODO,
                OffsetDateTime.parse("2026-05-25T17:00:00Z")
        ));
        String response = mockMvc.perform(post("/api/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readTree(response).get("id").asText();
    }
}
