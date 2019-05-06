package com.example.mundim.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import com.android.volley.*
import com.example.mundim.Fragments.CameraFragment
import com.example.mundim.Fragments.SampleDataFragment
import com.example.mundim.Fragments.PatientDataFragment
import com.example.mundim.R
import kotlinx.android.synthetic.main.activity_new_patient.*
import org.jetbrains.anko.toast
import java.util.ArrayList
import java.util.HashMap

class NewPatientAdapter(fragmentManager: FragmentManager) :
    FragmentStatePagerAdapter(fragmentManager) {

    // 2
    override fun getItem(position: Int): Fragment {
        return PatientDataFragment()
    }

    override fun getCount(): Int {
        return 1
    }
}

class NewPatientActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager
    private lateinit var pageAdapter: NewPatientAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_patient)

        viewPager = findViewById(R.id.viewPager2)
        pageAdapter = NewPatientAdapter(supportFragmentManager)
        viewPager.adapter = pageAdapter
        backBtn.setOnClickListener{
            onBackPressed()
        }
        optionsBtn.setOnClickListener {
            toast("Estamos trabalhando nisso! Virá junto com a função de login!")
        }
    }

}
