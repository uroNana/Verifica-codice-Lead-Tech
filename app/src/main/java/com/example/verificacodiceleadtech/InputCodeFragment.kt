package com.example.verificacodiceleadtech

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.verificacodiceleadtech.databinding.FragmentInputCodeBinding

class InputCodeFragment : Fragment() {

    private lateinit var binding: FragmentInputCodeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInputCodeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.buttonScan.setOnClickListener {
            super.onViewCreated(view, savedInstanceState)

            val inputCode = getEnteredCode()

            val bundle = Bundle().apply {
                putString("inputCode", inputCode)
            }
            findNavController().navigate(R.id.action_inputCodeFragment_to_Home, bundle)
        }
    }
    fun getEnteredCode(): String {
        return binding.editText.text.toString()
    }
}
