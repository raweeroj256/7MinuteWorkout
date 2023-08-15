package je.raweeroj.a7minuteworkout

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
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import je.raweeroj.a7minuteworkout.databinding.ActivityExerciseBinding
import je.raweeroj.a7minuteworkout.databinding.ActivityMainBinding
import je.raweeroj.a7minuteworkout.databinding.DialogCustomBackConfirmationBinding
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer : CountDownTimer? = null
    private var restProgress = 0

    private var restTimerDuration : Long = 1
    private var exerciseTimer : CountDownTimer? = null
    private var exerciseProgress = 0

    private var exerciseTimerDuration : Long = 1
    private var exerciseList : ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech?= null
    private var player: MediaPlayer? = null

    private var exerciseAdapter : ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)

        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        exerciseList = Constants.defaultExerciseList()

        binding?.toolbarExercise?.setNavigationOnClickListener{
            onBackPressed()
            //customDialogForBackButton()
        }

        tts = TextToSpeech(this,this)

        setupRestView()
        setupExerciseStatusRecyclerView()
    }

    private fun setupExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setupRestView(){

        try{
            val soundURI = Uri.parse("android.resource://je.raweeroj.a7minuteworkout/"+ R.raw.press_start)
            player = MediaPlayer.create(applicationContext,soundURI)
            player?.isLooping = false
            player?.start()
        }catch (e:Exception){
            e.printStackTrace()
        }

        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.flRestView?.visibility = View.VISIBLE

        binding?.tvExcerciseName?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        binding?.flExerciseStatus?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.VISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.VISIBLE


        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        speakOut("Please rest for 10 seconds")

        binding?.tvUpcomingExerciseName?.text = exerciseList!![currentExercisePosition + 1].getName()


        setRestProgressBar()

    }

    override fun onBackPressed() {
        customDialogForBackButton()
        //super.onBackPressed()
    }

    private fun customDialogForBackButton(){
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

    private fun setupExerciseView(){


        binding?.flExerciseStatus?.visibility = View.VISIBLE
        binding?.tvExcerciseName?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE

        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.tvUpcomingLabel?.visibility = View.INVISIBLE
        binding?.tvUpcomingExerciseName?.visibility = View.INVISIBLE

        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].getImage())
        binding?.tvExcerciseName?.text = exerciseList!![currentExercisePosition].getName()


        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }



        speakOut(exerciseList!![currentExercisePosition].getName())

        setExerciseProgressBar()

    }

    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object : CountDownTimer(restTimerDuration * 10000,1000){
            override fun onTick(p0:Long){
                restProgress++
                binding?.progressBar?.progress = 10 - restProgress
                binding?.tvTimer?.text = (10-restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

//                Toast.makeText(
//                    this@ExerciseActivity,
//                    "Here now we will start the exercise.",
//                    Toast.LENGTH_SHORT
//                ).show()


                setupExerciseView()
            }
        }.start()


    }

    private fun setExerciseProgressBar(){

        binding?.progressBarExercise?.progress = exerciseProgress

        exerciseTimer = object : CountDownTimer(exerciseTimerDuration*30000,1000){
            override fun onTick(p0:Long){
                exerciseProgress++
                binding?.progressBarExercise?.progress = 30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30-exerciseProgress).toString()
            }

            override fun onFinish() {

                exerciseAdapter!!.notifyDataSetChanged()
               if(currentExercisePosition < exerciseList?.size!! -1){
                   exerciseList!![currentExercisePosition].setIsSelected(false)
                   exerciseList!![currentExercisePosition].setIsCompleted(true)
                   setupRestView()
               }
                else{

                    finish()
                   val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                   startActivity(intent)
               }
            }
        }.start()


    }

    override fun onDestroy() {
        super.onDestroy()

        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null){
            tts?.stop()
        }

        if(player !=null){
            player!!.stop()
        }

        binding = null
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.US)

            if(result == TextToSpeech.LANG_MISSING_DATA ||  result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","the Language specified is not supported!")
            }
        }else{
            Log.e("TTS","Initialization failed")
        }
    }

    private fun speakOut(text:String){
        tts!!.speak(text,TextToSpeech.QUEUE_FLUSH,null,"")
    }
}