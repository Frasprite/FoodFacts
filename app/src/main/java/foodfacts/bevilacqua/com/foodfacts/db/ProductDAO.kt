package foodfacts.bevilacqua.com.foodfacts.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import foodfacts.bevilacqua.com.foodfacts.model.Product

/**
 * Room data access object for accessing the products table.
 */
@Dao
interface ProductDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(product: Product)

    @Query("SELECT * FROM products WHERE barcode LIKE :barcode")
    fun loadProduct(barcode: String): LiveData<Product>

    @Query("SELECT * FROM products ORDER BY timestampScanner")
    fun loadAllProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM products WHERE barcode = :barcode AND timestampScanner > :lastRefreshMax")
    fun containProduct(barcode: String, lastRefreshMax: Long): Product
}