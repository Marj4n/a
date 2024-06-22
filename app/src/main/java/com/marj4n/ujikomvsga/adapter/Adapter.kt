package com.marj4n.ujikomvsga.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.marj4n.ujikomvsga.MainActivity
import com.marj4n.ujikomvsga.R
import com.marj4n.ujikomvsga.model.Barang

class Adapter(private val listBarang: List<Barang>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

    private lateinit var listener: MainActivity

    fun setListener(listener: MainActivity) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val barang = listBarang[position]
        holder.bind(barang)
    }

    override fun getItemCount(): Int {
        return listBarang.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textId: TextView = itemView.findViewById(R.id.textId)
        private val textNama: TextView = itemView.findViewById(R.id.textNama)
        private val textHarga: TextView = itemView.findViewById(R.id.textHarga)
        private val textJumlah: TextView = itemView.findViewById(R.id.textJumlah)

        @SuppressLint("SetTextI18n")
        fun bind(barang: Barang) {
            textId.text = barang.id.toString()
            textNama.text = barang.nama
            textHarga.text = "Rp ${barang.harga}"
            textJumlah.text = barang.jumlah.toString()

            itemView.setOnLongClickListener {
                showEditDeleteDialog(barang) // Tampilkan dialog edit/delete saat long click
                true
            }

            // Tambahkan listener untuk click biasa (jika diperlukan)
            itemView.setOnClickListener {
                // Implementasikan aksi untuk click biasa di sini
            }
        }

        private fun showEditDeleteDialog(barang: Barang) {
            val dialogBuilder = AlertDialog.Builder(itemView.context)
            dialogBuilder.setTitle("Pilihan")
            dialogBuilder.setMessage("Edit atau Delete barang ini?")
            dialogBuilder.setPositiveButton("Edit") { _, _ ->
                listener.onEditClick(barang)
            }
            dialogBuilder.setNegativeButton("Delete") { _, _ ->
                listener.onDeleteClick(barang)
            }
            val dialog = dialogBuilder.create()
            dialog.show()
        }
    }
}
