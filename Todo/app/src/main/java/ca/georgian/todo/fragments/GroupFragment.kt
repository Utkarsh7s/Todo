package ca.georgian.todo.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.georgian.todo.R
import ca.georgian.todo.adapter.GroupAdapter
import ca.georgian.todo.model.Group
import ca.georgian.todo.viewmodels.GroupViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class GroupFragment : Fragment() {

    private lateinit var groupViewModel: GroupViewModel
    private lateinit var groups: List<Group>
    private lateinit var adapter: GroupAdapter
    private var searchText: String = ""
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_group, container, false)
        this.emptyTextView = view.findViewById(R.id.emptyTextView)

        groupViewModel = ViewModelProvider(this)[GroupViewModel::class.java]

        view.findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            showNewGroupDialogBox()
        }

        view.findViewById<SearchView>(R.id.searchView).setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                searchText = newText
                updateAdapter()
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                searchText = query
                updateAdapter()
                return false
            }

        })


        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = GroupAdapter(this)
        recyclerView.adapter = adapter

        groupViewModel.groups.observe(viewLifecycleOwner) { groups ->
            run {
                this.groups = groups
                updateAdapter()
            }
        }

        return view
    }

    private fun updateAdapter() {
        var groups = this.groups
        groups = groups.filter { group -> group.name.contains(this.searchText, true) }
        if (groups.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        adapter.setData(groups)
    }

    private fun showNewGroupDialogBox() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle("Title")

        val input = EditText(activity)
        input.hint = "Enter Text"
        input.inputType = InputType.TYPE_CLASS_TEXT
        builder.setView(input)
        builder.setPositiveButton("OK") { _, _ ->
            var text = input.text.toString()
            val group = Group(0, text)
            groupViewModel.addGroup(group)
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.cancel() }

        builder.show()
    }
}