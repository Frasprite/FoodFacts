package foodfacts.bevilacqua.com.foodfacts.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
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
import foodfacts.bevilacqua.com.foodfacts.util.Constants


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
    }

    /**
     * Function used to subscribe to product model.
     */
    private fun subscribeToProductModel() {
        val bundle = activity!!.intent.extras
        val barcode = bundle?.getParcelable<Barcode>(Constants.BarcodeObject)
        val productBarcode = barcode?.displayValue

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
                productModel.setProduct(it)

                val requestOptions = RequestOptions()
                requestOptions.placeholder(R.drawable.ic_barcode)
                requestOptions.error(R.drawable.ic_broken_image)

                Glide.with(this)
                        .load(productModel.product.get()?.imageUrl)
                        .apply(requestOptions)
                        .into(mBinding?.productImageView!!)
            }
        })
    }

    companion object {
        private val TAG = DetailFragment::class.java.simpleName
    }
}