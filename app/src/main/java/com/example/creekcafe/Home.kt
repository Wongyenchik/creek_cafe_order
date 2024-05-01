import android.R.string
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.creekcafe.R
import com.example.creekcafe.User
import com.example.creekcafe.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Reference to the BottomNavigationView in the activity
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize bottomNavigationView
        bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
        val txtWelcomeMessage = binding.txtWelcomeMessage

        user = auth.currentUser!!
        val mDbRef = FirebaseDatabase.getInstance().getReference()

        mDbRef.child("user").addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(postSnapshot in snapshot.children){
                    if (postSnapshot.key == user?.uid) {
                        val name = postSnapshot.child("name").getValue(String::class.java)
                        txtWelcomeMessage.text = "Hi, $name!"
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled
            }
        })
        // Find the button in the layout
        val button = binding.btnOrderNavi

        // Set click listener for the button
        button.setOnClickListener {

            // Update the selected item in the bottom navigation view
            bottomNavigationView.selectedItemId = R.id.menu
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
