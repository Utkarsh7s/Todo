package ca.georgian.todo.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import ca.georgian.todo.R
import ca.georgian.todo.fragments.TodoFragmentDirections
import ca.georgian.todo.model.Group
import ca.georgian.todo.model.Todo
import java.util.*
import kotlin.math.ceil

class TodoAdapter :
    RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private var todos: List<Todo>
    private var group: Group

    constructor(group: Group) : super() {
        this.todos = emptyList()
        this.group = group
    }


    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.todo_item_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currTodo = todos[position]

        // sets the text to the textview from our itemHolder class
        holder.name.text = currTodo.name
        holder.description.text = currTodo.description
        if (currTodo.completed) {
            holder.container.setBackgroundColor(Color.rgb(0,128,0))
        } else if (currTodo.date != null) {
            val dateDiff = getDateDiff(Date(currTodo.date!!), Date())
            if (dateDiff < 0) {
                holder.container.setBackgroundColor(Color.RED)
            } else if (dateDiff == 0) {
                holder.container.setBackgroundColor(Color.rgb(255, 165, 0))
            } else if (dateDiff == 1) {
                holder.container.setBackgroundColor(Color.YELLOW)
            }
        }

        holder.container.setOnClickListener {
            val action =
                TodoFragmentDirections.actionTodoFragmentToTodoDetailFragment(this.group, currTodo)
            holder.itemView.findNavController().navigate(action)
        }
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return todos.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(groupView: View) : RecyclerView.ViewHolder(groupView) {
        val name: TextView = groupView.findViewById(R.id.name)
        val description: TextView = groupView.findViewById(R.id.description)
        val container: LinearLayout = groupView.findViewById(R.id.container)
    }

    fun setData(todos: List<Todo>) {
        this.todos = todos
        notifyDataSetChanged()
    }

    private fun getDateDiff(date1: Date, date2: Date): Int {
        val diff: Long = date1.time - date2.time
        val seconds: Double = (diff / 1000).toDouble()
        val minutes = seconds / 60
        val hours = minutes / 60
        return ceil(hours / 24f).toInt()
    }
}