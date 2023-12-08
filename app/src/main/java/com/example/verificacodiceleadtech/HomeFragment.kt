package com.example.verificacodiceleadtech

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.verificacodiceleadtech.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFirst.setOnClickListener {
            val inputCode = "3-598-21507-X"
            val isCodeValid = codeCheckFunction(inputCode)

            if (isCodeValid) {
                codeIsValid(inputCode)
            } else {
                codeIsNotValid(inputCode)
            }
        }

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_scanner_fragment)
        }
    }

    private fun codeCheckFunction(inputCode: String): Boolean {
        val cleanedCode = inputCode.replace("-", "")
        if (cleanedCode.length != 10) {
            return false
        }
        var result = 0
        for (i in 0 until 10) {
            val char = cleanedCode[i]
            val value = if (char == 'X') 10 else Character.getNumericValue(char)
            result += value * (10 - i)
        }
        return result % 11 == 0
    }

    private fun codeIsValid(inputCode: String) {
        binding.cardViewHome.visibility = View.VISIBLE
        binding.textHomeCardNumber.text = inputCode
        binding.textHomeCardCheck.text = getString(R.string.valido)
        binding.imageHomeCardCheck.setImageResource(R.drawable.baseline_check)
    }

    private fun codeIsNotValid(inputCode: String) {
        binding.cardViewHome.visibility = View.VISIBLE
        binding.textHomeCardNumber.text = inputCode
        binding.textHomeCardCheck.text = getString(R.string.non_valido)
        binding.imageHomeCardCheck.setImageResource(R.drawable.baseline_clear)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
