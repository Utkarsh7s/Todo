package ca.georgian.todo.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import ca.georgian.todo.R
import ca.georgian.todo.model.Group
import ca.georgian.todo.model.Todo
import ca.georgian.todo.viewmodels.TodoViewModel
import java.util.*

class TodoDetailFragment : Fragment() {

    private val args by navArgs<TodoDetailFragmentArgs>()

    private lateinit var todoViewModel: TodoViewModel
    var date: Date? = null


    private var factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TodoViewModel(
                activity!!.application,
                args.group.uid
            ) as T
        }
    }

    private lateinit var nameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var dateTextView: TextView
    private lateinit var submitBtn: Button
    private lateinit var completedBtn: Button
    private lateinit var deleteBtn: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_todo_detail, container, false)
        setup(view)
        todoViewModel = ViewModelProvider(this, factory)[TodoViewModel::class.java]
        updateData()
        return view
    }

    private fun updateData() {
        var todo = args.todo
        if (todo == null) {
            deleteBtn.visibility = View.GONE
            completedBtn.visibility = View.GONE
            return
        }
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Update " + todo.name
        nameEditText.setText(todo.name)
        descriptionEditText.setText(todo.description)
        date = todo.date?.let { Date(it) }
        if (date != null) {
            dateTextView.text =
                "" + date!!.date + "/" + (date!!.month + 1) + "/" + (date!!.year + 1900)
        }
        submitBtn.text = "Update"

        submitBtn.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            if (name == "") {
                Toast.makeText(activity, "Name is required", Toast.LENGTH_LONG).show()
            } else {
                todo.name = name
                todo.description = description
                todo.date = date?.time
                todoViewModel.updateTodo(todo)
                findNavController().popBackStack()
            }
        }

        deleteBtn.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Are you sure you want to Delete?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    todoViewModel.deleteTodo(todo)
                    findNavController().popBackStack()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        val text = if (todo.completed) {
            "incomplete"
        } else {
            "completed"
        }
        completedBtn.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setMessage("Are you sure you want to mark as $text?")
                .setCancelable(false)
                .setPositiveButton("Yes") { _, _ ->
                    todo.completed = !todo.completed
                    todoViewModel.updateTodo(todo)
                    findNavController().popBackStack()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
            val alert = builder.create()
            alert.show()
        }

        completedBtn.text = "Mark as $text"
    }

    private fun setup(view: View) {
        nameEditText = view.findViewById(R.id.nameEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        dateTextView = view.findViewById(R.id.dateTextView)
        submitBtn = view.findViewById(R.id.submitBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)
        completedBtn = view.findViewById(R.id.completedBtn)

        submitBtn.setOnClickListener {
            val name = nameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            if (name == "") {
                Toast.makeText(activity, "Name is required", Toast.LENGTH_LONG).show()
            } else {
                val todo = Todo(0, name, description, date?.time, args.group.uid);
                todoViewModel.addTodo(todo)
                findNavController().popBackStack()
            }
        }

        dateTextView.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val dpd = DatePickerDialog(
                requireActivity(),
                { _, year, monthOfYear, dayOfMonth ->
                    date = Date(year - 1900, month, dayOfMonth)
                    dateTextView.text = "" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year
                },
                year,
                month,
                day
            )
            dpd.datePicker.minDate = System.currentTimeMillis() - 1000
            dpd.show()
        }
    }
}