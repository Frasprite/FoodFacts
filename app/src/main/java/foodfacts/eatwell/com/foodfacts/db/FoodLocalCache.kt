package foodfacts.eatwell.com.foodfacts.db

import android.arch.lifecycle.LiveData
import foodfacts.eatwell.com.foodfacts.model.Ingredient
import foodfacts.eatwell.com.foodfacts.model.Product
import foodfacts.eatwell.com.foodfacts.model.ProductIngredientJoin
import java.util.concurrent.Executor

/**
 * Class that handles the DAO local data source.
 * This ensures that methods are triggered on the correct executor.
 */
class FoodLocalCache(
        private val productDao: ProductDAO,
        private val ingredientDao: IngredientDAO,
        private val productIngredientJoinDao: ProductIngredientJoinDAO,
        private val ioExecutor: Executor
) {
    fun insertProduct(product: Product) {
        ioExecutor.execute {
            productDao.insert(product)
        }
    }

    fun insertIngredients(ingredients: ArrayList<Ingredient>) {
        ioExecutor.execute {
            ingredientDao.insert(ingredients)
        }
    }

    fun insertProductIngredients(productIngredients: ArrayList<ProductIngredientJoin>) {
        ioExecutor.execute {
            productIngredientJoinDao.insert(productIngredients)
        }
    }

    fun loadProduct(productBarcode: String): LiveData<Product> {
        return productDao.loadProduct(productBarcode)
    }

    fun loadAllProducts(): LiveData<List<Product>> {
        return productDao.loadAllProducts()
    }

    fun loadIngredients(productBarcode: String): LiveData<List<Ingredient>> {
        return productIngredientJoinDao.getIngredientsForProduct(productBarcode)
    }

    fun containProduct(productBarcode: String, maxTimestamp: Long): Product {
        return productDao.containProduct(productBarcode, maxTimestamp)
    }
}