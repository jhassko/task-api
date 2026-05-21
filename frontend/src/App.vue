<script setup lang="ts">
import { onMounted, ref } from 'vue'
import TaskForm from './components/TaskForm.vue'
import TaskList from './components/TaskList.vue'
import type { CreateTaskPayload, Task, TaskStatus, UpdateTaskPayload } from './types/task'
import { createTask, deleteTask, getTasks, updateTask, updateTaskStatus } from './api/tasks'

const tasks = ref<Task[]>([])
const loading = ref(false)
const submitting = ref(false)
const error = ref<string | null>(null)
const editingTask = ref<Task | null>(null)

onMounted(loadTasks)

async function loadTasks() {
  loading.value = true
  error.value = null
  try {
    tasks.value = await getTasks()
  } catch (err) {
    error.value = getErrorMessage(err)
  } finally {
    loading.value = false
  }
}

async function saveTask(payload: CreateTaskPayload | UpdateTaskPayload) {
  submitting.value = true
  error.value = null
  try {
    if (editingTask.value) {
      const updated = await updateTask(editingTask.value.id, payload as UpdateTaskPayload)
      tasks.value = tasks.value.map(task => task.id === updated.id ? updated : task)
      editingTask.value = null
    } else {
      const created = await createTask(payload as CreateTaskPayload)
      tasks.value = [created, ...tasks.value]
    }
  } catch (err) {
    error.value = getErrorMessage(err)
  } finally {
    submitting.value = false
  }
}

async function changeStatus(task: Task, status: TaskStatus) {
  if (task.status === status) return
  error.value = null
  try {
    const updated = await updateTaskStatus(task.id, status)
    tasks.value = tasks.value.map(existing => existing.id === updated.id ? updated : existing)
  } catch (err) {
    error.value = getErrorMessage(err)
  }
}

async function removeTask(task: Task) {
  const confirmed = window.confirm(`Delete task "${task.title}"?`)
  if (!confirmed) return

  error.value = null
  try {
    await deleteTask(task.id)
    tasks.value = tasks.value.filter(existing => existing.id !== task.id)
    if (editingTask.value?.id === task.id) editingTask.value = null
  } catch (err) {
    error.value = getErrorMessage(err)
  }
}

function getErrorMessage(err: unknown) {
  return err instanceof Error ? err.message : 'Something went wrong'
}
</script>

<template>
  <main class="app-shell">
    <header>
      <h1>Task Manager</h1>
      <p>Create, track, update, and delete your tasks.</p>
    </header>

    <p v-if="error" role="alert" class="error-message">{{ error }}</p>

    <TaskForm
      :editing-task="editingTask"
      :submitting="submitting"
      @submit="saveTask"
      @cancel-edit="editingTask = null"
    />

    <p v-if="loading" class="loading">Loading tasks...</p>
    <TaskList
      v-else
      :tasks="tasks"
      @edit="editingTask = $event"
      @delete="removeTask"
      @status-change="changeStatus"
    />
  </main>
</template>
