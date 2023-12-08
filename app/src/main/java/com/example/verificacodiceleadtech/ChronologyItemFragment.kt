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
import com.example.verificacodiceleadtech.placeholder.PlaceholderContent


class ChronologyItemFragment : Fragment() {

    private var columnCount = 2

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
        val view = inflater.inflate(R.layout.fragment_chronology_item_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.list)
        val emptyTextView: TextView = view.findViewById(R.id.empy_text_view)

        recyclerView.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }

        val adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)
        recyclerView.adapter = adapter

        emptyTextView.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE

        return view
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
