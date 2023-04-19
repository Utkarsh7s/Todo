package ca.georgian.todo.repositories

import androidx.lifecycle.LiveData
import ca.georgian.todo.dao.GroupDao
import ca.georgian.todo.dao.TodoDao
import ca.georgian.todo.model.Group
import ca.georgian.todo.model.Todo

class TodoRepository(private val todoDao: TodoDao, private val groupId: Int) {

    val getAllTodos: LiveData<List<Todo>> = todoDao.getAll(groupId)

    suspend fun addTodo(todo: Todo) {
        todoDao.insertAll(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.delete(todo)
    }

}