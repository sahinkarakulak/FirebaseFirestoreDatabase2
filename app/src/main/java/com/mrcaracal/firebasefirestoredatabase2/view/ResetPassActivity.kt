package com.mrcaracal.firebasefirestoredatabase2.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mrcaracal.firebasefirestoredatabase2.databinding.ActivityResetPassBinding

class ResetPassActivity : AppCompatActivity() {

    private lateinit var binding : ActivityResetPassBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPassBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        binding.imgResetPass.setOnClickListener {
            val email = binding.edtUserEmail.text.toString()
            if (email.equals("")) {
                Toast.makeText(applicationContext, "Please enter data", Toast.LENGTH_SHORT).show()
            } else {
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        binding.txtGoToLoginScreen.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}