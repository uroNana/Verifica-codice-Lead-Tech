package com.example.verificacodiceleadtech

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.verificacodiceleadtech.entity.CodeEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ChronologyItemFragment : Fragment() {

    private var columnCount = 1
    private lateinit var database: CodeDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

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
        val emptyTextView: TextView = view.findViewById(R.id.empy_text_view)

        val recyclerView: RecyclerView = view.findViewById(R.id.list)

        recyclerView.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }

        lifecycleScope.launch {
            val values = getScannedCodesFromDatabase()
            val adapter = MyItemRecyclerViewAdapter(values)
            recyclerView.adapter = adapter
            adapter.registerEmptyStateObserver(viewLifecycleOwner) { isEmpty ->
                emptyTextView.visibility = if (isEmpty) View.VISIBLE else View.GONE
            }
        }


        return view
    }

    private suspend fun getScannedCodesFromDatabase(): LiveData<List<CodeEntry>> {
        return withContext(Dispatchers.IO) {
            database.codeEntryDao().getAllCodeEntries()
        }
    }
    private fun updateEmptyTextViewVisibility(adapter: MyItemRecyclerViewAdapter) {
        val emptyTextView: TextView = requireView().findViewById(R.id.empy_text_view)
        emptyTextView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
    }

    // Funzione chiamata quando vengono aggiunti o rimossi elementi dalla RecyclerView
    private fun onDataChanged() {
        val recyclerView: RecyclerView = requireView().findViewById(R.id.list)
        val adapter = recyclerView.adapter as MyItemRecyclerViewAdapter
        updateEmptyTextViewVisibility(adapter)
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            ChronologyItemFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}
