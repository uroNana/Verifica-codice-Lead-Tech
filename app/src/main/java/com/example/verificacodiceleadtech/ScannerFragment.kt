package com.example.verificacodiceleadtech
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.io.IOException

class ScannerFragment : Fragment() {

    private lateinit var cameraSource: CameraSource
    private lateinit var cameraPreview: SurfaceView
    private val REQUEST_CAMERA_PERMISSION = 100
    private var isScanningEnabled = true

    companion object {
        fun newInstance() = ScannerFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scanner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cameraPreview = view.findViewById(R.id.cameraPreview)
        val buttonScan: Button = view.findViewById(R.id.button_scan)

        // Abilita la scansione quando il pulsante "Scansiona" viene premuto

        isScanningEnabled = true
        // Avvia la scansione quando il fragment viene creato
        startCameraSource()
    }

    override fun onResume() {
        super.onResume()
        // Ripristina la scansione quando il fragment è in primo piano
        if (isScanningEnabled) {
            startCameraSource()
        }
    }

    override fun onPause() {
        super.onPause()
        // Rilascia la telecamera quando il fragment è in background
        if (::cameraSource.isInitialized) {
            cameraSource.release()
        }
    }

    private fun startCameraSource() {
        val barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.ALL_FORMATS)
            .build()

        if (!barcodeDetector.isOperational) {
            Toast.makeText(
                requireContext(),
                "Errore durante l'inizializzazione del rilevatore di codici a barre",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(1600, 1024)
            .setAutoFocusEnabled(true)
            .build()

        cameraPreview.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    if (checkCameraPermission() && isScanningEnabled) {
                        cameraSource.start(holder)
                    } else {
                        requestCameraPermission()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })

        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {}

            override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                val barcodes: SparseArray<Barcode> = detections?.detectedItems ?: return

                if (barcodes.size() > 0) {
                    // Ottieni il primo codice a barre
                    val code = barcodes.valueAt(0).displayValue

                    // Verifica se il codice è composto da 10 cifre
                    //if (code.matches(Regex("\\d{10}"))) {
                        // Imposta il listener solo se la scansione è abilitata
                        if (isScanningEnabled) {
                            val buttonScan: Button? = view?.findViewById(R.id.button_scan)
                            buttonScan?.setOnClickListener {
                                activity?.runOnUiThread {
                                    onBarcodeScanned(code)
                                }
                            }

                            // Disabilita la scansione per evitare gestioni multiple dello stesso codice
                            isScanningEnabled = false
                        }
                    //}
                }
            }
        })

    }

    private fun onBarcodeScanned(barcode: String) {
        // Disabilita la scansione dopo aver rilevato il codice
        isScanningEnabled = false
        // Esegue la navigazione alla HomeFragment
        val action = ScannerFragmentDirections.actionScannerFragmentToHomeFragment(barcode)
        findNavController().navigate(action)
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
}

