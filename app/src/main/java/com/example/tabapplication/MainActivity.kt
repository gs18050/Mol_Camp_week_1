package com.example.tabapplication

import android.os.Bundle
import android.util.Log
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
import androidx.navigation.NavOptions

class SharedViewModel : ViewModel() {
    private val _sharedData = MutableLiveData<Int>()
    val sharedData: LiveData<Int> get() = _sharedData
    private val _currentTab = MutableLiveData<Int>()
    val currentTab: LiveData<Int> get() = _currentTab
    private val _pingSelect = MutableLiveData<Int>()
    val pingSelect: LiveData<Int> get() = _pingSelect
    private val _isTabChanging = MutableLiveData<Boolean>(false)
    val isTabChanging: LiveData<Boolean> get() = _isTabChanging
    private val _tabDir = MutableLiveData<Int>(0)
    val tabDir: LiveData<Int> get() = _tabDir

    fun setTabDir(newDir: Int) {
        _tabDir.value = newDir
    }

    fun setTabChanging(isChanging: Boolean) {
        _isTabChanging.value = isChanging
    }

    fun updateData(newData: Int) {
        _sharedData.value = newData
    }

    fun updateTab(newTab: Int) {
        _currentTab.value = newTab
    }

    fun updatePing(newPing: Int) {
        _pingSelect.value = newPing
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
        sharedViewModel.updateTab(0)
        sharedViewModel.setTabChanging(false)

        val navView: BottomNavigationView = binding.navView
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        navView.setupWithNavController(navController)
        navView.setOnItemSelectedListener { item ->
            val currentTab = sharedViewModel.currentTab.value ?: 0
            when (item.itemId) {
                R.id.navigation_tab1 -> {
                    sharedViewModel.setTabDir(if (currentTab > 0) 2 else 1)
                    sharedViewModel.updateTab(0)
                    true
                }
                R.id.navigation_tab2 -> {
                    sharedViewModel.setTabDir(if (currentTab < 1) 1 else 2)
                    sharedViewModel.updateTab(1)
                    true
                }
                R.id.navigation_tab3 -> {
                    sharedViewModel.setTabDir(if (currentTab < 2) 1 else 2)
                    sharedViewModel.updateTab(2)
                    true
                }
                else -> false
            }
        }

        sharedViewModel.currentTab.observe(this) { tabIndex ->
            Log.d("currentTab", tabIndex.toString())
            val direction = sharedViewModel.tabDir.value ?: 0
            navigateToTab(tabIndex, direction)
        }
    }

    fun navigateToTab(tabIndex: Int, direction: Int) {
        val navOptions = when (direction) {
            1 -> NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .setPopEnterAnim(R.anim.slide_in_left)
                .setPopExitAnim(R.anim.slide_out_right)
                .build()
            2 -> NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_left)
                .setExitAnim(R.anim.slide_out_right)
                .setPopEnterAnim(R.anim.slide_in_right)
                .setPopExitAnim(R.anim.slide_out_left)
                .build()
            else -> NavOptions.Builder().build()
        }

        when (tabIndex) {
            0 -> navController.navigate(R.id.navigation_tab1, null, navOptions)
            1 -> navController.navigate(R.id.navigation_tab2, null, navOptions)
            2 -> navController.navigate(R.id.navigation_tab3, null, navOptions)
        }
    }
}