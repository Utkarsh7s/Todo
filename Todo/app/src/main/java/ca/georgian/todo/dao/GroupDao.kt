package ca.georgian.todo.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import ca.georgian.todo.model.Group

@Dao
interface GroupDao {
    @Query("SELECT * FROM group_table order by name")
    fun getAll(): LiveData<List<Group>>

    @Insert
    fun insertAll(vararg groups: Group)

    @Delete
    fun delete(group: Group)
}