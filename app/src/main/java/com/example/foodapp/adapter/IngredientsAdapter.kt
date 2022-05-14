package com.example.foodapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.foodapp.R
import com.example.foodapp.model.ExtendedIngredient
import com.example.foodapp.util.Constants.Companion.BASE_IMAGE_URL
import com.example.foodapp.util.RecipesDiffUtil

class IngredientsAdapter : RecyclerView.Adapter<IngredientsAdapter.MyViewHolder>() {

    private var ingredientsList = emptyList<ExtendedIngredient>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.ingredients_row_layout, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.findViewById<ImageView>(R.id.ingredient_imageview)
            .load(BASE_IMAGE_URL + ingredientsList[position].image) {
                crossfade(600)
                error(R.drawable.ic_error_placeholder)
            }
        holder.itemView.findViewById<TextView>(R.id.ingredient_name).text =
            ingredientsList[position].name.capitalize()
        holder.itemView.findViewById<TextView>(R.id.ingredient_amount).text =
            ingredientsList[position].amount.toString()
        holder.itemView.findViewById<TextView>(R.id.ingredient_unit).text =
            ingredientsList[position].unit
        holder.itemView.findViewById<TextView>(R.id.ingredient_consistency).text =
            ingredientsList[position].consistency
        holder.itemView.findViewById<TextView>(R.id.ingredient_original).text =
            ingredientsList[position].original
    }

    override fun getItemCount(): Int {
        return ingredientsList.size
    }

    fun setData(newIngredients: List<ExtendedIngredient>) {
        val ingredientsDiffUtil = RecipesDiffUtil(ingredientsList, newIngredients)
        val diffUtilResult = DiffUtil.calculateDiff(ingredientsDiffUtil)
        ingredientsList = newIngredients
        diffUtilResult.dispatchUpdatesTo(this)
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}