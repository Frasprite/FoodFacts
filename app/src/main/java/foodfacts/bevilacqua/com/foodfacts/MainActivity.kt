package foodfacts.bevilacqua.com.foodfacts

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.vision.barcode.Barcode
import org.jetbrains.anko.alert


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName
    private val RC_BARCODE_CAPTURE = 9001
    private val PERMISSIONS_REQUEST_CAMERA = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                shouldShowExplanation()
            } else {
                // Permission granted, proceed
                openBarcodeCaptureActivity()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CAMERA -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission was granted, yay! We can now continue
                    openBarcodeCaptureActivity()
                } else {
                    // Permission denied, boo!
                }

                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * [.RESULT_CANCELED] if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     *
     * You will receive this call immediately before onResume() when your
     * activity is re-starting.
     *
     * @param requestCode The integer request code originally supplied to
     * startActivityForResult(), allowing you to identify who this
     * result came from.
     * @param resultCode  The integer result code returned by the child activity
     * through its setResult().
     * @param data        An Intent, which can return result data to the caller
     * (various data can be attached to Intent "extras").
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    val barcode = data.getParcelableExtra<Barcode>(BarcodeCaptureActivity.BarcodeObject)
                    statusMessage.setText(R.string.barcode_success)
                    barcodeValue.text = barcode.displayValue
                    Log.d(TAG, "Barcode read: " + barcode.displayValue)
                } else {
                    statusMessage.setText(R.string.barcode_failure)
                    Log.d(TAG, "No barcode captured, intent data is null")
                }
            } else {
                statusMessage.text = String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode))
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun openBarcodeCaptureActivity() {
        // Launch barcode activity
        val intent = Intent(this, BarcodeCaptureActivity::class.java)
        intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked)
        intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked)

        startActivityForResult(intent, RC_BARCODE_CAPTURE)
    }

    private fun shouldShowExplanation() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.CAMERA)) {
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            showExplanationDialog()
        } else {
            // No explanation needed, we can request the permission.
            requestPermission()

            // PERMISSIONS_REQUEST_CAMERA is an app-defined int constant.
            // The callback method gets the result of the request.
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA),
                PERMISSIONS_REQUEST_CAMERA)
    }

    private fun showExplanationDialog() {
        alert {
            titleResource = R.string.camera_permission_dialog_title
            messageResource = R.string.camera_permission_dialog_message

            positiveButton(R.string.dialog_grant_permission_button) { dialog ->
                dialog.dismiss()
                requestPermission()
            }

            negativeButton(R.string.dialog_no_thanks_button) { dialog ->
                dialog.dismiss()
            }
        }.show()
    }
}
