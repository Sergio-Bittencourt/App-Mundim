package com.example.mundim.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.example.mundim.Fragments.CameraFragment
import com.example.mundim.Fragments.SampleDataFragment
import com.example.mundim.R
import kotlinx.android.synthetic.main.activity_new_sample.*
import org.jetbrains.anko.toast

class NewSampleAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    // 2
    override fun getItem(position: Int): Fragment {
        if (position == 0){
            return CameraFragment()
        }
        return SampleDataFragment()
    }

    override fun getCount(): Int {
        return 2
    }
}

class NewSampleActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pageAdapter: NewSampleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_sample)

        viewPager = findViewById(R.id.viewPager2)
        pageAdapter = NewSampleAdapter(supportFragmentManager)
        viewPager.adapter = pageAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        backBtn.setOnClickListener{
            onBackPressed()
        }
    }
}
