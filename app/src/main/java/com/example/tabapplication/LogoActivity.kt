package com.example.tabapplication

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class LogoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo)

        val backgroundImageView = findViewById<ImageView>(R.id.logo_background)
        val nupjukImageView = findViewById<ImageView>(R.id.logo_nupjuk)
        val titleImageView = findViewById<ImageView>(R.id.logo_title)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        Glide.with(this)
            .load(R.drawable.ic_logo_background)
            .override(displayMetrics.widthPixels, displayMetrics.heightPixels)
            .centerCrop()
            .into(backgroundImageView)

        fun setLayoutParams(layoutParams: ViewGroup.MarginLayoutParams, left: Int, top: Int, right: Int, bottom: Int, width: Int, height: Int): ViewGroup.MarginLayoutParams {
            layoutParams.setMargins(left, top, right, bottom)
            layoutParams.width=width
            layoutParams.height=height
            return layoutParams
        }

        nupjukImageView.layoutParams=setLayoutParams(
            nupjukImageView.layoutParams as ViewGroup.MarginLayoutParams,
            0,
            (displayMetrics.heightPixels*0.2).toInt(),
            0,
            0,
            (displayMetrics.widthPixels*0.65).toInt(),
            (displayMetrics.widthPixels*0.65/1044*775).toInt())

        titleImageView.layoutParams=setLayoutParams(
            titleImageView.layoutParams as ViewGroup.MarginLayoutParams,
            0,
            (displayMetrics.heightPixels*0.5).toInt(),
            0,
            0,
            (displayMetrics.widthPixels*0.9).toInt(),
            (displayMetrics.widthPixels*0.9/1044*775).toInt())

        fun applyAnimation(imageViews: List<ImageView>, animation: Animation, visible: Boolean) {
            for (imv in imageViews) {
                imv.startAnimation(animation)
                if (visible) imv.visibility=View.VISIBLE
                else imv.visibility=View.INVISIBLE
            }
        }

        //val fadeIn = AlphaAnimation(0.1f, 1f)
        //fadeIn.duration = 2000
        //applyAnimation(listOf(backgroundImageView,nupjukImageView,titleImageView),fadeIn,true)

        Handler().postDelayed({
            val fadeOut = AlphaAnimation(1f, 0.1f)
            fadeOut.duration = 1500
            applyAnimation(listOf(backgroundImageView,nupjukImageView,titleImageView),fadeOut,false)

            Handler().postDelayed({
                val intent = Intent(this@LogoActivity, MainActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }, fadeOut.duration)
        }, 2000)
    }
}