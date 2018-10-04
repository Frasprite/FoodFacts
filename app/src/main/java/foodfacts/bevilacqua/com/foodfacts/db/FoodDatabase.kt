package foodfacts.bevilacqua.com.foodfacts.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import foodfacts.bevilacqua.com.foodfacts.model.Ingredient
import foodfacts.bevilacqua.com.foodfacts.model.Product
import foodfacts.bevilacqua.com.foodfacts.model.ProductIngredientJoin

/**
 * Database schema that holds the lists of products and ingredients.
 */
@Database(
        entities = [Product::class, Ingredient::class, ProductIngredientJoin::class],
        version = 1,
        exportSchema = false
)
abstract class FoodDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDAO
    abstract fun ingredientDao(): IngredientDAO
    abstract fun productIngredientJoinDao(): ProductIngredientJoinDAO

    companion object {

        @Volatile
        private var INSTANCE: FoodDatabase? = null

        fun getInstance(context: Context): FoodDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE
                            ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        FoodDatabase::class.java, "food.db")
                        .build()
    }
}
