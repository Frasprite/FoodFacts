package foodfacts.bevilacqua.com.foodfacts.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "products")
data class Product(@PrimaryKey
                   val barcode: String,
                   val name: String,
                   val imageUrl: String,
                   val energy: String,
                   val energyUnit: String,
                   val timestampScanner: Long
)