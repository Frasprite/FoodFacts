package foodfacts.eatwell.com.foodfacts.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import foodfacts.eatwell.com.foodfacts.model.Ingredient
import foodfacts.eatwell.com.foodfacts.model.ProductIngredientJoin

/**
 * Room data access object for accessing the products ingredients table.
 */
@Dao
interface ProductIngredientJoinDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(productIngredientJoin: List<ProductIngredientJoin>)

    @Query("SELECT * FROM ingredients INNER JOIN product_ingredient_join ON ingredients.id = product_ingredient_join.ingredientId WHERE product_ingredient_join.productBarcode = :barcode")
    fun getIngredientsForProduct(barcode: String): LiveData<List<Ingredient>>

}