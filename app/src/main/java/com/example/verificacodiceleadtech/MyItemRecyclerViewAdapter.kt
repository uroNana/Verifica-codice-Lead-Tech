package com.example.verificacodiceleadtech

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

import com.example.verificacodiceleadtech.databinding.FragmentChronologyItemBinding
import com.example.verificacodiceleadtech.entity.CodeEntry


class MyItemRecyclerViewAdapter(
    private val valuesLiveData: LiveData<List<CodeEntry>>
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder>() {
    private var values: List<CodeEntry> = emptyList()
    private val isEmpty: MutableLiveData<Boolean> = MutableLiveData(true)
    init {
        valuesLiveData.observeForever { codes ->
            values = codes
            isEmpty.value = codes.isEmpty()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentChronologyItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.idView.text = item.code
        holder.contentView.text = if (item.isValid) "Valido" else "Non valido"
        holder.imageView.setImageResource(if (item.isValid) R.drawable.baseline_check else R.drawable.baseline_clear)
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentChronologyItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val idView: TextView = binding.itemNumber
        val contentView: TextView = binding.content
        val imageView: ImageView = binding.imageView

        override fun toString(): String {
            return super.toString() + " '" + contentView.text + "'"
        }
    }
    fun registerEmptyStateObserver(owner: LifecycleOwner, observer: Observer<Boolean>) {
        isEmpty.observe(owner, observer)
    }

}