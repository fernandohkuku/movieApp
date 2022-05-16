package com.fernando.ui_ktx.content

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.fernando.ui_ktx.kotlin.castTo
import com.fernando.ui_ktx.navigation.getDestinationIdFromAction
import com.google.android.gms.maps.SupportMapFragment


fun Fragment.appSettings(applicationId: String) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    intent.data = Uri.parse("package:$applicationId")
    startActivity(intent)
}

fun Fragment.getMapFragment(map: Int): SupportMapFragment {
    castTo<SupportMapFragment>(childFragmentManager.findFragmentById(map)).also { supportMapFragment ->
        return supportMapFragment
    }
}

fun Fragment.navigate(@IdRes actionId: Int) {
    if (!isAlreadyAtDestination(actionId)) {
        findNavController().navigate(actionId)
    }
}

fun Fragment.navigate(directions: NavDirections) {
    if (!isAlreadyAtDestination(directions.actionId)) {
        findNavController().navigate(directions)
    }
}

private fun Fragment.isAlreadyAtDestination(@IdRes actionId: Int): Boolean {
    val previousDestinationId = previousDestination()?.getDestinationIdFromAction(actionId)
    val currentDestinationId = currentDestination()?.id
    return previousDestinationId == currentDestinationId
}

fun Fragment.previousDestination() = findNavController().previousBackStackEntry?.destination


fun Fragment.currentDestination() = findNavController().currentDestination

fun Fragment.onBackPressed() = requireActivity().onBackPressed()

fun Fragment.goBack() = findNavController().popBackStack()
