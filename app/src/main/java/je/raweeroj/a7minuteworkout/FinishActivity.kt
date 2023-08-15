package je.raweeroj.a7minuteworkout

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import je.raweeroj.a7minuteworkout.databinding.ActivityFinishBinding
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FinishActivity : AppCompatActivity() {
    private var binding: ActivityFinishBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityFinishBinding.inflate(layoutInflater)

        setContentView(binding?.root)

       val historyDao = (application as HistoryApp).db.historyDao()

        setSupportActionBar(binding?.toolbarFinishActivity)
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

//        }
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbarFinishActivity?.setNavigationOnClickListener {
            onBackPressed()
        }

        binding?.btnFinish?.setOnClickListener {
            finish()
            addRecord(historyDao = historyDao)
        }

    }

    private fun addRecord(historyDao: HistoryDao){
        val currentDateTime =
            LocalDateTime.now()

        // Create a formatter to convert the date and time to a string
        val dateFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd")


        val timeFormatter =
            DateTimeFormatter.ofPattern("HH:mm:ss")

        // Convert the date and time to a string using the formatter
        val formattedDate = currentDateTime.format(dateFormatter)
        val formattedTime = currentDateTime.format(timeFormatter)

        val date = formattedDate.toString()
        val time = formattedTime.toString()

        if(date.isNotEmpty() && time.isNotEmpty()){
            lifecycleScope.launch{
                historyDao.insert(HistoryEntity(date=date, timeFinish = time))
                //Toast.makeText(applicationContext,"Record saved", Toast.LENGTH_LONG).show()
//                binding?.etName?.text?.clear()
//                binding?.etEmailId?.text?.clear()
            }

        }else{
            Toast.makeText(applicationContext,"Time Error", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        binding = null // need to do incase you use binding to avoid problem / memory leak
    }
}