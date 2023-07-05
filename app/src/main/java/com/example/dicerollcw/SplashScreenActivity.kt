package com.example.dicerollcw

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import kotlin.concurrent.schedule

class SplashScreenActivity:AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initial_page)
        val newScreen= Intent(this,MainActivity::class.java)

        Timer().schedule(3500){
            startActivity(newScreen)
            finish()
        }

    }
}