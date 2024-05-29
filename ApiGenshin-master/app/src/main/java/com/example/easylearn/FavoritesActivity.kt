package com.example.easylearn

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class FavoritesActivity : AppCompatActivity() {
    private val favoritesList = arrayListOf<User>()
    private lateinit var favoritesAdapter: UserAdapter
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerViewFavorites: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        dbHelper = DatabaseHelper(this) // Inicializa dbHelper aquí

        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites)
        favoritesAdapter = UserAdapter(favoritesList)
        recyclerViewFavorites.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = favoritesAdapter
        }

        val buttonBack = findViewById<Button>(R.id.buttonBack)
        buttonBack.setOnClickListener {
            finish() // Cierra la actividad y vuelve a la página principal
        }
    }

    override fun onResume() {
        super.onResume()
        favoritesList.clear()
        val favoritesFromDB: List<User> = dbHelper.getAllFavorites()
        if (favoritesFromDB.isNotEmpty()) {
            favoritesList.addAll(favoritesFromDB)
            recyclerViewFavorites.visibility = View.VISIBLE
        } else {
            // Si no hay personajes marcados como favoritos, mostrar un mensaje de "No favourite characters"
            val noFavoritesTextView = TextView(this)
            noFavoritesTextView.text = "No favourite characters"
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.gravity = Gravity.CENTER
            noFavoritesTextView.layoutParams = layoutParams
            (recyclerViewFavorites.parent as ViewGroup).addView(noFavoritesTextView)
            recyclerViewFavorites.visibility = View.GONE
        }
        favoritesAdapter.notifyDataSetChanged()
    }
}
