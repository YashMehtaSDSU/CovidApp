package com.example.covidapp.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.covidapp.R
import com.example.covidapp.data.model.Model
import com.example.covidapp.databinding.ActivityMainBinding
import com.example.covidapp.ui.adapter.PrecautionsAdapter
import com.example.covidapp.ui.adapter.SymptomsAdapter
import com.example.covidapp.utils.Constant.KNOW_MORE
import com.example.covidapp.utils.Constant.SWAPNIL_GITHUB
import com.example.covidapp.utils.Constant.SWAPNIL_LINKEDIN
import com.example.covidapp.utils.Constant.SWAPNIL_WEB
import com.example.covidapp.utils.goNextScreen
import com.example.covidapp.utils.loadWeb
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val symptomsList = ArrayList<Model>()
    private val precautionsList = ArrayList<Model>()

    private val symptomsAdapter: SymptomsAdapter by lazy { SymptomsAdapter() }
    private val precautionsAdapter: PrecautionsAdapter by lazy { PrecautionsAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                RecyclerView.HORIZONTAL,
                false
            )

            adapter = symptomsAdapter
        }

        binding.recyclerViewPrecautions.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity,
                RecyclerView.HORIZONTAL,
                false
            )
            adapter = precautionsAdapter
        }

        symptomsList.add(
            Model(
                R.drawable.cough,
                "Dry Cough",
                "A dry cough is one that does not produce phlegm or mucus."
            )
        )
        symptomsList.add(
            Model(
                R.drawable.fever,
                "Fever",
                "A fever is a temporary increase in your body temperature."
            )
        )
        symptomsList.add(
            Model(
                R.drawable.headache,
                "Head Ache",
                "A painful sensation in any part of the head, ranging from sharp to dull, that may occur with other symptoms."
            )
        )
        precautionsList.add(
            Model(
                R.drawable.vaccine,
                "Get Vaccinated",
                "Get vaccinated and protect yourself and others from corona"
            )
        )
        precautionsList.add(
            Model(
                R.drawable.handwash,
                "Hand Wash",
                " Wash your hands well and often. Use hand sanitizer when you’re not near soap and water."
            )
        )
        precautionsList.add(
            Model(
                R.drawable.mask,
                "Wear Mask",
                "Masks are a key measure to suppress transmission and save lives."
            )
        )

        symptomsAdapter.submitList(symptomsList)
        precautionsAdapter.submitList(precautionsList)
        initClicks()
        getGlobalData()
    }

    private fun initClicks() = binding.run {
        txtViewSymptoms.setOnClickListener {
            goNextScreen(SymptomsActivity::class.java)
        }


        txtViewPrecautions.setOnClickListener {
            goNextScreen(PrecautionActivity::class.java)
        }
    }
    override fun onBackPressed() {
        // Create the object of AlertDialog Builder class
        val builder = AlertDialog.Builder(this)

        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit ?")

        // Set Alert Title
        builder.setTitle("Alert !")

        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false)

        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes") {
            // When the user click yes button then app will close
                dialog, which -> finish()
        }

        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No") {
            // If user click no then dialog box is canceled.
                dialog, which -> dialog.cancel()
        }

        // Create the Alert dialog
        val alertDialog = builder.create()
        // Show the Alert Dialog box
        alertDialog.show()
    }



    private fun getGlobalData() = binding.run {
        val url: String = "https://corona.lmao.ninja/v2/all/"

        val stringRequest = StringRequest(
            Request.Method.GET,
            url,
            {

                val jsonObject = JSONObject(it.toString())

                // now set values in textview
                txtInfected.text = jsonObject.getString("cases")
                txtRecoverd.text = jsonObject.getString("recovered")
                txtDeceased.text = jsonObject.getString("deaths")
            },
            {
                Toast.makeText(this@MainActivity, it.toString(), Toast.LENGTH_LONG).show()
                txtInfected.text = "-"
                txtRecoverd.text = "-"
                txtDeceased.text = "-"
            }
        )
        val requestQueue = Volley.newRequestQueue(this@MainActivity)
        requestQueue.add(stringRequest)
    }
}
