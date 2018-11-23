package foodfacts.eatwell.com.foodfacts.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "ingredients")
data class Ingredient(@PrimaryKey
                      val id: String,
                      val text: String
)