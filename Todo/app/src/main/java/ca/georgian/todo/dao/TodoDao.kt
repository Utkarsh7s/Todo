package ca.georgian.todo.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ca.georgian.todo.model.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM todo where group_id = :groupId order by name")
    fun getAll(groupId: Int): LiveData<List<Todo>>

    @Insert
    fun insertAll(vararg todos: Todo)

    @Update
    fun update(vararg todo: Todo)

    @Delete
    fun delete(todo: Todo)
}