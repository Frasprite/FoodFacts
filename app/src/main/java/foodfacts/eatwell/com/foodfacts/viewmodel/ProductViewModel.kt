package foodfacts.eatwell.com.foodfacts.viewmodel

import android.app.Application
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.databinding.ObservableField
import foodfacts.eatwell.com.foodfacts.Injection
import foodfacts.eatwell.com.foodfacts.data.DataRepository
import foodfacts.eatwell.com.foodfacts.model.Product

class ProductViewModel(private val repository: DataRepository, productBarcode: String) : ViewModel() {

    val observableProduct: LiveData<Product> = repository.loadProduct(productBarcode)

    var product = ObservableField<Product>()

    fun setProduct(product: Product) {
        this.product.set(product)
    }

    /**
     * A creator is used to inject the product barcode into the ViewModel.
     *
     *
     * This creator is to showcase how to inject dependencies into ViewModels.
     */
    class Factory(private val mApplication: Application, private val mProductBarcode: String) : ViewModelProvider.NewInstanceFactory() {

        private val mRepository: DataRepository = Injection.provideDataRepository(mApplication)

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return ProductViewModel(mRepository, mProductBarcode) as T
        }
    }
}