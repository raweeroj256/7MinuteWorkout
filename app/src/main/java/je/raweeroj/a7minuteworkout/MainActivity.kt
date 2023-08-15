package je.raweeroj.a7minuteworkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.FrameLayout
import android.widget.Toast
import je.raweeroj.a7minuteworkout.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var binding:ActivityMainBinding? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        val flStartButton : FrameLayout = findViewById(R.id.flstart)
        //        flStartButton.setOnClickListener{
//            Toast.makeText(
//                this@MainActivity,
//                "Here we will start the exercise",
//                Toast.LENGTH_SHORT
//            ).show()

        binding?.flstart?.setOnClickListener{
       val intent = Intent(this,ExerciseActivity::class.java)
            startActivity(intent)
        }

        binding?.flBMI?.setOnClickListener{
            val intent = Intent(this,BMIActivity::class.java)
            startActivity(intent)
        }

        binding?.flHistory?.setOnClickListener{
            val intent = Intent(this,HistoryActivity::class.java)
            startActivity(intent)
        }

    }



            override fun onDestroy() {
        super.onDestroy()

        binding = null // need to do incase you use binding to avoid problem / memory leak
    }
}