package com.example.tabapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class LogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        val logoImageView = findViewById<ImageView>(R.id.splash_logo)

        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 2000
        logoImageView.startAnimation(fadeIn)
        logoImageView.visibility = View.VISIBLE

        Handler().postDelayed({
            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.duration = 1500
            logoImageView.startAnimation(fadeOut)
            logoImageView.visibility = View.INVISIBLE

            Handler().postDelayed({
                val intent = Intent(this@LogoActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }, fadeOut.duration)
        }, 2500)
    }
}