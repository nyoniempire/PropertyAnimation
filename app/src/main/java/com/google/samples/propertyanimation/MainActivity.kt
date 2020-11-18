/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.samples.propertyanimation

import android.animation.*
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationSet
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView


class MainActivity : AppCompatActivity() {

    lateinit var star: ImageView
    lateinit var rotateButton: Button
    lateinit var translateButton: Button
    lateinit var scaleButton: Button
    lateinit var fadeButton: Button
    lateinit var colorizeButton: Button
    lateinit var showerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        star = findViewById(R.id.star)
        rotateButton = findViewById<Button>(R.id.rotateButton)
        translateButton = findViewById<Button>(R.id.translateButton)
        scaleButton = findViewById<Button>(R.id.scaleButton)
        fadeButton = findViewById<Button>(R.id.fadeButton)
        colorizeButton = findViewById<Button>(R.id.colorizeButton)
        showerButton = findViewById<Button>(R.id.showerButton)

        rotateButton.setOnClickListener {
            rotater()
        }

        translateButton.setOnClickListener {
            translater()
        }

        scaleButton.setOnClickListener {
            scaler()
        }

        fadeButton.setOnClickListener {
            fader()
        }

        colorizeButton.setOnClickListener {
            colorizer()
        }

        showerButton.setOnClickListener {
            shower()
        }
    }

    private fun ObjectAnimator.disableViewDuringAnimation(view: View){
        addListener(object: AnimatorListenerAdapter(){
            override fun onAnimationStart(animation: Animator?) {
                super.onAnimationStart(animation)
                view.isEnabled = false
            }

            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                view.isEnabled = true
            }
        })
    }
    private fun rotater() {
        val animator = ObjectAnimator.ofFloat(star, View.ROTATION,-360f,0f)
        animator.duration = 2000
        animator.disableViewDuringAnimation(rotateButton)
        animator.start()
    }

    private fun translater() {
        val animator = ObjectAnimator.ofFloat(star,View.TRANSLATION_X,400F)
        animator.disableViewDuringAnimation(translateButton)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.start()
    }

    private fun scaler() {
        val SCALE_X = PropertyValuesHolder.ofFloat(View.SCALE_X,4f)
        val SCALE_Y = PropertyValuesHolder.ofFloat(View.SCALE_Y,4f)
        val animator = ObjectAnimator.ofPropertyValuesHolder(star,SCALE_X,SCALE_Y)
        animator.repeatCount = 1
        animator.repeatMode = ObjectAnimator.REVERSE
        animator.disableViewDuringAnimation(scaleButton)
        animator.start()
    }

    private fun fader() {
        ObjectAnimator.ofFloat(star, View.ALPHA, 0f).apply{
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(fadeButton)
            start()
        }
    }

    private fun colorizer() {
        ObjectAnimator.ofArgb(star.parent,"backgroundColor", Color.GREEN,Color.RED).apply {
            duration = 2000
            repeatCount = 1
            repeatMode = ObjectAnimator.REVERSE
            disableViewDuringAnimation(colorizeButton)
            start()
        }
    }

    private fun shower() {
        val container = star.parent as ViewGroup
        val containerHeight = container.height
        val containerWidth = container.width
        var starW = star.width.toFloat()
        var starH = star.height.toFloat()

        val newStar = AppCompatImageView(this).apply {
            setImageResource(R.drawable.ic_star)
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)

            scaleX = Math.random().toFloat()*1.5f +1f
            scaleY = scaleX
            starW *= scaleX
            starH *= scaleX

            translationX = Math.random().toFloat()* containerWidth - starW/2
        }

        container.addView(newStar)


        val mover = ObjectAnimator.ofFloat(newStar,View.TRANSLATION_Y, -starH, containerHeight + starH).apply{
            interpolator = AccelerateInterpolator(1f)
        }
        val rotator = ObjectAnimator.ofFloat(newStar,View.ROTATION,(Math.random()*1080).toFloat()).also{
            it.interpolator = LinearInterpolator()
        }


        AnimatorSet().apply{
            playTogether(mover,rotator)
            duration = (Math.random()*1500+1500).toLong()

            addListener(object:AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    container.removeView(newStar)
                }
            })

            start()
        }

    }

}
