package com.example.verificacodiceleadtech

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.verificacodiceleadtech.chronology.ChronologyViewModel
import com.example.verificacodiceleadtech.chronology.MyItemRecyclerViewAdapter
import com.example.verificacodiceleadtech.repository.CodeDatabase


class ChronologyItemFragment : Fragment() {

    private lateinit var database: CodeDatabase
    private lateinit var chronologyViewModel: ChronologyViewModel
    private lateinit var adapter: MyItemRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        database = Room.databaseBuilder(
            requireContext(),
            CodeDatabase::class.java,
            "app-database"
        ).build()

        val view = inflater.inflate(R.layout.fragment_chronology_item_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.list)

        recyclerView.layoutManager = LinearLayoutManager(context).apply {
            reverseLayout = true
            stackFromEnd = true
        }
        chronologyViewModel = ViewModelProvider(this).get(ChronologyViewModel::class.java)

        chronologyViewModel.getAllCodeEntries().observe(viewLifecycleOwner) { codeEntries ->
            codeEntries?.let {
                adapter.setValues(it)
                updateEmptyTextViewVisibility(adapter)
            }
        }

        adapter = MyItemRecyclerViewAdapter(chronologyViewModel.getAllCodeEntries())
        recyclerView.adapter = adapter


        return view
    }

    private fun updateEmptyTextViewVisibility(adapter: MyItemRecyclerViewAdapter) {
        val emptyTextView: TextView = requireView().findViewById(R.id.empy_text_view)
        emptyTextView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
    }
}
