package foodfacts.bevilacqua.com.foodfacts.ui

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.github.pwittchen.reactivenetwork.library.rx2.ReactiveNetwork
import foodfacts.bevilacqua.com.foodfacts.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_detail.*

class BarcodeDetailActivity : AppCompatActivity() {

    private var connectivityDisposable: Disposable? = null
    private var internetDisposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        if (!checkConnection(this)) {
            Log.v(TAG, "No internet connection, firing a snackbar..")
            Snackbar.make(detailMainLayout, R.string.no_internet_connection, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry) {
                        // Retry downloading data by simply recreating activity
                        this@BarcodeDetailActivity.recreate()
                    }
                    .show()
            return
        }
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

    companion object {
        private val TAG = BarcodeDetailActivity::class.java.simpleName
    }
}