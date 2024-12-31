package com.example.tabapplication

import android.content.Intent
import android.graphics.Matrix
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
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        setContentView(R.layout.activity_logo)

        val nupjukImageView = findViewById<ImageView>(R.id.logo_nupjuk)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        fun setLayoutParams(layoutParams: ViewGroup.MarginLayoutParams, left: Int, top: Int, right: Int, bottom: Int, width: Int, height: Int): ViewGroup.MarginLayoutParams {
            layoutParams.setMargins(left, top, right, bottom)
            layoutParams.width=width
            layoutParams.height=height

            return layoutParams
        }

        val scale = 0.2
        val aspectRatio = 1238.0 / 791.0

        val scaledWidth = (displayMetrics.widthPixels * scale).toInt()
        val scaledHeight = (scaledWidth * aspectRatio).toInt()

        val horizontalMargin = ((displayMetrics.widthPixels - scaledWidth) / 2).toInt()
        val verticalMargin = ((displayMetrics.heightPixels - scaledHeight) / 2).toInt()

        nupjukImageView.layoutParams = setLayoutParams(
            nupjukImageView.layoutParams as ViewGroup.MarginLayoutParams,
            horizontalMargin,
            verticalMargin,
            horizontalMargin,
            verticalMargin,
            scaledWidth,
            scaledHeight
        )

        Glide.with(this)
            .load(R.drawable.menu_nupjuk)
            .override(scaledWidth, scaledHeight)
            .centerInside()
            .into(nupjukImageView)

        fun applyAnimation(imageViews: List<ImageView>, animation: Animation, visible: Boolean) {
            for (imv in imageViews) {
                imv.startAnimation(animation)
                if (visible) imv.visibility=View.VISIBLE
                else imv.visibility=View.INVISIBLE
            }
        }

        Handler().postDelayed({
            val intent = Intent(this@LogoActivity, MainActivity::class.java)
            startActivity(intent)
            overridePendingTransition(0, 0)
            finish()
        }, 2000)
    }
}