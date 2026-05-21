import type { CreateTaskPayload, Task, TaskStatus, UpdateTaskPayload } from '../types/task'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/api'

async function request<T>(path: string, options: RequestInit = {}): Promise<T> {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers ?? {})
    },
    ...options
  })

  if (!response.ok) {
    let message = `Request failed with status ${response.status}`
    try {
      const errorBody = await response.json()
      message = errorBody.message ?? message
    } catch {
      // Ignore non-JSON error bodies.
    }
    throw new Error(message)
  }

  if (response.status === 204) {
    return undefined as T
  }

  return response.json() as Promise<T>
}

export function getTasks(): Promise<Task[]> {
  return request<Task[]>('/tasks')
}

export function createTask(payload: CreateTaskPayload): Promise<Task> {
  return request<Task>('/tasks', {
    method: 'POST',
    body: JSON.stringify(payload)
  })
}

export function updateTask(id: string, payload: UpdateTaskPayload): Promise<Task> {
  return request<Task>(`/tasks/${id}`, {
    method: 'PUT',
    body: JSON.stringify(payload)
  })
}

export function updateTaskStatus(id: string, status: TaskStatus): Promise<Task> {
  return request<Task>(`/tasks/${id}/status`, {
    method: 'PATCH',
    body: JSON.stringify({ status })
  })
}

export function deleteTask(id: string): Promise<void> {
  return request<void>(`/tasks/${id}`, {
    method: 'DELETE'
  })
}
