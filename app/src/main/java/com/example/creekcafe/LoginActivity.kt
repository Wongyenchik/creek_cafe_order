package com.example.creekcafe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val email = findViewById<EditText>(R.id.etEmail)
        val password = findViewById<EditText>(R.id.etPassword)
        val login = findViewById<Button>(R.id.btnLogin)
        val register = findViewById<Button>(R.id.btnRegister)
        val btnForgetPassword = findViewById<Button>(R.id.btnForgetPassword)

        mAuth = FirebaseAuth.getInstance()
        login.setOnClickListener{
            val txt_email = email.text.toString()
            val txt_password = password.text.toString()
            if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                Toast.makeText(this, "Please fill up all field", Toast.LENGTH_SHORT).show()
            } else{
                loginUser(txt_email, txt_password)
            }
        }

        register.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        btnForgetPassword.setOnClickListener{
            startActivity(Intent(this, ForgetPasswordActivity::class.java))
        }
    }

    private fun loginUser(email: String, password: String) {
        //logic for login user
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //code for logging in user
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}