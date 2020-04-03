package com.glennreilly.androidproficiencyexercise.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.glennreilly.androidproficiencyexercise.R
import com.glennreilly.androidproficiencyexercise.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportFragmentManager.beginTransaction()
            .replace(
                binding.container.id,
                FactListFragment()
            ).commit()
    }
}
