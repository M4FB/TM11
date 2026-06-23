package com.example.tmejerscan

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : AppCompatActivity() {

    private lateinit var scanAdapter: ScanAdapter
    private val scanHistory = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupRecyclerView()

        findViewById<Button>(R.id.btnScan).setOnClickListener {
            startScanning()
        }
    }

    private fun setupRecyclerView() {
        val rvHistory = findViewById<RecyclerView>(R.id.rvHistory)
        scanAdapter = ScanAdapter(scanHistory)
        rvHistory.layoutManager = LinearLayoutManager(this)
        rvHistory.adapter = scanAdapter
    }

    private fun startScanning() {
        val scanner = GmsBarcodeScanning.getClient(this)

        scanner.startScan()
            .addOnSuccessListener { barcode ->
                val rawValue: String? = barcode.rawValue
                if (rawValue != null) {
                    scanAdapter.addScan(rawValue)
                } else {
                    Toast.makeText(this, "No se pudo leer el código", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnCanceledListener {
                // El usuario canceló el escaneo
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
