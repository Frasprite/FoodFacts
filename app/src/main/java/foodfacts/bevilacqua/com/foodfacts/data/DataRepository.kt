package foodfacts.bevilacqua.com.foodfacts.data

import android.util.Log
import foodfacts.bevilacqua.com.foodfacts.api.SearchService
import foodfacts.bevilacqua.com.foodfacts.api.searchProduct

/**
 * Repository class that works with local and remote data sources.
 * This class is responsible for triggering API requests and saving the response into database.
 */
class DataRepository(
        private val service: SearchService
) {

    /**
     * Search product.
     */
    fun searchProductInfo(productBarcode: String) {
        searchProduct(service, productBarcode, {
            Log.d("DataRepository", "searchProductInfo - Inserting product info: ${it.product} " +
                    "${it.product.imageUrl} ${it.product.productName}")
            // TODO insert data here
        }, { error ->
            Log.d("DataRepository", "searchProductInfo - Error: $error")
        })
    }
}