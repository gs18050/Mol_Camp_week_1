package com.example.tabapplication

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tabapplication.databinding.ActivityMainBinding
import androidx.lifecycle.ViewModelProvider

class SharedViewModel : ViewModel() {
    private val _sharedData = MutableLiveData<Int>()
    val sharedData: LiveData<Int> get() = _sharedData
    private val _sharedFlag = MutableLiveData<Boolean>()
    val sharedFlag: LiveData<Boolean> get() = _sharedFlag
    private val _currentTab = MutableLiveData<Int>()
    val currentTab: LiveData<Int> get() = _currentTab

    fun updateData(newData: Int) {
        _sharedData.value = newData
    }

    fun updateFlag(newFlag: Boolean) {
        _sharedFlag.value = newFlag
    }

    fun updateTab(newTab: Int) {
        _currentTab.value = newTab
    }
}

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.updateData(-1)
        sharedViewModel.updateFlag(true)
        sharedViewModel.updateTab(0)

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_tab1 -> {
                    sharedViewModel.updateTab(0)
                    true
                }
                R.id.navigation_tab2 -> {
                    sharedViewModel.updateTab(1)
                    true
                }
                R.id.navigation_tab3 -> {
                    sharedViewModel.updateTab(2)
                    true
                }
                else -> false
            }
        }

        sharedViewModel.currentTab.observe(this) { tabIndex ->
            navigateToTab(tabIndex)
        }
    }

    fun navigateToTab(tabIndex: Int) {
        when (tabIndex) {
            0 -> navController.navigate(R.id.navigation_tab1)
            1 -> navController.navigate(R.id.navigation_tab2)
            2 -> navController.navigate(R.id.navigation_tab3)
        }
    }
}