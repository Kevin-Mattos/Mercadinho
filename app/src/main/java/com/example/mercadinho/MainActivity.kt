package com.example.mercadinho

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.mercadinho.databinding.MainActivityBinding
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    interface FabAction {
        fun fabClicked()
    }

    var fabCallback: (() -> Unit)? = null

    private val mBinding by lazy { setupBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)

        Log.d(BuildConfig.FOO_STRING, ": ${BuildConfig.FOO}")
    }

    override fun onStart() {
        super.onStart()
        setupFab()
        addDestinationChangeListener()
    }

    private fun addDestinationChangeListener() {
        val listerner = NavController.OnDestinationChangedListener { controller, destination, arguments ->
            mBinding.myTitle.text = destination.label
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

fun Context.getIntentForMainActivity() = Intent(this, MainActivity::class.java)