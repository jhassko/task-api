<script setup lang="ts">
import type { Task, TaskStatus } from '../types/task'

const props = defineProps<{
  task: Task
}>()

const emit = defineEmits<{
  edit: [task: Task]
  delete: [task: Task]
  statusChange: [task: Task, status: TaskStatus]
}>()

function formatDate(value: string) {
  return new Intl.DateTimeFormat(undefined, {
    dateStyle: 'medium',
    timeStyle: 'short'
  }).format(new Date(value))
}

function statusLabel(status: TaskStatus) {
  return status.replace('_', ' ').toLowerCase()
}
</script>

<template>
  <article class="task-card">
    <div class="task-card-header">
      <div>
        <h3>{{ props.task.title }}</h3>
        <p class="due-date">Due {{ formatDate(props.task.dueDate) }}</p>
      </div>
      <span class="status-badge" :data-status="props.task.status">{{ statusLabel(props.task.status) }}</span>
    </div>

    <p v-if="props.task.description" class="description">{{ props.task.description }}</p>
    <p v-else class="description muted">No description provided.</p>

    <div class="task-actions">
      <label>
        Status
        <select
          :value="props.task.status"
          @change="emit('statusChange', props.task, ($event.target as HTMLSelectElement).value as TaskStatus)"
        >
          <option value="TODO">Todo</option>
          <option value="IN_PROGRESS">In progress</option>
          <option value="DONE">Done</option>
        </select>
      </label>
      <button type="button" class="secondary" @click="emit('edit', props.task)">Edit</button>
      <button type="button" class="danger" @click="emit('delete', props.task)">Delete</button>
    </div>
  </article>
</template>
