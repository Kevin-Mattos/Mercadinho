package com.example.mercadinho

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.example.mercadinho.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mBinding by lazy { setupBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        mBinding.toolbar.title = getString(R.string.app_name)
        setSupportActionBar(mBinding.toolbar);
    }

    private fun setupBinding(): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater)
    }

    override fun onBackPressed() {
        if(!Navigation.findNavController(this, R.id.nav_host_fragment_container).popBackStack())
            super.onBackPressed()
    }

}

fun Context.getIntentForMainActivity() = Intent(this, MainActivity::class.java)