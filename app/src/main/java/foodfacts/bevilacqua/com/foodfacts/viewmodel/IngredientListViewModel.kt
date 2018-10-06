package foodfacts.bevilacqua.com.foodfacts.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import foodfacts.bevilacqua.com.foodfacts.Injection
import foodfacts.bevilacqua.com.foodfacts.data.DataRepository
import foodfacts.bevilacqua.com.foodfacts.model.Ingredient

/**
 * ViewModel for the detail activity screen which expose a list of ingredients.
 * The ViewModel works with the [DataRepository] to get the data.
 */
class IngredientListViewModel(private val repository: DataRepository, private val productBarcode: String) : ViewModel() {

    val observableIngredientList: LiveData<List<Ingredient>> = repository.loadIngredients(productBarcode)

    var ingredientList = ObservableField<List<Ingredient>>()

    fun setIngredientList(ingredients: List<Ingredient>) {
        this.ingredientList.set(ingredients)
    }

    /**
     * A creator is used to inject the product barcode into the ViewModel and load list of ingredients.
     */
    class Factory(mApplication: Application, private val productBarcode: String) : ViewModelProvider.NewInstanceFactory() {

        private val mRepository: DataRepository = Injection.provideDataRepository(mApplication)

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return IngredientListViewModel(mRepository, productBarcode) as T
        }
    }
}