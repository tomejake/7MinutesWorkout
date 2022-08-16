package com.example.a7minutesworkout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null
    private var restTimer: CountDownTimer? = null
    private var restProcess = 0
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProcess = 0
    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setupRestView()
    }

    private fun setupRestView(){
        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE

        if(restTimer != null){
            restTimer?.cancel()
            restProcess = 0
        }
        setRestProgressBar()
    }

    @SuppressLint("SetTextI18n")
    private fun setupExerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProcess = 0
        }

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProcess

        restTimer = object: CountDownTimer(10000, 1000){
            override fun onTick(p0: Long) {
                restProcess++
                binding?.progressBar?.progress = 10 - restProcess
                binding?.tvTimer?.text = (10 - restProcess).toString()
            }

            override fun onFinish() {
                Toast.makeText(
                    this@ExerciseActivity,
                    "휴식 끝, 운동 시작",
                    Toast.LENGTH_SHORT
                ).show()
                currentExercisePosition++
                setupExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProcess

        exerciseTimer = object: CountDownTimer(30000, 1000){
            override fun onTick(p0: Long) {
                exerciseProcess++
                binding?.progressBarExercise?.progress = 30 - exerciseProcess
                binding?.tvTimerExercise?.text = (30 - exerciseProcess).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList!!.size - 1){
                    Toast.makeText(
                        this@ExerciseActivity,
                        "운동 끝, 휴식 시작",
                        Toast.LENGTH_SHORT
                    ).show()
                    setupRestView()
                } else {
                    Toast.makeText(
                        this@ExerciseActivity,
                        "축하합니다",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProcess = 0
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProcess = 0
        }

        binding = null
    }
}