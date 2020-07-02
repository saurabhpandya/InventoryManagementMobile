package com.fidato.inventorymngmnt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.fidato.inventorymngmnt.databinding.MainActivityBinding

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

        appBarConfiguration = AppBarConfiguration(setOf(R.id.categoryFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

}