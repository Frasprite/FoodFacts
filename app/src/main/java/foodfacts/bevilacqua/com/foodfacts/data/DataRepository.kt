package foodfacts.bevilacqua.com.foodfacts.data

import android.arch.lifecycle.LiveData
import android.util.Log
import foodfacts.bevilacqua.com.foodfacts.api.SearchService
import foodfacts.bevilacqua.com.foodfacts.api.searchProduct
import foodfacts.bevilacqua.com.foodfacts.db.FoodLocalCache
import foodfacts.bevilacqua.com.foodfacts.model.Ingredient
import foodfacts.bevilacqua.com.foodfacts.model.Product
import foodfacts.bevilacqua.com.foodfacts.model.ProductIngredientJoin

/**
 * Repository class that works with local and remote data sources.
 * This class is responsible for triggering API requests and saving the response into database.
 */
class DataRepository(
        private val service: SearchService,
        private val cache: FoodLocalCache
) {

    /**
     * Search product info on web.
     */
    fun searchProductInfo(productBarcode: String) {
        searchProduct(service, productBarcode, {
            Log.d("DataRepository", "searchProductInfo - Found product info")

            // Creating product
            val rawProduct = it.rawProduct
            val nutriments = rawProduct.nutriments
            val product = Product(productBarcode, rawProduct.productName, rawProduct.imageUrl,
                    nutriments.energy, nutriments.energyUnit, System.currentTimeMillis())
            Log.v("DataRepository", "searchProductInfo - Adding $product")
            cache.insertProduct(product)

            // Creating ingredient and product ingredients reference
            val rawIngredients = rawProduct.rawIngredients
            val ingredients = ArrayList<Ingredient>()
            val productIngredients = ArrayList<ProductIngredientJoin>()
            for (rawIngredient in rawIngredients) {
                ingredients.add(Ingredient(rawIngredient.id, rawIngredient.text))
                productIngredients.add(ProductIngredientJoin(productBarcode, rawIngredient.id))
            }

            Log.v("DataRepository", "searchProductInfo - Adding $ingredients")
            cache.insertIngredients(ingredients)
            cache.insertProductIngredients(productIngredients)
        }, { error ->
            Log.w("DataRepository", "searchProductInfo - Error: $error")
        })
    }

    /**
     * Load product info from database.
     */
    fun loadProduct(productBarcode: String): LiveData<Product> {
        return cache.loadProduct(productBarcode)
    }
}