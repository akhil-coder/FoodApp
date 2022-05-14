package com.example.foodapp.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foodapp.R
import com.example.foodapp.adapter.PagerAdapter
import com.example.foodapp.data.database.entities.FavouritesEntity
import com.example.foodapp.databinding.ActivityDetailsBinding
import com.example.foodapp.ui.fragments.ingredients.IngredientsFragment
import com.example.foodapp.ui.fragments.instructions.InstructionsFragment
import com.example.foodapp.ui.fragments.overview.OverViewFragment
import com.example.foodapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.lang.Exception

@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private val TAG = "DetailsActivity"

    private val mainViewModel: MainViewModel by viewModels()
    private val args by navArgs<DetailsActivityArgs>()

    private var _binding: ActivityDetailsBinding? = null
    private val binding get() = _binding!!

    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fragments = ArrayList<Fragment>()
        fragments.add(OverViewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")


        val resultBundle = Bundle()
        resultBundle.putParcelable("recipeBundle", args.result)

        val adapter = PagerAdapter(
            resultBundle,
            fragments,
            titles,
            supportFragmentManager
        )

        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)
        val menuItem = menu?.findItem(R.id.save_to_favourites_menu)
        checkSavedRecipes(menuItem)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        } else if (item.itemId == R.id.save_to_favourites_menu && !recipeSaved) {
            saveToFavourites(item)
        } else if (item.itemId == R.id.save_to_favourites_menu && recipeSaved)
            removeFromFavourites(item)

        return super.onOptionsItemSelected(item)
    }

    private fun checkSavedRecipes(menuItem: MenuItem?) {
        mainViewModel.readFavouriteRecipes.observe(this) { favouriteEntity ->
            try {
                for (savedRecipe in favouriteEntity) {
                    if (savedRecipe.result.recipeId == args.result.recipeId) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    }
                }
            } catch (e: Exception) {
                Log.d(TAG, "checkSavedRecipes: ", e)
            }
        }
    }

    private fun saveToFavourites(item: MenuItem) {
        val favouritesEntity = FavouritesEntity(
            0,
            args.result
        )
        mainViewModel.insertFavouriteRecipe(favouritesEntity)
        changeMenuItemColor(item, R.color.yellow)
        showSnackBar("Recipe Saved")
        recipeSaved = true
    }

    private fun removeFromFavourites(item: MenuItem) {
        val favoritesEntity =
            FavouritesEntity(
                savedRecipeId,
                args.result
            )
        mainViewModel.deleteFavouriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed from Favourites")
        recipeSaved = false
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    private fun changeMenuItemColor(item: MenuItem?, color: Int) {
        item?.icon?.setTint(ContextCompat.getColor(this, color))
    }
}