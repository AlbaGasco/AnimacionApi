package com.example.easylearn

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.content.Intent
import android.util.Log

import android.database.sqlite.SQLiteDatabase

class MainActivity : AppCompatActivity() {
    private val userList = arrayListOf<User>()
    private lateinit var userAdapter: UserAdapter

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        userAdapter = UserAdapter(userList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = userAdapter

        val buttonFavorites = findViewById<Button>(R.id.buttonFavorites)
        buttonFavorites.setOnClickListener {
            startActivity(Intent(this@MainActivity, FavoritesActivity::class.java))
        }

        fetchCharacters()

        userAdapter.setOnItemClickListener(object : UserAdapter.OnItemClickListener {
            override fun onItemClick(id: Int, name: String) {
                dbHelper.addFavorite(id, name)
                val position = userList.indexOfFirst { it.id == id }
                if (position != -1) {
                    userList[position].isFavorite = true
                    userAdapter.notifyItemChanged(position)
                }
            }
        })
    }

    private fun fetchCharacters() {
        val requestQueue: RequestQueue = Volley.newRequestQueue(this)
        val url = "https://gsi.fly.dev/characters"
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONObject> { response ->
                val results = response.getJSONArray("results")
                for (i in 0 until results.length()) {
                    val character = results.getJSONObject(i)
                    val id = character.getInt("id")
                    val name = character.getString("name")
                    val weapon = character.getString("weapon")
                    val vision = character.getString("vision")
                    Log.d("MainActivity", "Character: $name, Weapon: $weapon, Vision: $vision")
                    userList.add(User(id, name, weapon, vision))
                }
                userAdapter.notifyDataSetChanged()
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
            }
        )
        requestQueue.add(jsonObjectRequest)
    }
}
