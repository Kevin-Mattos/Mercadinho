package com.example.mercadinho

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.mercadinho.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    interface FabAction {
        fun fabClicked()
    }

    var fabCallback: (() -> Unit)? = null

    private val mBinding by lazy { setupBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
    }

    override fun onStart() {
        super.onStart()
        setupFab()
        addDestinationChangeListener()
    }

    private fun addDestinationChangeListener() {
        val listerner = NavController.OnDestinationChangedListener { controller, destination, arguments ->
            title = destination.label
        }
        findNavController(R.id.nav_host_fragment_container).addOnDestinationChangedListener(listerner)

    }

    private fun setupBinding(): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater)
    }

    private fun setupFab() {
        mBinding.fab.setOnClickListener {
            fabCallback?.invoke()
        }
    }
}