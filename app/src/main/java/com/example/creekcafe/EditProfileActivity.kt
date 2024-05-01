package com.example.creekcafe

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var datePickerDialog: DatePickerDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        val btnBack = findViewById<Button>(R.id.btnBack)
        val txtName = findViewById<EditText>(R.id.txtName)
        val txtEmail = findViewById<EditText>(R.id.txtEmail)
        val txtPhone = findViewById<EditText>(R.id.txtPhone)
        val dateButton = findViewById<Button>(R.id.txtBirthDate)
        val updateButton = findViewById<Button>(R.id.updateButton)
        val mDbRef = FirebaseDatabase.getInstance().getReference()
//        val auth: FirebaseAuth = FirebaseAuth.getInstance()
//        val user: FirebaseUser? = auth.currentUser
        val user = Firebase.auth.currentUser
        initDatePicker()

        // In your activity or fragment code

        val dialogView = layoutInflater.inflate(R.layout.reauthentication_dialog, null)
        val editTextEmail = dialogView.findViewById<EditText>(R.id.editTextEmail)
        val editTextPassword = dialogView.findViewById<EditText>(R.id.editTextPassword)
        val buttonSubmit = dialogView.findViewById<Button>(R.id.buttonSubmit)
        val buttonCancel = dialogView.findViewById<Button>(R.id.buttonCancel)

        val alertDialogBuilder = AlertDialog.Builder(this)
        alertDialogBuilder.setView(dialogView)
        val alertDialog = alertDialogBuilder.create()

        buttonSubmit.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()

            // Reauthenticate the user with the provided email and password
            val credential = EmailAuthProvider.getCredential(email, password)
            user?.reauthenticate(credential)
                ?.addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        // Reauthentication successful, dismiss the dialog
                        alertDialog.dismiss()

                        // Perform sensitive operation after successful reauthentication
                        // For example, update the email address
                    } else {
                        // Failed to reauthenticate, show an error message
                        Toast.makeText(this, "Failed to reauthenticate. Please check your email and password.", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        buttonCancel.setOnClickListener {
            // Dismiss the dialog if canceled
            alertDialog.dismiss()
            finish()
        }
        alertDialog.setCanceledOnTouchOutside(false)

// Show the dialog
        alertDialog.show()


        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    if (postSnapshot.key == user?.uid) {
                        val name1 = postSnapshot.child("name").getValue(String::class.java)
                        val email1 = postSnapshot.child("email").getValue(String::class.java)
                        val phoneNo = postSnapshot.child("phoneNo").getValue(String::class.java)
                        val birthDate = postSnapshot.child("birthDate").getValue(String::class.java)
                        txtName.hint = "$name1"
                        txtEmail.hint = "$email1"
                        txtPhone.hint = "$phoneNo"
                        dateButton.text = "$birthDate"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
        btnBack.setOnClickListener{
            finish()
        }

        updateButton.setOnClickListener{
            val email1 = editTextEmail.text.toString()
            val password1 = editTextPassword.text.toString()

            val credential = EmailAuthProvider
                .getCredential(email1, password1)
            user?.reauthenticate(credential)
                ?.addOnSuccessListener {
                    if (txtEmail.text.toString().trim().isEmpty()){
                        Toast.makeText(this, "User data updated", Toast.LENGTH_SHORT).show()
                        var name = txtName.text.toString()
                        var phoneNo = txtPhone.text.toString()
                        var birthDate = dateButton.text.toString()

                        if (name.isEmpty()) {
                            name = txtName.hint.toString()
                        }
                        if (phoneNo.isEmpty()) {
                            phoneNo = txtPhone.hint.toString()
                        }
                        if (birthDate.isEmpty()) {
                            birthDate = dateButton.hint.toString()
                        }

                        val currentUserUid = Firebase.auth.currentUser?.uid
                        if (currentUserUid != null) {
                            val currentUserRef = FirebaseDatabase.getInstance().getReference("user").child(currentUserUid)

                            // Update the user data in the database
                            currentUserRef.child("name").setValue(name)
                            currentUserRef.child("phoneNo").setValue(phoneNo)
                            currentUserRef.child("birthDate").setValue(birthDate)
                                .addOnSuccessListener {
//                                    Toast.makeText(this, "User data updated successfully else", Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                                .addOnFailureListener { e ->
//                                    Toast.makeText(this, "Failed to update user data: else ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    } else{
//                        Toast.makeText(this, "testing ${txtEmail.text.toString()}", Toast.LENGTH_SHORT).show()

                        user?.verifyBeforeUpdateEmail(txtEmail.text.toString().trim())
                            ?.addOnSuccessListener {
                                Toast.makeText(this, "Please check your email to confirm change email.", Toast.LENGTH_SHORT).show()

                                var name = txtName.text.toString()
                                var email = txtEmail.text.toString()
                                var phoneNo = txtPhone.text.toString()
                                var birthDate = dateButton.text.toString()

                                if (name.isEmpty()) {
                                    name = txtName.hint.toString()
                                }
                                if (email.isEmpty()) {
                                    email = txtEmail.hint.toString()
                                }
                                if (phoneNo.isEmpty()) {
                                    phoneNo = txtPhone.hint.toString()
                                }
                                if (birthDate.isEmpty()) {
                                    birthDate = dateButton.hint.toString()
                                }

                                val currentUserUid = Firebase.auth.currentUser?.uid
                                if (currentUserUid != null) {
                                    val currentUserRef = FirebaseDatabase.getInstance().getReference("user").child(currentUserUid)

                                    // Update the user data in the database
                                    currentUserRef.child("name").setValue(name)
                                    currentUserRef.child("email").setValue(email)
                                    currentUserRef.child("phoneNo").setValue(phoneNo)
                                    currentUserRef.child("birthDate").setValue(birthDate)
                                        .addOnSuccessListener {
//                                            Toast.makeText(this, "User data updated successfully", Toast.LENGTH_SHORT).show()
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
//                                            Toast.makeText(this, "Failed to update user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }

                            }
                    }
                }
        }

    }

    private fun initDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener{ datePicker: DatePicker, year: Int, month: Int, day: Int ->
            val month = month + 1
            val date = makeDateString(day, month,year)
            val dateButton = findViewById<Button>(R.id.txtBirthDate)
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

    private fun makeDateString(day: Int, month: Int, year: Int): CharSequence? {
        return "${getMonthFormat(month)} $day $year"

    }

    private fun getMonthFormat(month: Int): String {
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