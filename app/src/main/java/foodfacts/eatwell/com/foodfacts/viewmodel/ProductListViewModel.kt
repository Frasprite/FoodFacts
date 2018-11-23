package foodfacts.eatwell.com.foodfacts.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import foodfacts.eatwell.com.foodfacts.Injection
import foodfacts.eatwell.com.foodfacts.data.DataRepository
import foodfacts.eatwell.com.foodfacts.model.Product

/**
 * ViewModel which expose a list of all products.
 * The ViewModel works with the [DataRepository] to get the data.
 */
class ProductListViewModel(private val repository: DataRepository) : ViewModel() {

    val observableProductList: LiveData<List<Product>> = repository.loadAllProducts()

    var productList = ObservableField<List<Product>>()

    fun setProductList(products: List<Product>) {
        this.productList.set(products)
    }

    /**
     * A creator is used to inject the product barcode into the ViewModel and load list of products.
     */
    class Factory(mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

        private val mRepository: DataRepository = Injection.provideDataRepository(mApplication)

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ProductListViewModel(mRepository) as T
        }
    }
}