package com.colodori.mercadinho.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.colodori.mercadinho.databinding.ActivityLoginBinding
import com.colodori.mercadinho.getIntentForMainActivity
import com.colodori.mercadinho.repository.local.LocalSharedPref
import com.colodori.mercadinho.util.showToast
import com.colodori.mercadinho.util.startActivitySlide
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setListenter()
        checkLoggedIn()
    }

    private fun checkLoggedIn() {
      val a = FirebaseAuth.AuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let {
                startActivitySlide(getIntentForMainActivity(), finalize = true)
            }
        }
        FirebaseAuth.getInstance().addAuthStateListener(a)
    }

    private fun setListenter() {
        binding.loading.setOnClickListener {
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build()
            )
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(),
                4
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 4) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                user?.email
                LocalSharedPref.userName = binding.loginNickname.text.toString()
                startActivitySlide(getIntentForMainActivity(), finalize = true)
            } else {
                val error = response?.error
                error?.message?.let { showToast(it) }
            }
        }
    }
}