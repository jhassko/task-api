<script setup lang="ts">
import TaskItem from './TaskItem.vue'
import type { Task, TaskStatus } from '../types/task'

const props = defineProps<{
  tasks: Task[]
}>()

const emit = defineEmits<{
  edit: [task: Task]
  delete: [task: Task]
  statusChange: [task: Task, status: TaskStatus]
}>()
</script>

<template>
  <section class="task-list" aria-label="Tasks">
    <p v-if="props.tasks.length === 0" class="empty-state">No tasks yet. Create your first task above.</p>
    <TaskItem
      v-for="task in props.tasks"
      :key="task.id"
      :task="task"
      @edit="emit('edit', $event)"
      @delete="emit('delete', $event)"
      @status-change="(task, status) => emit('statusChange', task, status)"
    />
  </section>
</template>
