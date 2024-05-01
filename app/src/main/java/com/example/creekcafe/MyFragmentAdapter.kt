import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.creekcafe.About_us
import com.example.creekcafe.About_us_coffee
import com.example.creekcafe.About_us_contact
import com.example.creekcafe.about_us_main

class MyFragmentAdapter(fragmentActivity: About_us, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        // Return the total number of fragments
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        // Return the fragment instance based on position
        return when (position) {
            0 ->  about_us_main()
            1 -> About_us_coffee()
            2 -> About_us_contact()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }
    }
}
