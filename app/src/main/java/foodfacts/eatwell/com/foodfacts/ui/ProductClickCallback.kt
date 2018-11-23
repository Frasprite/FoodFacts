package foodfacts.eatwell.com.foodfacts.ui

import foodfacts.eatwell.com.foodfacts.model.Product

interface ProductClickCallback {
    fun onClick(product: Product)
}
