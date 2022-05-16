package com.fernando.ui_ktx.navigation

import androidx.annotation.IdRes
import androidx.navigation.NavDestination

fun NavDestination.getDestinationIdFromAction(@IdRes actionId: Int) = getAction(actionId)?.destinationId
