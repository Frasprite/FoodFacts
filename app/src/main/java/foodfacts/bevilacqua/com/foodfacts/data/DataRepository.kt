package foodfacts.bevilacqua.com.foodfacts.data

import android.arch.lifecycle.LiveData
import android.util.Log
import foodfacts.bevilacqua.com.foodfacts.api.SearchService
import foodfacts.bevilacqua.com.foodfacts.api.searchProduct
import foodfacts.bevilacqua.com.foodfacts.db.FoodLocalCache
import foodfacts.bevilacqua.com.foodfacts.model.Ingredient
import foodfacts.bevilacqua.com.foodfacts.model.Product
import foodfacts.bevilacqua.com.foodfacts.model.ProductIngredientJoin
import org.jetbrains.anko.doAsync
import java.util.*


/**
 * Repository class that works with local and remote data sources.
 * This class is responsible for triggering API requests and saving the response into database.
 */
class DataRepository(
        private val service: SearchService,
        private val cache: FoodLocalCache
) {

    private val FRESH_TIMEOUT_IN_DAYS = 7

    /**
     * Search product info on web.
     */
    private fun searchProductInfo(productBarcode: String) {
        searchProduct(service, productBarcode, {
            Log.d(TAG, "searchProductInfo - Found product info")

            if (it.rawProduct != null) {
                // Creating product
                val rawProduct = it.rawProduct
                val nutriments = rawProduct.nutriments
                val product = Product(productBarcode, rawProduct.productName, rawProduct.imageUrl,
                        nutriments.energy, nutriments.energyUnit, System.currentTimeMillis())
                Log.v(TAG, "searchProductInfo - Adding $product")
                cache.insertProduct(product)

                // Creating ingredient and product ingredients reference
                val rawIngredients = rawProduct.rawIngredients
                val ingredients = ArrayList<Ingredient>()
                val productIngredients = ArrayList<ProductIngredientJoin>()
                for (rawIngredient in rawIngredients) {
                    ingredients.add(Ingredient(rawIngredient.id, rawIngredient.text))
                    productIngredients.add(ProductIngredientJoin(productBarcode, rawIngredient.id))
                }

                Log.v(TAG, "searchProductInfo - Adding $ingredients")
                cache.insertIngredients(ingredients)
                cache.insertProductIngredients(productIngredients)
            } else {
                Log.w(TAG, "Product not found!")
            }
        }, { error ->
            Log.w(TAG, "searchProductInfo - Error: $error")
        })
    }

    /**
     * Load product info from database.
     */
    fun loadProduct(productBarcode: String): LiveData<Product> {
        refreshProduct(productBarcode) // try to refresh data if possible from API
        return cache.loadProduct(productBarcode) // return a LiveData directly from the database
    }

    /**
     * Function which load all products from database.
     */
    fun loadAllProducts(): LiveData<List<Product>> {
        return cache.loadAllProducts()
    }

    /**
     * Load list of ingredients for specific product.
     */
    fun loadIngredients(productBarcode: String): LiveData<List<Ingredient>> {
        return cache.loadIngredients(productBarcode)
    }

    /**
     * Checking (asynchronously) if product should be refreshed.
     */
    private fun refreshProduct(productBarcode: String) {
        doAsync {
            // Check if product was fetched recently
            val productExists = cache.containProduct(productBarcode, getMaxRefreshTime(Date())) != null

            // Check if product have to be updated
            if (!productExists) {
                searchProductInfo(productBarcode)
            }
        }
    }

    /**
     * Evaluate how much time should be passed since last sync.
     */
    private fun getMaxRefreshTime(currentDate: Date): Long {
        val cal = Calendar.getInstance()
        cal.time = currentDate
        cal.add(Calendar.DAY_OF_MONTH, -FRESH_TIMEOUT_IN_DAYS)
        return cal.time.time
    }

    companion object {
        private val TAG = DataRepository::class.java.simpleName
    }
}