package foodfacts.bevilacqua.com.foodfacts.api

import android.util.Log

import foodfacts.bevilacqua.com.foodfacts.rawmodel.ProductRequest

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val TAG = "SearchService"

/**
 * Trigger a request to the OpenFoodFacts open API with the following params:
 * @param productBarcode text representation of product barcode
 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle received data
 * @param onError function that defines how to handle request failure
 */
fun searchProduct(
        service: SearchService,
        productBarcode: String,
        onSuccess: (productRequest: ProductRequest) -> Unit,
        onError: (error: String) -> Unit) {
    Log.d(TAG, "searchProduct - Product barcode id $productBarcode")

    service.searchProduct(productBarcode).enqueue(
            object : Callback<ProductRequest> {
                override fun onFailure(call: Call<ProductRequest>, t: Throwable) {
                    Log.d(TAG, "onFailure - Failed to get product data $call \n${t.message}")
                    onError(t.message ?: "Unknown error")
                }

                override fun onResponse(call: Call<ProductRequest>, response: Response<ProductRequest>) {
                    Log.d(TAG, "onResponse - Got a response $response")
                    if (response.isSuccessful) {
                        val productRequest = response.body()
                        if (productRequest != null) {
                            onSuccess(productRequest)
                        } else {
                            onError("Null body response!")
                        }
                    } else {
                        onError(response.errorBody()?.string() ?: "Unknown error")
                    }
                }
            }
    )
}

interface SearchService {

    /**
     * Search product info.
     *
     * Complete sample: https://world.openfoodfacts.org/api/v0/product/3329770057258.json
     */
    @GET("product/{productBarcode}.json")
    fun searchProduct(@Path("productBarcode") productBarcode: String): Call<ProductRequest>

    companion object {

        private const val BASE_URL = "https://world.openfoodfacts.org/api/v0/"

        fun create(): SearchService {
            val logger = HttpLoggingInterceptor()
            logger.level = HttpLoggingInterceptor.Level.BASIC

            // WTF https://github.com/openfoodfacts/openfoodfacts-androidapp/issues/793
            // Issue found with Tablet running Android Nougat 7.0
            val client = OkHttpClient.Builder()
                        .addInterceptor(logger)
                        .build()

            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(SearchService::class.java)
        }
    }
}