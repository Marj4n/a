package com.marj4n.ujikomvsga

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.marj4n.ujikomvsga.helper.DbHelper

class InputActivity : AppCompatActivity() {
    private lateinit var idEt: TextInputEditText
    private lateinit var nameEt: TextInputEditText
    private lateinit var priceEt: TextInputEditText
    private lateinit var qtyEt: TextInputEditText
    private lateinit var buttonSubmit: MaterialButton
    private lateinit var buttonReset: MaterialButton
    private lateinit var dbHelper: DbHelper
    private var isUpdateMode = false
    private var barangIdToUpdate: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input)

        dbHelper = DbHelper(this)

        idEt = findViewById(R.id.idEt)
        nameEt = findViewById(R.id.nameEt)
        priceEt = findViewById(R.id.priceEt)
        qtyEt = findViewById(R.id.qtyEt)
        buttonSubmit = findViewById(R.id.buttonSubmit)
        buttonReset = findViewById(R.id.buttonReset)

        this.actionBar?.setDisplayHomeAsUpEnabled(true)

        // Check if intent contains data for update
        if (intent.hasExtra("barang_id")) {
            isUpdateMode = true
            barangIdToUpdate = intent.getIntExtra("barang_id", -1)
            loadBarangData(barangIdToUpdate)
            idEt.isEnabled = false
        }

        buttonSubmit.setOnClickListener {
            val id = idEt.text.toString().trim()
            val name = nameEt.text.toString().trim()
            val priceStr = priceEt.text.toString().trim()
            val qtyStr = qtyEt.text.toString().trim()

            var isValid = true

            validateInput(id, "ID tidak boleh kosong", findViewById(R.id.idLayout))
            validateInput(name, "Nama tidak boleh kosong", findViewById(R.id.nameLayout))
            validateInput(priceStr, "Harga tidak boleh kosong", findViewById(R.id.priceLayout))
            validateInput(qtyStr, "Qty tidak boleh kosong", findViewById(R.id.qtyLayout))

            if (findViewById<TextInputLayout>(R.id.idLayout).error != null ||
                findViewById<TextInputLayout>(R.id.nameLayout).error != null ||
                findViewById<TextInputLayout>(R.id.priceLayout).error != null ||
                findViewById<TextInputLayout>(R.id.qtyLayout).error != null
            ) {
                isValid = false
            }

            if (isValid) {
                if (isUpdateMode) {
                    // Update existing barang
                    val price = priceStr.toDouble()
                    val qty = qtyStr.toInt()

                    val updated = dbHelper.update(barangIdToUpdate, name, price, qty)

                    if (updated > 0) {
                        Toast.makeText(this, "Data barang berhasil diperbarui", Toast.LENGTH_SHORT)
                            .show()
                        // Redirect back to MainActivity
                        onBackPressed()
                    } else {
                        Toast.makeText(this, "Gagal memperbarui data barang", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    // Insert new barang
                    if (dbHelper.isIdExists(id.toInt())) {
                        setError(findViewById(R.id.idLayout), "ID sudah ada")
                    } else {
                        val price = priceStr.toDouble()
                        val qty = qtyStr.toInt()

                        val insertedId = dbHelper.insert(id.toInt(), name, price, qty)

                        if (insertedId > 0) {
                            Toast.makeText(
                                this,
                                "Data barang berhasil disimpan",
                                Toast.LENGTH_SHORT
                            ).show()
                            // Redirect back to MainActivity
//                            onBackPressed()
                        } else {
                            Toast.makeText(this, "Gagal menyimpan data barang", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        buttonReset.setOnClickListener {
            // Clear all EditText fields
            idEt.text = null
            nameEt.text = null
            priceEt.text = null
            qtyEt.text = null

            // Clear errors in TextInputLayouts
            clearError(findViewById(R.id.idLayout))
            clearError(findViewById(R.id.nameLayout))
            clearError(findViewById(R.id.priceLayout))
            clearError(findViewById(R.id.qtyLayout))
        }
    }

    private fun loadBarangData(barangId: Int) {
        val barang = dbHelper.isIdExists(barangId)
        if (barang != null) {
            val barang = dbHelper.getAllData().find { it.id == barangId }
            if (barang != null) {
                idEt.setText(barang.id.toString())
                nameEt.setText(barang.nama)
                priceEt.setText(barang.harga.toString())
                qtyEt.setText(barang.jumlah.toString())
            }
        } else {
            Toast.makeText(this, "Data barang tidak ditemukan", Toast.LENGTH_SHORT).show()
            onBackPressed()
        }
    }

    private fun validateInput(value: String, message: String, layout: TextInputLayout) {
        if (value.isEmpty()) {
            setError(layout, message)
        } else {
            clearError(layout)
        }
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun clearError(layout: TextInputLayout) {
        layout.error = null
        layout.boxStrokeColor = resources.getColor(R.color.blue)
        layout.hintTextColor = resources.getColorStateList(R.color.blue)
    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    private fun setError(layout: TextInputLayout, errorMessage: String) {
        layout.error = errorMessage
        layout.boxStrokeColor = resources.getColor(android.R.color.holo_red_dark)
        layout.hintTextColor = resources.getColorStateList(android.R.color.holo_red_dark)
    }
}