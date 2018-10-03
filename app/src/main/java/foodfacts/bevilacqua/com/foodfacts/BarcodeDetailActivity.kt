package foodfacts.bevilacqua.com.foodfacts

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import foodfacts.bevilacqua.com.foodfacts.api.SearchService
import foodfacts.bevilacqua.com.foodfacts.data.DataRepository
import foodfacts.bevilacqua.com.foodfacts.util.Constants
import kotlinx.android.synthetic.main.fragment_detail.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class BarcodeDetailActivity : AppCompatActivity() {

    private val TAG = BarcodeDetailActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val data = intent.extras

        if (data == null) {
            longToast(R.string.barcode_failure)
            Log.d(TAG, "No barcode captured, intent data is null")
            return
        }

        val resultCode = data.getInt(Constants.StatusCode)
        if (resultCode == CommonStatusCodes.SUCCESS) {
            val barcode = data.getParcelable<Barcode>(Constants.BarcodeObject)

            if (barcode == null) {
                longToast(R.string.barcode_failure)
                Log.w(TAG, "No barcode captured, because is null")
                return
            }

            toast(R.string.barcode_success)
            barcodeValue.text = barcode.displayValue
            Log.v(TAG, "Barcode read: " + barcode.displayValue)

            // Temporary calling data repository
            val dataRepository = DataRepository(SearchService.create())
            dataRepository.searchProductInfo(barcode.displayValue)
        } else {
            longToast(String.format(getString(R.string.barcode_error),
                    CommonStatusCodes.getStatusCodeString(resultCode)))
        }
    }
}