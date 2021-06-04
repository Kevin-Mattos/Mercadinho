package com.example.mercadinho

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.example.mercadinho.databinding.MainActivityBinding
import com.example.mercadinho.view.extensions.makeTransaction
import com.example.mercadinho.view.fragments.GROUP_ID_KEY
import com.example.mercadinho.view.fragments.ShopGroupFragment
import com.example.mercadinho.view.fragments.ShopItemFragment

class MainActivity : AppCompatActivity() {

    interface FabAction {
        fun fabClicked()
    }

    private val mBinding by lazy { setupBinding() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(mBinding.container.id, ShopGroupFragment.newInstance())
                .commitNow()
        }
        setupFab()
    }

    private fun setupBinding(): MainActivityBinding {
        return MainActivityBinding.inflate(layoutInflater)
    }

    private fun setupFab() {
        mBinding.fab.setOnClickListener {
            Toast.makeText(applicationContext, "eaeaea", Toast.LENGTH_LONG).show()
            val frag = supportFragmentManager.findFragmentById(mBinding.container.id)
            if (frag != null && frag is FabAction) {
                frag.fabClicked()
            }
        }
    }

    fun replaceThis(groupId: Long) {
        val frag = ShopItemFragment.newInstance()

        frag.arguments = Bundle()
        frag.arguments?.putLong(GROUP_ID_KEY, groupId)

        makeTransaction {
            replace(mBinding.container.id, frag)
        }
    }
}