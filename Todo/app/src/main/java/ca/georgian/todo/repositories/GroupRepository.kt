package ca.georgian.todo.repositories

import androidx.lifecycle.LiveData
import ca.georgian.todo.dao.GroupDao
import ca.georgian.todo.model.Group

class GroupRepository(private val groupDao: GroupDao) {

    val getAllGroups: LiveData<List<Group>> = groupDao.getAll()

    suspend fun addGroup(group: Group) {
        groupDao.insertAll(group)
    }

    suspend fun deleteGroup(group: Group) {
        groupDao.delete(group)
    }

}