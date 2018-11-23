package foodfacts.eatwell.com.foodfacts.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import foodfacts.eatwell.com.foodfacts.R

import foodfacts.eatwell.com.foodfacts.util.Constants

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mainMenu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            openBarcodeCaptureActivity()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        mainMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        R.id.auto_focus -> consumeAction { setAutoFocus(item) }
        R.id.use_flash -> consumeAction { setFlash(item) }
        else -> super.onOptionsItemSelected(item)
    }

    private fun openBarcodeCaptureActivity() {
        // Launch barcode activity
        val intent = Intent(this, BarcodeCaptureActivity::class.java)
        intent.putExtra(Constants.AutoFocus, mainMenu.findItem(R.id.auto_focus).isChecked)
        intent.putExtra(Constants.UseFlash, mainMenu.findItem(R.id.use_flash).isChecked)

        startActivity(intent)
    }

    private fun setAutoFocus(item: MenuItem) {
        item.isChecked = !item.isChecked
        Log.v(TAG, "setAutoFocus - Auto focus enabled ${item.isChecked}")
    }

    private fun setFlash(item: MenuItem) {
        item.isChecked = !item.isChecked
        Log.v(TAG, "setFlash - Flash enabled ${item.isChecked}")
    }

    private inline fun consumeAction(f: () -> Unit): Boolean {
        f()
        return true
    }

    fun launchDetailView(barcode: String) {
        val data = Intent(this, BarcodeDetailActivity::class.java)
        data.putExtra(Constants.BarcodeObject, barcode)
        startActivity(data)
    }
}
