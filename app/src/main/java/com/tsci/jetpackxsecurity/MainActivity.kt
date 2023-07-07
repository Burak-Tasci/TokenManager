package com.tsci.jetpackxsecurity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textViewToken = findViewById<AppCompatTextView>(R.id.textViewToken)
        val editTextToken = findViewById<AppCompatEditText>(R.id.editTextToken)
        val buttonEncryptToken = findViewById<AppCompatButton>(R.id.buttonEncryptToken)
        val buttonDecryptToken = findViewById<AppCompatButton>(R.id.buttonDecryptToken)

        buttonEncryptToken.setOnClickListener {
            tokenManager.setToken(editTextToken.text.toString())
        }
        buttonDecryptToken.setOnClickListener {
            textViewToken.text = tokenManager.getToken()
        }
    }
}