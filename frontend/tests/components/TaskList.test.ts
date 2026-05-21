import { render, screen, fireEvent } from '@testing-library/vue'
import { describe, expect, it } from 'vitest'
import TaskList from '../../src/components/TaskList.vue'
import type { Task } from '../../src/types/task'

describe('TaskList', () => {
  it('renders an empty state', () => {
    render(TaskList, {
      props: {
        tasks: []
      }
    })

    expect(screen.getByText(/no tasks yet/i)).toBeTruthy()
  })

  it('renders tasks and emits delete', async () => {
    const task: Task = {
      id: '1',
      title: 'Test task',
      description: 'Test description',
      status: 'TODO',
      dueDate: '2026-05-25T17:00:00Z',
      createdAt: '2026-05-21T10:00:00Z',
      updatedAt: '2026-05-21T10:00:00Z'
    }

    const { emitted } = render(TaskList, {
      props: {
        tasks: [task]
      }
    })

    await fireEvent.click(screen.getByRole('button', { name: /delete/i }))

    const deleteEvents = emitted('delete') as unknown[][]

    expect(deleteEvents).toHaveLength(1)
    expect(deleteEvents[0][0]).toEqual(task)
  })
})