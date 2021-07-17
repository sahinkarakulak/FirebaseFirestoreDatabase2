package com.mrcaracal.firebasefirestoredatabase2.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mrcaracal.firebasefirestoredatabase2.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        binding.imgCreateAccount.setOnClickListener {
            val email = binding.edtUserEmil.text.toString()
            val pass1 = binding.edtUserPass1.text.toString()
            val pass2 = binding.edtUserPass2.text.toString()

            if (email.equals("") || pass1.equals("") || pass2.equals("")) {
                Toast.makeText(applicationContext, "Please enter data", Toast.LENGTH_SHORT).show()
            } else if (!pass1.equals(pass2)) {
                Toast.makeText(applicationContext, "Passwords are not the same", Toast.LENGTH_SHORT)
                    .show()
            } else {
                auth.createUserWithEmailAndPassword(email, pass1).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Verify your e-mail account",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(applicationContext, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }
                    }
                }.addOnFailureListener { exception ->
                    if (exception != null) {
                        Toast.makeText(
                            applicationContext,
                            exception.localizedMessage,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.txtGoToLoginScreen.setOnClickListener {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}