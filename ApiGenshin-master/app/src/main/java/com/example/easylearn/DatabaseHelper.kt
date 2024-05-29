package com.example.easylearn

import android.annotation.SuppressLint
import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "favorites.db"
        private const val TABLE_NAME = "favorites"
        private const val TABLE_FAVORITES = "favorites"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_WEAPON = "weapon"
        private const val COLUMN_VISION = "vision"
        private const val COLUMN_IS_FAVORITE = "is_favorite"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_NAME TEXT, $COLUMN_IS_FAVORITE INTEGER)"
        db?.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Aquí puedes manejar la migración de datos si es necesario
    }

    fun addFavorite(id: Int, name: String) {
        val values = ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_NAME, name)
            put(COLUMN_IS_FAVORITE, 1) // Marcar como favorito
        }
        writableDatabase.insert(TABLE_NAME, null, values)
    }

    @SuppressLint("Range")
    fun getAllFavorites(): List<User> {
        val favoritesList = mutableListOf<User>()
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT $COLUMN_ID, $COLUMN_NAME FROM $TABLE_FAVORITES", null)
        cursor.use { cursor ->
            while (cursor.moveToNext()) {
                val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                favoritesList.add(User(id, name, "", "")) // No necesitas las otras columnas
            }
        }
        return favoritesList
    }


}
