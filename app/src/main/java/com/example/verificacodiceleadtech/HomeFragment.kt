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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.verificacodiceleadtech.repository.CodeRepository
import com.example.verificacodiceleadtech.home.HomeViewModel
import com.example.verificacodiceleadtech.home.HomeViewModelFactory
import com.example.verificacodiceleadtech.home.ScannedCodeDetails


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val REQUEST_CAMERA_PERMISSION = 100
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(
            this,
            HomeViewModelFactory(CodeRepository((requireActivity().application as MyApp).database.codeEntryDao()))
        ).get(HomeViewModel::class.java)


        homeViewModel.scannedCodeDetails.observe(viewLifecycleOwner, Observer { scannedCodeDetails ->
            showCodeDetails(scannedCodeDetails)
        })

        val inputCode = arguments?.getString("inputCode")

        if (inputCode != null) {
           homeViewModel.processScannedCode(inputCode)

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

    private fun showCodeDetails(scannedCodeDetails: ScannedCodeDetails) {
        binding.cardViewHome.visibility = View.VISIBLE
        binding.textHomeCardNumber.text = scannedCodeDetails.code
        binding.textHomeCardCheck.text = getString(scannedCodeDetails.messageResId)
        binding.imageHomeCardCheck.setImageResource(scannedCodeDetails.imageResId)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
