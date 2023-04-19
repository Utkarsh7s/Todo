package ca.georgian.todo.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ca.georgian.todo.R
import ca.georgian.todo.fragments.GroupFragment
import ca.georgian.todo.fragments.GroupFragmentDirections
import ca.georgian.todo.model.Group
import ca.georgian.todo.viewmodels.GroupViewModel

class GroupAdapter() :
    RecyclerView.Adapter<GroupAdapter.ViewHolder>() {

    private lateinit var groupViewModel: GroupViewModel
    private lateinit var fragment: Fragment
    private var groups = emptyList<Group>()

    constructor(fragment: Fragment) : this() {
        this.fragment = fragment
        groupViewModel = ViewModelProvider(fragment)[GroupViewModel::class.java]
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.group_item_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currGroup = groups[position]

        // sets the text to the textview from our itemHolder class
        holder.textView.text = currGroup.name

        holder.container.setOnClickListener {
            val action = GroupFragmentDirections.actionGroupFragmentToTodoFragment(
                currGroup
            )
            holder.itemView.findNavController().navigate(action)
        }

        holder.container.setOnLongClickListener {
            val builder = AlertDialog.Builder(fragment.activity)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    groupViewModel.deleteGroup(currGroup)
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
            return@setOnLongClickListener true
        }
    }


    override fun getItemCount(): Int {
        return groups.size
    }


    class ViewHolder(groupView: View) : RecyclerView.ViewHolder(groupView) {
        val textView: TextView = groupView.findViewById(R.id.textview)
        val container: LinearLayout = groupView.findViewById(R.id.container)
    }

    fun setData(groups: List<Group>) {
        this.groups = groups
        notifyDataSetChanged()
    }
}