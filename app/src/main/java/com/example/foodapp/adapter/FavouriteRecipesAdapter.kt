package com.example.foodapp.adapter

import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.data.database.entities.FavouritesEntity
import com.example.foodapp.databinding.FavouritesRecipeRowLayoutBinding
import com.example.foodapp.ui.fragments.favourite.FavoriteRecipesFragment
import com.example.foodapp.ui.fragments.favourite.FavoriteRecipesFragmentDirections
import com.example.foodapp.util.RecipesDiffUtil

class FavouriteRecipesAdapter(
    private val requireActivity: FragmentActivity
) : RecyclerView.Adapter<FavouriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    private var favouriteRecipes = emptyList<FavouritesEntity>()

    class MyViewHolder(private val binding: FavouritesRecipeRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favouritesEntity: FavouritesEntity) {
            binding.favoritesEntity = favouritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =
                    FavouritesRecipeRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val selectedRecipe = favouriteRecipes[position]
        holder.bind(selectedRecipe)

        holder.itemView.findViewById<ConstraintLayout>(R.id.favoriteRecipesRowLayout)
            .setOnClickListener {
                val action =
                    FavoriteRecipesFragmentDirections.actionFavoriteFragmentToDetailsActivity(
                        selectedRecipe.result
                    )
                holder.itemView.findNavController().navigate(action)
            }

        holder.itemView.findViewById<ConstraintLayout>(R.id.favoriteRecipesRowLayout)
            .setOnLongClickListener {
                requireActivity.startActionMode(this)
                true
            }
    }

    override fun getItemCount(): Int {
        return favouriteRecipes.size
    }


    override fun onCreateActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        actionMode?.menuInflater?.inflate(R.menu.favourites_contextual_menu, menu)
        return true
    }

    override fun onPrepareActionMode(actionMode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(actionMode: ActionMode?, menu: MenuItem?): Boolean {
        return true
    }

    override fun onDestroyActionMode(actionMode: ActionMode?) {

    }

    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor =
            ContextCompat.getColor(requireActivity, color) 
    }

    fun setData(newFavouriteRecipes: List<FavouritesEntity>) {
        val favouriteRecipesDiffUtil = RecipesDiffUtil(favouriteRecipes, newFavouriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favouriteRecipesDiffUtil)
        favouriteRecipes = newFavouriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

}