package je.raweeroj.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import je.raweeroj.a7minuteworkout.databinding.ActivityHistoryBinding
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private var binding : ActivityHistoryBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val historyDao = (application as HistoryApp).db.historyDao()

        setSupportActionBar(binding?.toolbarHistoryActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "HISTORY"
        }

        binding?.toolbarHistoryActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        lifecycleScope.launch{
            historyDao.fetchAllHistory().collect{
                val list = ArrayList(it)
                setupListofDataIntoRecyclerView(list,historyDao)
            }
        }

    }

    private fun setupListofDataIntoRecyclerView(historyList:ArrayList<HistoryEntity>,employeeDao: HistoryDao){
        if(historyList.isNotEmpty()){
            val historyAdapter = HistoryAdapter(historyList)
            binding?.rvItemsList?.layoutManager = LinearLayoutManager(this)
            binding?.rvItemsList?.adapter = historyAdapter
            binding?.rvItemsList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
        }else{
            binding?.rvItemsList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }
    }
}