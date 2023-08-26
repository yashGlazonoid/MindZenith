package com.devinfusion.mindzenith

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.devinfusion.mindzenith.activity.chatActivity
import com.devinfusion.mindzenith.databinding.ActivityMainBinding
import java.util.Random


class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding;
    private lateinit var random: Random
    private lateinit var handler : Handler
    private lateinit var affirmations : ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        affirmations = ArrayList<String>()
        affirmations.add("You are capable of great things.")
        affirmations.add("Every day is a new opportunity.")
        affirmations.add("You are resilient and strong.")
        affirmations.add("You have the power to overcome any challenge.")
        affirmations.add("Your potential is limitless.")
        affirmations.add("You radiate positivity and light.")
        affirmations.add("You are deserving of love and happiness.")
        affirmations.add("Your efforts will pay off in the long run.")
        affirmations.add("You are in control of your own happiness.")
        affirmations.add("Every day is a new opportunity to grow.")
        affirmations.add("You are resilient and can handle anything that comes your way.")
        affirmations.add("Your dreams are within reach, keep pushing forward.")


        random = Random()
        handler = Handler(Looper.getMainLooper())

        updateAffirmation();

        binding.chatButton.setOnClickListener{
            startActivity(Intent(this@MainActivity,chatActivity::class.java))
        }


    }

    private fun updateAffirmation() {
        val index: Int = random.nextInt(affirmations.size)
        val affirmation: String = affirmations[index]
        binding.affirmationTextView.text = affirmation

        handler.postDelayed({ updateAffirmation() }, 3000)
    }


    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }
}