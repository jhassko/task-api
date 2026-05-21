<script setup lang="ts">
import { computed, reactive, watch } from 'vue'
import type { Task, TaskStatus, CreateTaskPayload, UpdateTaskPayload } from '../types/task'

const props = defineProps<{
  editingTask?: Task | null
  submitting?: boolean
}>()

const emit = defineEmits<{
  submit: [payload: CreateTaskPayload | UpdateTaskPayload]
  cancelEdit: []
}>()

const form = reactive({
  title: '',
  description: '',
  status: 'TODO' as TaskStatus,
  dueDate: ''
})

const isEditing = computed(() => Boolean(props.editingTask))

watch(
  () => props.editingTask,
  task => {
    if (task) {
      form.title = task.title
      form.description = task.description ?? ''
      form.status = task.status
      form.dueDate = toDatetimeLocal(task.dueDate)
    } else {
      resetForm()
    }
  },
  { immediate: true }
)

function onSubmit() {
  if (!form.title.trim() || !form.dueDate) return

  const payload = {
    title: form.title.trim(),
    description: form.description.trim() || null,
    status: form.status,
    dueDate: new Date(form.dueDate).toISOString()
  }

  emit('submit', payload)

  if (!isEditing.value) {
    resetForm()
  }
}

function resetForm() {
  form.title = ''
  form.description = ''
  form.status = 'TODO'
  form.dueDate = ''
}

function cancelEdit() {
  emit('cancelEdit')
  resetForm()
}

function toDatetimeLocal(value: string) {
  const date = new Date(value)
  const offsetMs = date.getTimezoneOffset() * 60_000
  return new Date(date.getTime() - offsetMs).toISOString().slice(0, 16)
}
</script>

<template>
  <form class="task-form" @submit.prevent="onSubmit" aria-label="Task form">
    <h2>{{ isEditing ? 'Edit task' : 'Create task' }}</h2>

    <label>
      Title
      <input v-model="form.title" name="title" required placeholder="Task title" />
    </label>

    <label>
      Description
      <textarea v-model="form.description" name="description" placeholder="Optional description" />
    </label>

    <div class="form-row">
      <label>
        Status
        <select v-model="form.status" name="status">
          <option value="TODO">Todo</option>
          <option value="IN_PROGRESS">In progress</option>
          <option value="DONE">Done</option>
        </select>
      </label>

      <label>
        Due date/time
        <input v-model="form.dueDate" name="dueDate" type="datetime-local" required />
      </label>
    </div>

    <div class="actions">
      <button type="submit" :disabled="submitting">
        {{ submitting ? 'Saving...' : isEditing ? 'Save changes' : 'Create task' }}
      </button>
      <button v-if="isEditing" type="button" class="secondary" @click="cancelEdit">Cancel</button>
    </div>
  </form>
</template>
