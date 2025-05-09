package com.dead.ship.even.sank.flashbright250506.ui2


import android.content.Intent
import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dead.ship.even.sank.flashbright250506.databinding.ActivityGuideBinding
import com.dead.ship.even.sank.flashbright250506.databinding.ActivityLightBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LightActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLightBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLightBinding.inflate(layoutInflater)
        setContentView(binding.root)
        this.supportActionBar?.hide()
        onBackPressedDispatcher.addCallback {
        }
        binding.imgLightL.setOnClickListener {
            lifecycleScope.launch {
                finish()
            }
        }
        setScreenBrightness(1.0f)
    }

    private fun setScreenBrightness(brightness: Float) {
        val layoutParams = window.attributes
        layoutParams.screenBrightness = brightness
        window.attributes = layoutParams
    }
}