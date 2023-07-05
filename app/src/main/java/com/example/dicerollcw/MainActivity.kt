package com.example.dicerollcw

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.*

class MainActivity : AppCompatActivity() {

    var wins=0
    var loss=0
    var difficulty=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val aboutButton:Button = findViewById(R.id.about_button)
        val newGameButton: Button = findViewById(R.id.roll_button)
        //gets the win and loss data from the intent
        wins=intent.getIntExtra("wins",0)
        loss=intent.getIntExtra("loss",0)
        aboutButton.setOnClickListener{val popupDialog=AboutPopup()             //sets the popup when about is clicked
        popupDialog.show(supportFragmentManager,"mypopup")}
        val gameScreen = Intent(this, NewGameActivity::class.java)
        //sends the wins and loss data taken from game screen to the game screen itself
        gameScreen.putExtra("wins",wins)
        gameScreen.putExtra("loss",loss)
        //sets the seek bar for the difficulty level
        val difficultyBar=findViewById<SeekBar>(R.id.seekBar)
        difficultyBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, state: Boolean) {
                difficulty = progress==0
                gameScreen.putExtra("difficulty",difficulty)
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
            }
            override fun onStopTrackingTouch(p0: SeekBar?){
            }
        })
        newGameButton.setOnClickListener{gameScreen.putExtra("target",getTarget())
            startActivity(gameScreen)}

    }
    fun getTarget(): Int? {
        val editText: EditText = findViewById(R.id.target)
        return editText.text.toString().toIntOrNull()
    }
    //saves the data
    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("wins",wins)
        outState.putInt("loss",loss)

    }
    //restores once recreated
    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        if (savedInstanceState != null) {
            wins=savedInstanceState.getInt("wins")
        }
        if (savedInstanceState != null) {
            loss=savedInstanceState.getInt("loss")
        }
    }


}