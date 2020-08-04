package com.example.luasapp.ui.common

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.luasapp.R
import com.example.luasapp.utils.CheckNetwork
import com.example.luasapp.utils.NetworkListener
import com.kaopiz.kprogresshud.KProgressHUD
import kotlin.properties.Delegates

open class BaseActivity: AppCompatActivity(), NetworkListener {

    private var progress: KProgressHUD? = null
    private var noInternetToast: Toast? = null

    protected var isLoading: Boolean by Delegates.observable(false) { _, _, newValue ->
        if (newValue) showProgress() else dismissProgress()
    }

    protected var isConnected: Boolean by Delegates.observable(false) { _, _, newValue ->
        if (!newValue) showNoInternetConnection()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CheckNetwork(applicationContext, this)
    }

    private fun initProgressHud() {
        progress = KProgressHUD
            .create(this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setDimAmount(0.5f)
            .setCancellable(false)
    }

    private fun showProgress() {
        if (progress == null) initProgressHud()

        progress?.show()
    }

    private fun dismissProgress() {
        progress?.dismiss()
    }

    @SuppressLint("ShowToast")
    protected fun showNoInternetConnection() {
        if (noInternetToast == null) {
            noInternetToast = Toast.makeText(
                this,
                getString(R.string.error_no_internet_connection),
                Toast.LENGTH_SHORT
            )
        }
        noInternetToast?.show()
    }

    /**
     * [NetworkListener]
     */

    override fun onConnectionAvailable(isConnected: Boolean) {
        this.isConnected = isConnected
    }
}