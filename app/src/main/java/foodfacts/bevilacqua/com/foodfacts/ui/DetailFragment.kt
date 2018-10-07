package foodfacts.bevilacqua.com.foodfacts.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import foodfacts.bevilacqua.com.foodfacts.R
import foodfacts.bevilacqua.com.foodfacts.databinding.FragmentDetailBinding
import foodfacts.bevilacqua.com.foodfacts.model.Product
import foodfacts.bevilacqua.com.foodfacts.viewmodel.ProductViewModel
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.vision.barcode.Barcode
import foodfacts.bevilacqua.com.foodfacts.model.Ingredient
import foodfacts.bevilacqua.com.foodfacts.util.Constants
import foodfacts.bevilacqua.com.foodfacts.viewmodel.IngredientListViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import org.jetbrains.anko.longToast


/**
 * A placeholder fragment containing details about item found with barcode.
 */
class DetailFragment : Fragment() {

    private var mBinding: FragmentDetailBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        return mBinding?.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        subscribeToProductModel()
        subscribeToIngredientsModel()
    }

    /**
     * Function used to subscribe to product model.
     */
    private fun subscribeToProductModel() {
        val productBarcode = inspectBarcode()

        if (productBarcode == null) {
            Log.w(TAG, "subscribeToProductModel - Barcode not found!")
            return
        }

        val productModel = ViewModelProviders.of(this,
                ProductViewModel.Factory(activity!!.application,
                        productBarcode)).get(ProductViewModel::class.java)

        mBinding?.productViewModel = productModel

        productModel.observableProduct.observe(this, Observer<Product> {
            if (it != null) {
                Log.d(TAG, "subscribeToProductModel - Product found on DB")
                productModel.setProduct(it)

                val requestOptions = RequestOptions()
                requestOptions.placeholder(R.drawable.ic_food)
                requestOptions.error(R.drawable.ic_broken_image)

                Glide.with(this)
                        .load(productModel.product.get()?.imageUrl)
                        .apply(requestOptions)
                        .into(mBinding?.productImageView!!)
            } else {
                // Product does not exists
                Log.v(TAG, "subscribeToProductModel - Product NOT found on DB and on API")
                activity?.longToast(R.string.barcode_not_found)
            }
        })
    }

    /**
     * Function used to subscribe to product ingredients list model.
     */
    private fun subscribeToIngredientsModel() {
        val productBarcode = inspectBarcode()

        if (productBarcode == null) {
            Log.w(TAG, "subscribeToIngredientsModel - Barcode not found!")
            return
        }

        ingredientsList.layoutManager = LinearLayoutManager(activity!!.applicationContext)

        val adapter = IngredientRecyclerViewAdapter()
        ingredientsList.adapter = adapter

        val ingredientListViewModel = ViewModelProviders.of(this,
                IngredientListViewModel.Factory(activity!!.application,
                        productBarcode)).get(IngredientListViewModel::class.java)

        ingredientListViewModel.observableIngredientList.observe(this, Observer<List<Ingredient>> {
            if (it != null) {
                Log.d(TAG, "subscribeToIngredientsModel - List size : ${it.size}")
                ingredientListViewModel.setIngredientList(it)
                if (it.isEmpty()) {
                    emptyList.visibility = View.VISIBLE
                } else {
                    emptyList.visibility = View.GONE
                }
                adapter.submitList(it)
                adapter.notifyDataSetChanged()
            }
        })
    }

    /**
     * Inspecting found barcode.
     */
    private fun inspectBarcode(): String? {
        val data = activity!!.intent.extras

        if (data == null) {
            activity?.longToast(R.string.barcode_failure)
            Log.d(TAG, "No barcode captured, intent data is null")
            return null
        }

        val barcode = data.getString(Constants.BarcodeObject)

        if (barcode == null) {
            activity?.longToast(getString(R.string.barcode_error))
            Log.w(TAG, "No barcode captured, because is null")
            return null
        }

        return barcode
    }

    companion object {
        private val TAG = DetailFragment::class.java.simpleName
    }
}