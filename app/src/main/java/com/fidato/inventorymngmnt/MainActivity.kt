package com.fidato.inventorymngmnt

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.fidato.inventorymngmnt.databinding.MainActivityBinding
import com.fidato.inventorymngmnt.utility.hideKeyboard

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.canonicalName

    private lateinit var binding: MainActivityBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var host: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = setContentView<MainActivityBinding>(
            this,
            R.layout.main_activity
        )
        setSupportActionBar(binding.toolbar)

        setupNavigationHost()

    }

    private fun setupNavigationHost() {

        host = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(setOf(R.id.dashboardFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        viewModelStore.clear()
        hideKeyboard()
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return true
    }

    override fun onBackPressed() {
        viewModelStore.clear()
        hideKeyboard()
        super.onBackPressed()

    }
}