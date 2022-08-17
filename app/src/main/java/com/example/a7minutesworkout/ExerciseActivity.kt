package com.example.a7minutesworkout

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a7minutesworkout.databinding.ActivityExerciseBinding
import com.example.a7minutesworkout.databinding.DialogCustomBackConfirmationBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null
    private var restTimerDuration: Long = 1
    private var restTimer: CountDownTimer? = null
    private var restProcess = 0
    private var exerciseTimerDuration: Long = 1
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProcess = 0
    private var exerciseList:ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1
    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null
    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        binding?.toolbarExercise?.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        setupRestView()
        setupExerciseStatusRecycleView()
    }

    override fun onBackPressed() {
        customDialogForBackButton()
    }

    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)

        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)

        dialogBinding.btnYes.setOnClickListener {
            this@ExerciseActivity.finish()
            customDialog.dismiss()
        }

        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
        }

        customDialog.show()
    }

    private fun setupExerciseStatusRecycleView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupRestView(){
        try {
            val soundURI =
                Uri.parse("android.resource://com.example.a7minutesworkout/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExercise?.visibility = View.VISIBLE

        if(restTimer != null){
            restTimer?.cancel()
            restProcess = 0
        }

        binding?.tvUpcomingExercise?.text = exerciseList!![currentExercisePosition + 1].getName()

        setRestProgressBar()
    }

    @SuppressLint("SetTextI18n")
    private fun setupExerciseView(){
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExercise?.visibility = View.INVISIBLE

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProcess = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar()
    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProcess

        restTimer = object: CountDownTimer(restTimerDuration * 1000, 1000){
            override fun onTick(p0: Long) {
                restProcess++
                binding?.progressBar?.progress = 10 - restProcess
                binding?.tvTimer?.text = (10 - restProcess).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                Toast.makeText(
                    this@ExerciseActivity,
                    "휴식 끝, 운동 시작",
                    Toast.LENGTH_SHORT
                ).show()
                currentExercisePosition++
                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()
                setupExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar(){
        binding?.progressBarExercise?.progress = exerciseProcess

        exerciseTimer = object: CountDownTimer(exerciseTimerDuration * 3000, 1000){
            override fun onTick(p0: Long) {
                exerciseProcess++
                binding?.progressBarExercise?.progress = 30 - exerciseProcess
                binding?.tvTimerExercise?.text = (30 - exerciseProcess).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                if(currentExercisePosition < exerciseList!!.size - 1){
                    exerciseList!![currentExercisePosition].setIsSelected(false)
                    exerciseList!![currentExercisePosition].setIsCompleted(true)
                    exerciseAdapter!!.notifyDataSetChanged()
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
                    val intent = Intent(this@ExerciseActivity, FinishActivity::class.java)
                    finish()
                    startActivity(intent)
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

        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }

        if(player != null){
            player?.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)

            // TextToSpeech.LANG_MISSING_DATA: 언어 데이터 존재하지 않음
            // TextToSpeech.LANG_NOT_SUPPORTED: 지원하지 않는 언어
            if(result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                // 지원 안한다는 로그 생성
                Log.e("TTS", "The Language specified is not supported!")
            } else {
                Log.e("TTS", "Initialization Failed!")
            }
        }
    }

    private fun speakOut(text: String){
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}