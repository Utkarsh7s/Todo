package ca.georgian.todo.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(
    tableName = "todo",
    foreignKeys = [ForeignKey(
        entity = Group::class,
        parentColumns = arrayOf("uid"),
        childColumns = arrayOf("group_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Todo(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "description") var description: String?,
    @ColumnInfo(name = "date") var date: Long?,
    @ColumnInfo(index = true) val group_id: Int,
    @ColumnInfo(name = "completed") var completed: Boolean = false
) : Parcelable