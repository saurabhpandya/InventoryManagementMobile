package com.fidato.inventorymngmnt

import android.app.Application
import android.content.Context
import java.lang.ref.WeakReference

class InventoryManagementApp : Application() {
    override fun onCreate() {
        super.onCreate()
        wApp!!.clear()
        wApp = WeakReference(this@InventoryManagementApp)

    }

    companion object {
        private var wApp: WeakReference<InventoryManagementApp>? =
            WeakReference<InventoryManagementApp>(null)!!
        val instance: InventoryManagementApp get() = wApp?.get()!!

        val context: Context
            get() {
                val app = if (null != wApp) wApp!!.get() else InventoryManagementApp()
                return if (app != null) app.applicationContext else InventoryManagementApp().applicationContext
            }
    }
}