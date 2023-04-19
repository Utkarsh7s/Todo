package ca.georgian.todo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ca.georgian.todo.dao.GroupDao
import ca.georgian.todo.dao.TodoDao
import ca.georgian.todo.model.Group
import ca.georgian.todo.model.Todo

@Database(entities = [Group::class, Todo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(ctx: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance =
                    Room.databaseBuilder(ctx.applicationContext, AppDatabase::class.java, "todo")
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}