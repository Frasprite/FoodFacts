package foodfacts.eatwell.com.foodfacts.ui

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import foodfacts.eatwell.com.foodfacts.R
import foodfacts.eatwell.com.foodfacts.databinding.FragmentMainBinding
import foodfacts.eatwell.com.foodfacts.model.Product
import foodfacts.eatwell.com.foodfacts.viewmodel.ProductListViewModel
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a list of product scanned.
 */
class MainActivityFragment : Fragment() {

    private var mBinding: FragmentMainBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        Log.d(TAG, "onCreateView - start")

        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)

        return mBinding!!.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subscribeToProductsModel()
    }

    private fun subscribeToProductsModel() {
        // Init adapter
        val productAdapter = ProductRecyclerViewAdapter(object : ProductClickCallback {
            override fun onClick(product: Product) {
                if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                    (activity as MainActivity).launchDetailView(product.barcode)
                }
            }
        })

        productsList.adapter = productAdapter

        val productListViewModel = ViewModelProviders.of(this,
                ProductListViewModel.Factory(activity!!.application)).get(ProductListViewModel::class.java)

        productListViewModel.observableProductList.observe(this, Observer<List<Product>> {
            if (it != null) {
                Log.d(TAG, "subscribeToProductsModel - List size : ${it.size}")
                productListViewModel.setProductList(it)
                if (it.isEmpty()) {
                    emptyList.visibility = View.VISIBLE
                } else {
                    emptyList.visibility = View.GONE
                }
                productAdapter.submitList(it)
                productAdapter.notifyDataSetChanged()

                // Espresso does not know how to wait for data binding's loop so we execute changes
                // sync.
                mBinding!!.executePendingBindings()
            }
        })
    }

    companion object {
        private val TAG = MainActivityFragment::class.java.simpleName
    }
}
