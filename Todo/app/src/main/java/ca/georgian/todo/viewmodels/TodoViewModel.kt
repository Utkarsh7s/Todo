package ca.georgian.todo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ca.georgian.todo.database.AppDatabase
import ca.georgian.todo.model.Todo
import ca.georgian.todo.repositories.GroupRepository
import ca.georgian.todo.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoViewModel(application: Application, groupId: Int) : AndroidViewModel(application) {

    val todos: LiveData<List<Todo>>
    private val repository: TodoRepository

    init {
        val todoDao = AppDatabase.getInstance(application).todoDao()
        repository = TodoRepository(todoDao, groupId)
        todos = repository.getAllTodos
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todo)
        }
    }
}