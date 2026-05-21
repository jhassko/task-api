import { render, screen, fireEvent } from '@testing-library/vue'
import { describe, expect, it } from 'vitest'
import TaskForm from '../../src/components/TaskForm.vue'

describe('TaskForm', () => {
  it('emits a create payload', async () => {
    const { emitted } = render(TaskForm)

    await fireEvent.update(screen.getByLabelText('Title'), 'Submit assignment')
    await fireEvent.update(screen.getByLabelText('Description'), 'Finish the task app')
    await fireEvent.update(screen.getByLabelText('Due date/time'), '2026-05-25T17:00')
    await fireEvent.click(screen.getByRole('button', { name: /create task/i }))

    const submitEvents = emitted('submit') as unknown[][]

    expect(submitEvents).toHaveLength(1)
    expect(submitEvents[0][0]).toMatchObject({
      title: 'Submit assignment',
      description: 'Finish the task app',
      status: 'TODO'
    })
  })

  it('shows edit mode when editingTask is provided', () => {
    render(TaskForm, {
      props: {
        editingTask: {
          id: '1',
          title: 'Existing task',
          description: 'Existing description',
          status: 'TODO',
          dueDate: '2026-05-25T17:00:00Z',
          createdAt: '2026-05-21T10:00:00Z',
          updatedAt: '2026-05-21T10:00:00Z'
        }
      }
    })

    expect(screen.getByDisplayValue('Existing task')).toBeTruthy()
  })
})