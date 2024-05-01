import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.creekcafe.About_us
import com.example.creekcafe.About_us_coffee
import com.example.creekcafe.About_us_contact
import com.example.creekcafe.CurrentOrder
import com.example.creekcafe.OrderHistory
import com.example.creekcafe.OrderHistoryActivity
import com.example.creekcafe.about_us_main

class OrderFragmentAdapter(fragmentActivity: OrderHistoryActivity, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        // Return the total number of fragments
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        // Return the fragment instance based on position
        return when (position) {
            0 -> CurrentOrder()
            1 -> OrderHistory()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
