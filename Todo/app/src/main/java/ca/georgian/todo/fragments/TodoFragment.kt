package ca.georgian.todo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ca.georgian.todo.R
import ca.georgian.todo.adapter.GroupAdapter
import ca.georgian.todo.adapter.TodoAdapter
import ca.georgian.todo.model.Group
import ca.georgian.todo.model.Todo
import ca.georgian.todo.viewmodels.TodoViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class TodoFragment : Fragment() {

    private val args by navArgs<TodoFragmentArgs>()

    private lateinit var todoViewModel: TodoViewModel
    private lateinit var todos: List<Todo>
    private lateinit var adapter: TodoAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyTextView: TextView
    private var searchText: String = ""

    private var factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TodoViewModel(
                activity!!.application,
                args.group.uid
            ) as T
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todo, container, false)
        setupRecyclerView(view)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = args.group.name
        this.emptyTextView = view.findViewById(R.id.emptyTextView)
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
        return view
    }

    private fun setupRecyclerView(view: View) {
        todoViewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]

        view.findViewById<FloatingActionButton>(R.id.add_btn).setOnClickListener {
            val action =
                TodoFragmentDirections.actionTodoFragmentToTodoDetailFragment(args.group, null)
            view.findNavController().navigate(action)
        }

        recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        this.adapter = TodoAdapter(args.group)
        recyclerView.adapter = adapter

        todoViewModel.todos.observe(viewLifecycleOwner) { todos ->
            run {
                this.todos = todos
                updateAdapter()
            }
        }
    }

    private fun updateAdapter() {
        var todos = this.todos
        todos = todos.filter { todo -> todo.name.contains(this.searchText, true) }
        if (todos.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE
            recyclerView.visibility = View.GONE
        } else {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        adapter.setData(todos)
    }
}