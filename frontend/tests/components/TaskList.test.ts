import { fireEvent, render, screen } from '@testing-library/vue'
import { describe, expect, it } from 'vitest'
import TaskList from '../../src/components/TaskList.vue'

const task = {
  id: '1',
  title: 'Test task',
  description: 'Task description',
  status: 'TODO' as const,
  dueDate: '2026-05-25T17:00:00.000Z',
  createdAt: '2026-05-20T10:00:00.000Z',
  updatedAt: '2026-05-20T10:00:00.000Z'
}

describe('TaskList', () => {
  it('renders an empty state', () => {
    render(TaskList, { props: { tasks: [] } })
    expect(screen.getByText('No tasks yet. Create your first task above.')).toBeInTheDocument()
  })

  it('renders tasks and emits delete', async () => {
    const { emitted } = render(TaskList, { props: { tasks: [task] } })

    expect(screen.getByText('Test task')).toBeInTheDocument()
    await fireEvent.click(screen.getByRole('button', { name: 'Delete' }))

    expect(emitted().delete[0][0]).toEqual(task)
  })
})
