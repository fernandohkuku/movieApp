package com.fernando.core.presentation.components.permissions

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import com.fernando.ui_ktx.content.hasPermissions

class PermissionLifecycle(private val permission: String) : DefaultLifecycleObserver {
    private lateinit var fragment: Fragment

    private val context: Context
        get() = fragment.requireContext()

    private val hasPermission = MutableLiveData<Boolean>()

    fun attach(fragment: Fragment) {
        this.fragment = fragment
        fragment.lifecycle.addObserver(this)
    }

//    @OnLifecycleEvent(Lifecycle.Event.ON_START)
//    private fun onStart() = with(context) {
//        val newHasPermission = hasPermissions(permission)
//        if (hasPermission.value != newHasPermission) {
//            hasPermission.value = newHasPermission
//        }
//    }
//
//    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
//    private fun onResume() = with(context) {
//        val newHasPermission = hasPermissions(permission)
//        if (hasPermission.value != newHasPermission) {
//            hasPermission.value = newHasPermission
//        }
//    }

    override fun onStart(owner: LifecycleOwner)= with(context) {
        val newHasPermission = hasPermissions(permission)
        if (hasPermission.value != newHasPermission) {
            hasPermission.value = newHasPermission
        }
    }

    override fun onResume(owner: LifecycleOwner) = with(context){
        val newHasPermission = hasPermissions(permission)
        if (hasPermission.value != newHasPermission) {
            hasPermission.value = newHasPermission
        }
    }

    fun observe(observer: Observer<Boolean>) = validate().run {
        hasPermission.observe(fragment.viewLifecycleOwner, observer)
    }

    fun granted() = validate().also {
        hasPermission.value = true
    }

    private fun validate() {
        require(::fragment.isInitialized) { "Call attach first" }
    }
}
