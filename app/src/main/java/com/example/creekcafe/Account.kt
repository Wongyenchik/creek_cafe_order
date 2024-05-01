package com.example.creekcafe

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Account : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val name = requireActivity().findViewById<TextView>(R.id.name)
        val email = requireActivity().findViewById<TextView>(R.id.email)
        val phoneNumber = requireActivity().findViewById<TextView>(R.id.phoneNumber)
        val txtBirthDate = requireActivity().findViewById<TextView>(R.id.txtBirthDate)
        val btnEditProfile = requireActivity().findViewById<Button>(R.id.btnEditProfile)
        val btnDeleteAccount = requireActivity().findViewById<Button>(R.id.btnDeleteAccount)
        val btnLogout = requireActivity().findViewById<Button>(R.id.btnLogout)
        val mDbRef = FirebaseDatabase.getInstance().getReference()
        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        val user: FirebaseUser? = auth.currentUser
        val addDeleteDialog = AlertDialog.Builder(requireContext())
            .setTitle("Delete Account")
            .setMessage("Are you confirm to delete account?")
            .setPositiveButton("Yes"){_,_->
                user?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
//                        Toast.makeText(requireContext(), "User deleted successfully.", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)

                    } else {
                        Toast.makeText(requireContext(), "DeleteAsync encountered an error: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }?.addOnCanceledListener {
                    Toast.makeText(requireContext(), "DeleteAsync was canceled.", Toast.LENGTH_SHORT).show()
                }

                mDbRef.child("user").addValueEventListener(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(postSnapshot in snapshot.children){
                            if (postSnapshot.key == user?.uid) {
                                postSnapshot.ref.removeValue().addOnCompleteListener { dataTask ->
                                    if (dataTask.isSuccessful) {
                                        Toast.makeText(requireContext(), "User data deleted successfully.", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(requireContext(), "Failed to delete user data: ${dataTask.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle onCancelled
                    }
                })
            }
            .setNegativeButton("No"){_,_->

            }
        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    if (postSnapshot.key == user?.uid) {
                        val name1 = postSnapshot.child("name").getValue(String::class.java)
                        val email1 = postSnapshot.child("email").getValue(String::class.java)
                        val phoneNo = postSnapshot.child("phoneNo").getValue(String::class.java)
                        val birthDate = postSnapshot.child("birthDate").getValue(String::class.java)
                        name.text = "$name1"
                        email.text = "$email1"
                        phoneNumber.text = "$phoneNo"
                        txtBirthDate.text = "$birthDate"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })


        btnDeleteAccount.setOnClickListener{
            addDeleteDialog.show()
        }
        btnLogout.setOnClickListener{
            auth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
        btnEditProfile.setOnClickListener{
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }
        val orderhistoryBTN = requireActivity().findViewById<Button>(R.id.orderhistoryBTN)
        orderhistoryBTN.setOnClickListener{
            startActivity(Intent(requireContext(),OrderHistoryActivity::class.java))
        }

    }
}