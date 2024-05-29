package com.example.easylearn

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    private lateinit var context: Context
    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(id: Int, name: String)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val favoriteButton: Button = itemView.findViewById(R.id.btnFavorite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.user_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = userList[position]
        holder.name.text = user.name

        // Actualiza el color del botón según si es favorito o no
        if (user.isFavorite) {
            holder.favoriteButton.setBackgroundColor(ContextCompat.getColor(context, R.color.favoriteColor))
        } else {
            holder.favoriteButton.setBackgroundColor(ContextCompat.getColor(context, R.color.defaultColor))
        }

        holder.favoriteButton.setOnClickListener {
            listener.onItemClick(user.id, user.name)

            // Animación del botón
            val colorFrom = if (user.isFavorite) ContextCompat.getColor(context, R.color.favoriteColor) else ContextCompat.getColor(context, R.color.defaultColor)
            val colorTo = if (user.isFavorite) ContextCompat.getColor(context, R.color.defaultColor) else ContextCompat.getColor(context, R.color.favoriteColor)
            val colorAnimation = ObjectAnimator.ofArgb(holder.favoriteButton, "backgroundColor", colorFrom, colorTo)

            val scaleXUp = ObjectAnimator.ofFloat(holder.favoriteButton, "scaleX", 1f, 1.2f)
            val scaleYUp = ObjectAnimator.ofFloat(holder.favoriteButton, "scaleY", 1f, 1.2f)
            val scaleXDown = ObjectAnimator.ofFloat(holder.favoriteButton, "scaleX", 1.2f, 1f)
            val scaleYDown = ObjectAnimator.ofFloat(holder.favoriteButton, "scaleY", 1.2f, 1f)

            val scaleUp = AnimatorSet().apply {
                playTogether(scaleXUp, scaleYUp)
                duration = 200
            }
            val scaleDown = AnimatorSet().apply {
                playTogether(scaleXDown, scaleYDown)
                duration = 200
            }

            val animatorSet = AnimatorSet()
            animatorSet.playSequentially(scaleUp, scaleDown, colorAnimation)
            animatorSet.start()

            user.isFavorite = !user.isFavorite
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}