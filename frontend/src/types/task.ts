export type TaskStatus = 'TODO' | 'IN_PROGRESS' | 'DONE'

export interface Task {
  id: string
  title: string
  description: string | null
  status: TaskStatus
  dueDate: string
  createdAt: string
  updatedAt: string
}

export interface CreateTaskPayload {
  title: string
  description?: string | null
  status?: TaskStatus
  dueDate: string
}

export interface UpdateTaskPayload {
  title: string
  description?: string | null
  status: TaskStatus
  dueDate: string
}
