package foodfacts.bevilacqua.com.foodfacts

import android.content.Context
import foodfacts.bevilacqua.com.foodfacts.api.SearchService
import foodfacts.bevilacqua.com.foodfacts.data.DataRepository
import foodfacts.bevilacqua.com.foodfacts.db.FoodDatabase
import foodfacts.bevilacqua.com.foodfacts.db.FoodLocalCache
import java.util.concurrent.Executors

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [FoodLocalCache] based on the database DAO.
     */
    private fun provideCache(context: Context): FoodLocalCache {
        val database = FoodDatabase.getInstance(context)
        return FoodLocalCache(database.productDao(), database.ingredientDao(), database.productIngredientJoinDao(), Executors.newSingleThreadExecutor())
    }

    /**
     * Creates an instance of [DataRepository] based on the [SearchService] and a
     * [FoodLocalCache]
     */
    fun provideDataRepository(context: Context): DataRepository {
        return DataRepository(SearchService.create(), provideCache(context))
    }
}