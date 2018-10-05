package foodfacts.bevilacqua.com.foodfacts.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import foodfacts.bevilacqua.com.foodfacts.Injection
import foodfacts.bevilacqua.com.foodfacts.R
import foodfacts.bevilacqua.com.foodfacts.util.Constants
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast

class BarcodeDetailActivity : AppCompatActivity() {

    private val TAG = BarcodeDetailActivity::class.java.simpleName

    private var connectivityDisposable: Disposable? = null
    private var internetDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (!checkConnection(this)) {
            longToast(R.string.no_internet_connection)
            Log.v(TAG, "No internet connection, amen..")
            return
        }

        inspectBarcode()
    }

    override fun onResume() {
        super.onResume()

        connectivityDisposable = ReactiveNetwork.observeNetworkConnectivity(applicationContext)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { connectivity ->
                    Log.d(TAG, connectivity.toString())
                    val state = connectivity.state()
                    val name = connectivity.typeName()
                    Log.v(TAG, "connectivityDisposable - State $state name $name")
                }

        internetDisposable = ReactiveNetwork.observeInternetConnectivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isConnectedToInternet ->
                    Log.v(TAG, "internetDisposable - Connected to the web $isConnectedToInternet")
                    if (isConnectedToInternet) {
                        // TODO do something when internet is available or not
                    }
                }
    }

    override fun onPause() {
        super.onPause()
        safelyDispose(connectivityDisposable)
        safelyDispose(internetDisposable)
    }

    private fun inspectBarcode() {
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
            Log.v(TAG, "Barcode read: " + barcode.displayValue)

            // Temporary calling data repository
            val dataRepository = Injection.provideDataRepository(this)
            dataRepository.searchProductInfo(barcode.displayValue)
        } else {
            longToast(String.format(getString(R.string.barcode_error),
                    CommonStatusCodes.getStatusCodeString(resultCode)))
        }
    }

    private fun safelyDispose(disposable: Disposable?) {
        if (disposable != null && !disposable.isDisposed) {
            disposable.dispose()
        }
    }

    private fun checkConnection(applicationContext: Context): Boolean {
        // Checking first if we are connected to the web
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }
}