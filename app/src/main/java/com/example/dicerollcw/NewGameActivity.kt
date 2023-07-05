package com.example.dicerollcw

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NewGameActivity: AppCompatActivity(){

    lateinit var playerValues: IntArray
    private lateinit var computerValues: IntArray
    lateinit var staticDice:IntArray
    var playerScore: Int =0
    var computerScore: Int =0
    lateinit var throwButton: Button
    lateinit var scoreButton: Button
    private var playerRollCount: Int=0
    private var cpuRoll:Int = 0
    private var target:Int=0
    private var rerollCount:Int=0
    var optimizedCounter:Int=0
    var wins:Int=0
    var loss:Int=0
    var counter:Int=0
    lateinit var playerDice: List<ImageView>
    lateinit var computerDice: List<ImageView>
    val images= mutableListOf(R.drawable.dice_1,R.drawable.dice_2,R.drawable.dice_3,R.drawable.dice_4,R.drawable.dice_5,R.drawable.dice_6)
    var clickedList = mutableListOf<Boolean>(false,false,false,false,false)

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        wins=intent.getIntExtra("wins",0)
        loss=intent.getIntExtra("loss",0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)
        val winCounter=findViewById<TextView>(R.id.winCount)
        winCounter.text="H:$wins/C:$loss"
        playerDice= listOf(findViewById(R.id.leftDice1),findViewById(R.id.leftDice2),findViewById(R.id.leftDice3),findViewById(R.id.leftDice4),findViewById(R.id.leftDice5))
        computerDice= listOf(findViewById(R.id.rightDice1),findViewById(R.id.rightDice2),findViewById(R.id.rightDice3),findViewById(R.id.rightDice4),findViewById(R.id.rightDice5))
        playerValues= IntArray(5){1}
        computerValues= IntArray(5){1}
        staticDice= IntArray(5){0}
        setClickListener()
        throwButton =findViewById(R.id.throwButton)
        target=intent.getIntExtra("target",101)
        val difficulty=intent.getBooleanExtra("difficulty",true)
        scoreButton=findViewById(R.id.score_button)
        throwButton.setOnClickListener{playerRoll(clickedList);optimizedCounter++
            if(difficulty){
                computerRoll(false)}
            else{
                if (optimizedCounter==1){
                    optimizedRoll()
                }else if (optimizedCounter==3){
                    addScore(computerValues,findViewById(R.id.cpu_score),false)
                    optimizedCounter=0
                }
            }
        }
        scoreButton.setOnClickListener{addScore(playerValues,findViewById(R.id.player_Score),true)
            if(difficulty){ computerRoll(true)}
            else{
                addScore(computerValues,findViewById(R.id.cpu_score),false)
            }}
    }
    //saves values so that it can be used after destroyed and recreated
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt("wins", wins)
        outState.putInt("loss",loss)
        outState.putInt("cpuScore",computerScore)
        outState.putInt("playerScore",playerScore)
        outState.putInt("counter",counter)
        outState.putInt("playerRoll",playerRollCount)
        outState.putInt("cpuRoll",cpuRoll)
        outState.putInt("rerollCount",rerollCount)
        outState.putIntArray("computerValues",computerValues)
        outState.putIntArray("playerValues",playerValues)
        outState.putIntArray("staticDice",staticDice)
        outState.putBooleanArray("selectedDices",clickedList.toBooleanArray())
        super.onSaveInstanceState(outState)
    }
    //restores the values back to the variables after recreated
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        wins = savedInstanceState.getInt("wins")
        loss=savedInstanceState.getInt("loss")
        counter=savedInstanceState.getInt("counter")
        playerRollCount=savedInstanceState.getInt("playerRoll")
        cpuRoll=savedInstanceState.getInt("cpuRoll")
        rerollCount=savedInstanceState.getInt("rerollCount")
        computerScore=savedInstanceState.getInt("cpuScore")
        playerScore=savedInstanceState.getInt("playerScore")
        computerValues=savedInstanceState.getIntArray("computerValues")?:computerValues
        playerValues=savedInstanceState.getIntArray("playerValues")?:playerValues
        staticDice=savedInstanceState.getIntArray("staticDice")?:staticDice
        clickedList=savedInstanceState.getBooleanArray("selectedDices")?.toMutableList()?:clickedList
        for (index in 0..4){
            computerDice[index].setImageResource(images[computerValues[index]-1])
            playerDice[index].setImageResource(images[playerValues[index]-1])
        }
        if (computerScore>target || playerScore>target){
            disableButtons()
        }
        val winCounter=findViewById<TextView>(R.id.winCount)
        winCounter.text = "H:$wins/C:$loss"
        findViewById<TextView>(R.id.cpu_score).text = "CPU:  $computerScore"
        findViewById<TextView>(R.id.player_Score).text = "PLAYER:  $playerScore"

    }
    //funtion to make the dices clickable and unclickable
    private fun clickableState(state: Boolean){
        for (index in 0..4){
            playerDice[index].isClickable=state

        }
    }
    //function to allow the user to select and deselect the dices
    private fun setClickListener(){
        for (index in 0..4){
            playerDice[index].setOnClickListener{playerDice[index].isSelected=!playerDice[index].isSelected
                if (playerRollCount>0){
                    if (playerDice[index].isSelected){
                        playerDice[index].setBackgroundColor(Color.BLACK)
                        clickedList[index]=true
                    }else{
                        playerDice[index].setBackgroundColor(Color.TRANSPARENT)
                        clickedList[index]=false
                    }
                }
            }
        }
    }
    //fuction to randomise the values and set the relevant dices
    private fun diceArranger(valueArray: IntArray, dice: ImageView, index: Int){
        valueArray[index]=(1..6).random()
        dice.setImageResource(images[valueArray[index]-1])
    }

    private fun animation(dice: ImageView){
        val animation = ObjectAnimator.ofFloat(dice, "rotationY", 0f, 360f)
        animation.duration = 500
        animation.start()
    }
    //function for the rolling of the player
    private fun playerRoll(clickedList: MutableList<Boolean>){
        playerRollCount += 1
        //allows the user to select dices after the first throw
        clickableState(true)
        //checks if any dice is selected and if there are it skips that dice
        for (index in 0..4){
            if (clickedList[index]){
                clickedList[index]=false
                playerDice[index].setBackgroundColor(Color.TRANSPARENT)
                continue
            }
            diceArranger(playerValues,playerDice[index],index)
            animation(playerDice[index])
        }
        //after three throws it adds the score by sending it to the add score function
        if (playerRollCount==3){
            addScore(playerValues,findViewById(R.id.player_Score),true)
            playerRollCount=0
        }
    }
    //rolling and rerolling function for the easy difficulty computer
    private fun computerRoll(state: Boolean) {
        //counter to check the amount of throws done for that attempt
        if (!state){
            counter+=1
        }
        //randomises the dice and randomises how many rerolls should be done for the turn
        if (playerRollCount<=1 && !state){
            rerollCount =(0..2).random()
            cpuRoll+=1
            for (index in 0..4){
                diceArranger(computerValues,computerDice[index],index)
                animation(computerDice[index])
            }
        }else if ((state && rerollCount>0)|| counter==3 && rerollCount>0){ //checks if there are any rerolls to be done and allows to proceed if there are
            for (index in 0 until rerollCount){
                //gets how many dices in wants to roll
                var randomNum=(0..4).random()
                staticDice=IntArray(randomNum){0}

                //selects which dices it wants to reroll
                while (randomNum!=0){
                    val tempValue=(0..4).random()
                    var count=0
                    if (tempValue !in staticDice){
                        staticDice[count]=tempValue
                        randomNum-=1
                        count+=1
                    }
                }
                //re rolls the dices it selected
                for (value in staticDice){
                    diceArranger(computerValues,computerDice[value],value)
                    animation(computerDice[index])
                }
            }

        }
        //calculates the score when there are three throws done by the player instead of the score button
        if ((cpuRoll>=1 && state)||counter==3){
            addScore(computerValues,findViewById(R.id.cpu_score),false)
            cpuRoll=0
            rerollCount=0
            counter=0
        }
    }


    //function to add the score and set the text of the passed parameters
    private fun addScore(valueArray: IntArray, scoreText: TextView, state: Boolean){
        //for the player
        if (playerRollCount>=1 && state){
            clickableState(false)
            for (index in valueArray) {
                playerScore += index
            }
            scoreText.text = "PLAYER:  $playerScore"
            playerRollCount=0
        } else if (cpuRoll>=1 && !state){           //for the computer
            clickableState(false)
            for (index in valueArray) {
                computerScore += index
            }
            scoreText.text = "CPU:  $computerScore"
            cpuRoll=0
            optimizedCounter=0
        }
        if (!state){
            result()
        }
    }
    private fun result(){
        if (playerScore>=target && playerScore>computerScore){
            resultPopup(true)
        }else if (computerScore>=target && computerScore>playerScore){
            resultPopup(false)
        }else if (playerScore>=target){
            while (true){
                playerRoll(clickedList)
                computerRoll(false)
                if (playerValues.sum()>computerValues.sum()){
                    resultPopup(true)
                    break
                }else if (playerValues.sum()<computerValues.sum()){
                    resultPopup(false)
                    break
                }else{
                    cpuRoll=0
                    playerRollCount=0
                    continue
                }
            }
        }
    }
    @SuppressLint("SetTextI18n")
    private fun resultPopup(state: Boolean){ //sets the result, popup and disable buttons
        if (state){
            this.wins +=1
            val winCounter=findViewById<TextView>(R.id.winCount)
            winCounter.text="H:$wins/C:$loss"
            val popupDialog=WinPopup()
            popupDialog.show(supportFragmentManager,"mypopup")
            disableButtons()
        }else{
            loss+=1
            val winCounter=findViewById<TextView>(R.id.winCount)
            winCounter.text = "H:$wins/C:$loss"
            val popupDialog=LosePopup()
            popupDialog.show(supportFragmentManager,"mypopup")
            disableButtons()
        }
    }
    fun disableButtons() {
        // Get the root layout of the activity
        throwButton.isEnabled=false
        scoreButton.isEnabled=false
    }

    override fun onBackPressed() {
        val intent=Intent(this,MainActivity::class.java)
        intent.putExtra("wins",wins)
        intent.putExtra("loss",loss)
        startActivity(intent)
        finish()
    }


    fun optimizedRoll(){
        cpuRoll+=1
        counter++
        val avg=playerScore/counter
        for (index in 0..4){
            diceArranger(computerValues,computerDice[index],index)
            animation(computerDice[index])
        }
        if(playerScore+avg>target && playerScore-computerScore>10){//add another for loop for the two rerolls
            for (rolls in 0..1){
                for (index in 0..4){
                    if (computerValues[index]<5){
                        diceArranger(computerValues,computerDice[index],index)
                        animation(computerDice[index])
                    }
                }
            }
        }
        else if((avg)>computerValues.sum()|| computerValues.sum()<16){
            for (roll in 0..1){
                for (index in 0..4){
                    if (computerValues[index]<4){
                        diceArranger(computerValues,computerDice[index],index)
                        animation(computerDice[index])
                        cpuRoll++
                    }
                }
            }
        }else{
            for (roll in 0..1){
                for (index in 0..4){
                    if (computerValues[index]<4){
                        diceArranger(computerValues,computerDice[index],index)
                        val anim = ObjectAnimator.ofFloat(computerDice[index], "rotationY", 0f, 360f)
                        anim.duration = 500
                        anim.start()
                        cpuRoll++
                    }
                }
            }
        }
    }
    /*the optimized function calculates the score per turn of the player and checks if the current
    roll is less than that, since there is a chance that the average score can be a low value so the
    function also checks if the current roll is less than half of the total possible score. If it
    matches one of those conditions it will go through and re roll the dices which are less than
    or equal to 3. since these are not close to the target the function will only risk to get values
    with a 50% probability rate. If the player score and average is greater than target and if the
    difference between cpu and player is greater than 10 , the function rerolls the dices which are
    less than or equal to 4 .It does this because there is a chance the player can win at that point
    so it risks by trying to get a higher value with a chance of 33% */
}