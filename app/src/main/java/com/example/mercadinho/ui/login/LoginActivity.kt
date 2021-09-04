package com.example.mercadinho.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import com.example.mercadinho.MainActivity
import com.example.mercadinho.databinding.ActivityLoginBinding

import com.example.mercadinho.R
import com.example.mercadinho.getIntentForMainActivity
import com.example.mercadinho.repository.local.LocalSharedPref
import com.example.mercadinho.util.showToast
import com.example.mercadinho.util.startActivitySlide
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