package com.fernando.ui_ktx.content

import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.ContextCompat

fun Context.isInternetAvailable(): Boolean {
    val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    connectivityManager?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            it.getNetworkCapabilities(it.activeNetwork)?.apply {
                if (hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                ) {
                    return when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            }
        } else {
            return it.isSupportConnected
        }
    }
    return false
}


@Suppress("DEPRECATION")
private val ConnectivityManager.isSupportConnected: Boolean
    get() {
        val nwInfo = activeNetworkInfo ?: return false
        return nwInfo.isConnected
    }

fun Context.hasPermissions(vararg permissions: String): Boolean {
    permissions.forEach { permission ->
        if (!hasPermission(permission)) {
            return false
        }
    }

    return true
}
fun Context.hasPermission(permission: String): Boolean =
    ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

