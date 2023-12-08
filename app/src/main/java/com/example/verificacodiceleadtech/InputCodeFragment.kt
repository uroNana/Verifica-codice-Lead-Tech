package com.example.verificacodiceleadtech

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class InputCodeFragment : Fragment() {

    companion object {
        fun newInstance() = InputCodeFragment()
    }

    private lateinit var viewModel: InputCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input_code, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(InputCodeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}