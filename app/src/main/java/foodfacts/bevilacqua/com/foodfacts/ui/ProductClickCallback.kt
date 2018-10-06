package foodfacts.bevilacqua.com.foodfacts.ui

import foodfacts.bevilacqua.com.foodfacts.model.Product

interface ProductClickCallback {
    fun onClick(product: Product)
}
