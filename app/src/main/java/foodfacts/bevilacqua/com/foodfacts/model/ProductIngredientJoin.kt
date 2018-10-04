package foodfacts.bevilacqua.com.foodfacts.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey


@Entity(tableName = "product_ingredient_join",
        primaryKeys = arrayOf("productBarcode", "ingredientId"),
        foreignKeys = arrayOf(
                ForeignKey(
                        entity = Product::class, parentColumns = arrayOf("barcode"),
                        childColumns = arrayOf("productBarcode")),
                ForeignKey(
                        entity = Ingredient::class, parentColumns = arrayOf("id"),
                        childColumns = arrayOf("ingredientId"))))
class ProductIngredientJoin(val productBarcode: String, val ingredientId: String)