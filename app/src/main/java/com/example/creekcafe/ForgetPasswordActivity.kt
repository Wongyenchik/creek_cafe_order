package com.example.creekcafe

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgetPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_password)

        val btnReset = findViewById<Button>(R.id.btnResetPassword)
        val backToLogin = findViewById<Button>(R.id.backToLogin)
        backToLogin.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
        btnReset.setOnClickListener{
            val email = findViewById<EditText>(R.id.etResetEmail)
//            Toast.makeText(this, "${email.text}", Toast.LENGTH_SHORT).show()

            val emailAddress = "${email.text}"

            if(TextUtils.isEmpty(emailAddress)){
                Toast.makeText(this, "Please write your email", Toast.LENGTH_SHORT).show()
            } else{
                Firebase.auth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Log.d(TAG, "Email sent.")
                            Toast.makeText(this, "Please check your email", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                        }
                        else{
                            Toast.makeText(this, "Email did not register.", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}