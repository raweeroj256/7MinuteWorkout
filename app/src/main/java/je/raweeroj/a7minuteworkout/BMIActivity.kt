package je.raweeroj.a7minuteworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import je.raweeroj.a7minuteworkout.databinding.ActivityBmiBinding
import java.io.Console
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object{
        private const val Metric_UNITS_VIEW = "METRIC_UNIT_VIEW"
        private const val US_UNITS_VIEW = "US_UNIT_VIEW"
    }

    private var binding : ActivityBmiBinding? = null
    private var currentisibleView : String =
        Metric_UNITS_VIEW

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if(supportActionBar != null){
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }

        binding?.toolbarBmiActivity?.setNavigationOnClickListener{
            onBackPressed()
        }

        makeVisibleMetricUnitsView()

        binding?.rgUnits?.setOnCheckedChangeListener{
            _,checkedId: Int->
            if(checkedId == R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener {
            calculateUnits()

        }
    }

    private fun makeVisibleMetricUnitsView(){
        currentisibleView = Metric_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.GONE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.GONE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.GONE

        binding?.etMetricUnitWeight?.text!!.clear()
        binding?.etMetricUnitHeight?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE

    }

    private fun makeVisibleUSUnitsView(){
        currentisibleView = US_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear()
        binding?.etUsMetricUnitHeightFeet?.text!!.clear()
        binding?.etUsMetricUnitHeightInch?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun displayBMIResult(bmi:Float){

        val bmiLabel : String
        val bmiDescription : String

        if(bmi.compareTo(15f) <= 0){
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if(bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0){
            bmiLabel = "Very underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if(bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0){
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Eat more!"
        }else if(bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0){
            bmiLabel = "Normal"
            bmiDescription = "Congratulation! You are in good shape!"
        }else if(bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0){
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout Abit!"
        }else if(bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0){
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take better care of yourself! Workout More!"
        }else if(bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0){
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "Oops! You really need to take better care of yourself! Act now!!"
        }else{
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "Oops! You really need to take better care of yourself! Act now!!!"
        }

        val bmiValue = BigDecimal(bmi.toDouble())
            .setScale(2,RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription

    }

    private fun validateMetricUnits():Boolean{
        var isValid = true

        if(binding?.etMetricUnitWeight?.text.toString().isEmpty()){
            isValid = false
        }else if(binding?.etMetricUnitHeight?.text.toString().isEmpty()){
            isValid = false
        }
        return isValid
    }

    private fun calculateUnits(){
        if(currentisibleView == Metric_UNITS_VIEW){
            if(validateMetricUnits()){
                val heightValue : Float = binding?.etMetricUnitHeight?.text.toString().toFloat()/100
                val weightValue : Float = binding?.etMetricUnitWeight?.text.toString().toFloat()


                val bmi = weightValue / (heightValue*heightValue)
                displayBMIResult(bmi)

            }else{
                Toast.makeText(this@BMIActivity,"Please Enter valid values.",Toast.LENGTH_SHORT).show()
            }
        }else{
            if(validateUSUnits()){
                val usheightValueFeet : String = binding?.etUsMetricUnitHeightFeet?.text.toString()
                val usheightValueInch : String = binding?.etUsMetricUnitHeightInch?.text.toString()
                val usweightValue : Float = binding?.etUsMetricUnitWeight?.text.toString().toFloat()

                val heightValue =
                    usheightValueInch.toFloat() + usheightValueFeet.toFloat() * 12

                val bmi = 703 * (usweightValue / (heightValue*heightValue))

                displayBMIResult(bmi)

            }else{
                Toast.makeText(this@BMIActivity,"Please Enter valid values.",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateUSUnits():Boolean{
        var isValid = true

        when{
            binding?.etUsMetricUnitWeight?.text.toString().isEmpty() ->{
                isValid = false
            }
            binding?.etUsMetricUnitHeightFeet?.text.toString().isEmpty() ->{
                isValid = false
            }
            binding?.etUsMetricUnitHeightInch?.text.toString().isEmpty() ->{
                isValid = false
            }
        }

        return isValid
    }
}