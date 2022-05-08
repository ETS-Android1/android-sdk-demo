package com.geomoby.demoapp.ui.discount

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.geomoby.demoapp.R
import com.geomoby.demoapp.databinding.ActivityDiscountBinding

class DiscountActivityNew:AppCompatActivity() {

    private lateinit var binding:ActivityDiscountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.discountBackButton.setOnClickListener { finish() }

        when (intent.getStringExtra("id")) {
            "Enter" -> changeState("Welcome to our venue", R.mipmap.hotel, R.mipmap.bg_1)
            "Exit" -> changeState(
                "Good Bye and see you again soon",
                R.mipmap.good_bye,
                R.mipmap.bg_3
            )
            "Drink" -> changeState("Get one drink for only $5", R.mipmap.drink, R.mipmap.bg_4)
            "Offer" -> changeState("Today's Special offer", R.mipmap.offer, R.mipmap.bg_2)
        }
    }

    private fun changeState(text: String, image: Int, background: Int) {
        onSetText(text)
        onSetImage(image)
        onSetBackground(background)
    }

    private fun onSetBackground(background: Int) {
        binding.discountBackground.setImageResource(background)
    }

    private fun onSetText(text: String?) {
        binding.discountText.text = text
    }

    private fun onSetImage(image: Int) {
        binding.discountImage.setImageResource(image)
    }

}