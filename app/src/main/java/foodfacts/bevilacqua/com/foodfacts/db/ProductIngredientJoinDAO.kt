package foodfacts.bevilacqua.com.foodfacts.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import foodfacts.bevilacqua.com.foodfacts.model.Ingredient
import foodfacts.bevilacqua.com.foodfacts.model.ProductIngredientJoin

@Dao
interface ProductIngredientJoinDAO {

    @Insert
    fun insert(userRepoJoin: ProductIngredientJoin)

    @Query("SELECT * FROM ingredients INNER JOIN product_ingredient_join ON ingredients.id = product_ingredient_join.ingredientId WHERE product_ingredient_join.productBarcode = :barcode")
    fun getIngredientsForProduct(barcode: String): List<Ingredient>

}