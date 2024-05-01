package com.example.creekcafe

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.auth.User
import java.util.Calendar

class RegisterActivity : AppCompatActivity() {
    private lateinit var datePickerDialog: DatePickerDialog
    private var auth = FirebaseAuth.getInstance()
    private lateinit var mDbRef: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        initDatePicker()
        val dateButton = findViewById<Button>(R.id.birthDate)
        dateButton.text = getTodayDate().toString()
        val email = findViewById<EditText>(R.id.txtEmail)
        val password = findViewById<EditText>(R.id.txtPassword)
        val registerButton = findViewById<Button>(R.id.btnRegister1)
        val loginButton = findViewById<Button>(R.id.btnLogin1)

        registerButton.setOnClickListener{
            val txt_email = email.text.toString()
            val txt_password = password.text.toString()

            if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                Toast.makeText(this, "Empty credentials", Toast.LENGTH_SHORT).show()
            } else if (txt_password.length < 6){
                Toast.makeText(this, "Password too short!", Toast.LENGTH_SHORT).show()
            } else{
                registerUser(txt_email,txt_password)
            }
        }

        loginButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun registerUser(txtEmail: String, txtPassword: String) {
        auth.createUserWithEmailAndPassword(txtEmail, txtPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // code for jumping to home
                    val firstName = findViewById<EditText>(R.id.txtFirstName)
                    val lastName = findViewById<EditText>(R.id.txtLastName)
                    val phoneNumber = findViewById<EditText>(R.id.txtPhoneNumber)
                    val birthDate = findViewById<Button>(R.id.birthDate)
                    val email = findViewById<EditText>(R.id.txtEmail)

                    addUserToDatabase(firstName.text.toString(), lastName.text.toString(), phoneNumber.text.toString(), birthDate.text.toString(), email.text.toString(),  auth.currentUser?.uid!!)
                    Toast.makeText(this, "Register Success", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    Toast.makeText(this, "User already existed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(firstName: String,lastName: String, phoneNumber: String, birthDate: String, email: String, uid: String){
        mDbRef = FirebaseDatabase.getInstance().getReference()
        val fullName = "$firstName $lastName"
        mDbRef.child("user").child(uid).setValue(User(fullName,phoneNumber,birthDate,email, uid))
    }

    private fun getTodayDate(): Any {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH) + 1
        val day = cal.get(Calendar.DAY_OF_MONTH)
        return makeDateString(day,month,year)
    }

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener{ datePicker: DatePicker, year: Int, month: Int, day: Int ->
            val month = month + 1
            val date = makeDateString(day, month,year)
            val dateButton = findViewById<Button>(R.id.birthDate)
            dateButton.text = date
        }
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)

        val style = AlertDialog.THEME_HOLO_LIGHT
        datePickerDialog = DatePickerDialog(this, style,dateSetListener,year,month,day)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

    }

    private fun makeDateString(day: Int, month: Int, year: Int): String {
        return "${getMonthFormat(month)} $day $year"
    }


    private fun getMonthFormat(month: Int): Any {
        return when (month) {
        1 -> "JAN"
        2 -> "FEB"
        3 -> "MAR"
        4 -> "APR"
        5 -> "MAY"
        6 -> "JUN"
        7 -> "JUL"
        8 -> "AUG"
        9 -> "SEP"
        10 -> "OCT"
        11 -> "NOV"
        12 -> "DEC"
        else -> "JAN"
    }

    }

    fun openDatePicker(view: View) {
        datePickerDialog.show()
    }
}