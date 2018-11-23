package foodfacts.eatwell.com.foodfacts

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import foodfacts.eatwell.com.foodfacts.db.FoodDatabase
import foodfacts.eatwell.com.foodfacts.model.Ingredient
import foodfacts.eatwell.com.foodfacts.model.Product
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.ArrayList


class DAOTest {
    private lateinit var appDatabase: FoodDatabase
    private lateinit var product: LiveData<Product>
    private lateinit var ingredient: LiveData<Ingredient>

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getTargetContext()
        appDatabase = Room.inMemoryDatabaseBuilder(context, FoodDatabase::class.java).build()
    }

    @Test
    fun openDAOTest() {
        val productDAO = appDatabase.productDao()

        Assert.assertNotNull(productDAO)
    }

    @Test
    fun loadProductTest() {
        val productDAO = appDatabase.productDao()
        // https://world.openfoodfacts.org/api/v0/rawProduct/3329770057258.json
        product = productDAO.loadProduct("3329770057258")
        Assert.assertNull(product.value)
    }

    @Test
    fun insertProductTest() {
        val productDAO = appDatabase.productDao()

        val product = Product("3329770057258", "Petits Filous Tub's Goût Fraise, Pêche, Framboise",
                "https://static.openfoodfacts.org/images/products/332/977/005/7258/front_fr.31.400.jpg",
                "385", "kcal", 1538936893)
        productDAO.insert(product)
    }

    @Test
    fun loadIngredientTest() {
        val ingredientDAO = appDatabase.ingredientDao()

        ingredient = ingredientDAO.loadIngredient("sugar")
        Assert.assertNull(product.value)
    }

    @Test
    fun insertIngredientTest() {
        val ingredientDAO = appDatabase.ingredientDao()

        val ingredient = Ingredient("en-sugar", "sugar")
        val ingredients = ArrayList<Ingredient>()
        ingredients.add(ingredient)
        ingredientDAO.insert(ingredients)
    }

    @After
    fun tearDown() {
        appDatabase.close()
    }
}