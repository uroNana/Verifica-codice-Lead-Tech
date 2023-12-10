package com.example.verificacodiceleadtech

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.example.verificacodiceleadtech.databinding.FragmentHomeBinding
import android.Manifest
import androidx.room.Room
import com.example.verificacodiceleadtech.repository.CodeDatabase
import com.example.verificacodiceleadtech.repository.entity.CodeEntry
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CAMERA_PERMISSION = 100
    private lateinit var database: CodeDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Room.databaseBuilder(requireContext(), CodeDatabase::class.java, "app-database").build()

        val inputCode = arguments?.getString("inputCode")

        if (inputCode != null) {
            val isCodeValid = codeCheckFunction(inputCode)
            saveScannedCodeToDatabase(inputCode, isCodeValid)
            if (isCodeValid) {
                showCodeDetails(inputCode, R.string.valid, R.drawable.baseline_check)
            } else {
                showCodeDetails(inputCode, R.string.not_valid, R.drawable.baseline_clear)
            }
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_home_fragment_to_input_code_fragment)
        }
        binding.buttonSecond.setOnClickListener {
            if (checkCameraPermission()) {
                findNavController().navigate(R.id.action_home_fragment_to_scanner_fragment)
            } else {
                requestCameraPermission()
            }
        }
    }

    private fun saveScannedCodeToDatabase(code: String, isValid: Boolean) {
        val scannedCode = CodeEntry(code = code, isValid = isValid)
        GlobalScope.launch(Dispatchers.IO) {
            database.codeEntryDao().insert(scannedCode)
        }
    }
    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            REQUEST_CAMERA_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findNavController().navigate(R.id.action_home_fragment_to_scanner_fragment)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Il permesso per la fotocamera è necessario per lo scanner",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun codeCheckFunction(inputCode: String): Boolean {
        val cleanedCode = inputCode.replace("[\\-–]".toRegex(), "")
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

    private fun showCodeDetails(inputCode: String, messageResId: Int, imageResId: Int) {
        binding.cardViewHome.visibility = View.VISIBLE
        binding.textHomeCardNumber.text = inputCode
        binding.textHomeCardCheck.text = getString(messageResId)
        binding.imageHomeCardCheck.setImageResource(imageResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}