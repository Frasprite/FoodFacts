package foodfacts.bevilacqua.com.foodfacts.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import foodfacts.bevilacqua.com.foodfacts.model.Ingredient

/**
 * Room data access object for accessing the ingredients table.
 */
@Dao
interface IngredientDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(ingredients: List<Ingredient>)

    @Query("SELECT * FROM ingredients WHERE id LIKE :id")
    fun loadIngredient(id: String): LiveData<Ingredient>
}