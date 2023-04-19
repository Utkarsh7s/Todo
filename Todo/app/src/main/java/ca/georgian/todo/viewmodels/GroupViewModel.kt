package ca.georgian.todo.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import ca.georgian.todo.database.AppDatabase
import ca.georgian.todo.model.Group
import ca.georgian.todo.repositories.GroupRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupViewModel(application: Application) : AndroidViewModel(application) {

    val groups: LiveData<List<Group>>
    private val repository: GroupRepository

    init {
        val groupDao = AppDatabase.getInstance(application).groupDao()
        repository = GroupRepository(groupDao)
        groups = repository.getAllGroups
    }

    fun addGroup(group: Group) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addGroup(group)
        }
    }

    fun deleteGroup(group: Group) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteGroup(group)
        }
    }
}