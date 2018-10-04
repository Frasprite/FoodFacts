package foodfacts.bevilacqua.com.foodfacts.db

/**
 * Class that handles the DAO local data source.
 * This ensures that methods are triggered on the correct executor.
 */
class ProductLocalCache(
        private val productDao: ProductDAO,
        private val ingredientDao: IngredientDAO,
        private val productIngredientJoinDao: ProductIngredientJoinDAO
) {

    // TODO insert crud functions
}