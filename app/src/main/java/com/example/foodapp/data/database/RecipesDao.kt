package com.example.foodapp.data.database

import androidx.room.*
import com.example.foodapp.data.database.entities.FavouritesEntity
import com.example.foodapp.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    @Query("SELECT * FROM favorite_recipes_table ORDER BY id ASC")
    fun readFavouriteRecipes(): Flow<List<FavouritesEntity>>

    @Delete
    suspend fun deleteFavouriteRecipe(favouritesEntity: FavouritesEntity)

    @Query("DELETE FROM favorite_recipes_table")
    suspend fun deleteAllFavouriteRecipes()


}