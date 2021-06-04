package com.example.mercadinho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mercadinho.databinding.MainActivityBinding
import com.example.mercadinho.ui.main.MainFragment
import org.jetbrains.annotations.NotNull

class MainActivity : AppCompatActivity() {

    private val mBinding by lazy {
        setupBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(mBinding.container.id, MainFragment.newInstance())
                    .commitNow()
        }
    }

    private fun setupBinding(): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater)
    }
}