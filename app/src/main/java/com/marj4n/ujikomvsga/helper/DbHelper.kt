package com.marj4n.ujikomvsga.helper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.marj4n.ujikomvsga.model.Barang

class DbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
    companion object {
        private const val DB_NAME = "vsga_ujikom.db"
        private const val DB_VERSION = 1

        private const val TABLE_NAME = "barang"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAMA = "nama"
        private const val COLUMN_HARGA = "harga"
        private const val COLUMN_JUMLAH = "jumlah"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val sqlCreate = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER NOT NULL, " +
                "$COLUMN_NAMA TEXT NOT NULL, " +
                "$COLUMN_HARGA REAL NOT NULL, " +
                "$COLUMN_JUMLAH INTEGER NOT NULL, " +
                "PRIMARY KEY($COLUMN_ID))"

        db?.execSQL(sqlCreate)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insert(id: Int, nama: String, harga: Double, jumlah: Int): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_ID, id)
        values.put(COLUMN_NAMA, nama)
        values.put(COLUMN_HARGA, harga)
        values.put(COLUMN_JUMLAH, jumlah)
        val rowId = db.insert(TABLE_NAME, null, values)
        db.close()
        return rowId
    }

    fun update(id: Int, nama: String, harga: Double, jumlah: Int): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAMA, nama)
        values.put(COLUMN_HARGA, harga)
        values.put(COLUMN_JUMLAH, jumlah)
        val rowAffected = db.update(TABLE_NAME, values, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return rowAffected
    }

    fun delete(id: Int): Int {
        val db = this.writableDatabase
        val rowAffected = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(id.toString()))
        db.close()
        return rowAffected
    }

    fun isIdExists(id: Int): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(query, arrayOf(id.toString()))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }


    @SuppressLint("Range")
    fun getAllData(): ArrayList<Barang> {
        val data = ArrayList<Barang>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery(query, null)
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
            val nama = cursor.getString(cursor.getColumnIndex(COLUMN_NAMA))
            val harga = cursor.getDouble(cursor.getColumnIndex(COLUMN_HARGA))
            val jumlah = cursor.getInt(cursor.getColumnIndex(COLUMN_JUMLAH))
            data.add(Barang(id, nama, harga, jumlah))
        }
        cursor.close()
        db.close()
        return data
    }
}
