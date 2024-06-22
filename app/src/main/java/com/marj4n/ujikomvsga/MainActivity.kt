package com.marj4n.ujikomvsga

import com.marj4n.ujikomvsga.adapter.Adapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.marj4n.ujikomvsga.helper.DbHelper
import com.marj4n.ujikomvsga.model.Barang

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DbHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var fab: FloatingActionButton
    private lateinit var barangAdapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DbHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        fab.setOnClickListener {
            val intent = Intent(this, InputActivity::class.java)
            startActivity(intent)
        }

        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val listBarang = dbHelper.getAllData()
        barangAdapter = Adapter(listBarang)
        barangAdapter.setListener(this) // Set listener untuk handle click dari com.marj4n.ujikomvsga.adapter.Adapter
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = barangAdapter
        }
    }

    fun onEditClick(barang: Barang) {
        // Tampilkan intent untuk edit barang
        val intent = Intent(this, InputActivity::class.java)
        intent.putExtra("barang_id", barang.id)
        startActivity(intent)
    }

    fun onDeleteClick(barang: Barang) {
        // Lakukan proses delete barang dari database
        val deleted = dbHelper.delete(barang.id)
        if (deleted > 0) {
            // Refresh RecyclerView setelah delete
            setupRecyclerView()
        } else {
            // Handle jika delete gagal
        }
    }
}