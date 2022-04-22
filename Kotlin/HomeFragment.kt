package io.github.wilhelmus098.client_app.fragment

import android.content.Intent
//import android.app.Fragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.google.zxing.Result
//import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import android.widget.Toast
import com.example.ast4b.externaldbandre.SendPostRequest
import com.journeyapps.barcodescanner.CaptureActivity
import io.github.wilhelmus098.client_app.*
import org.json.JSONException
import org.json.JSONObject
import kotlinx.android.synthetic.main.fragment_home.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class HomeFragment : Fragment() {

    var isCaptured = false
    internal var txtName: TextView? = null
    internal var txtSiteName: TextView? = null
    internal var btnScan: Button? = null
    internal var qrIntegrator: IntentIntegrator? = null
    internal var home: Fragment? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_home, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //txtName = view.findViewById(R.id.name)
        //txtSiteName = view.findViewById(R.id.site_name)
        //btnScan = view.findViewById(R.id.btnScan)
        qrIntegrator = IntentIntegrator.forSupportFragment(this)
        qrIntegrator?.setOrientationLocked(true)
        //btnScan!!.setOnClickListener { performAction() }

        //GETTING LIST OF PARKING LOT
        val m = activity as MainActivity
        var phone = m.findUser()
        //CLEARING ARRAYS
        m.parkingLots.clear()
        m.parkingLots1.clear()
        m.users.clear()
        m.setTitle("HOME")

        try
        {
            //EXECUTING POST TO GET BALANCE
            val field = arrayOf("phone")
            val values = arrayOf(phone)
            val post = SendPostRequest(m, field, values)
            post.execute(getString(R.string.server) + "getprofile.php")

            val json = JSONObject(post.get())
            val jsonarr = json.getJSONArray("profile")

            //ADD VALUES TO INTERNAL ARRAY
            for(i in 0 until jsonarr.length())
            {
                val jsonProfile = jsonarr.getJSONObject(i)
                m.users.add(User(
                        jsonProfile.getString("user_phone"),
                        jsonProfile.getString("user_name"),
                        jsonProfile.getString("user_email"),
                        jsonProfile.getInt("user_balance")
                ))
            }

            txtWelcome.setText("Welcome, " + m.users[0].name)
        }
        catch(e: Exception)
        {
            Toast.makeText(m,"cant parse json",Toast.LENGTH_SHORT).show()
        }



        //GET AND PARSE JSON FOR PARKING LOT
        try
        {
            //EXECUTING POST FOR PARKING LOT
            val field = arrayOf("")
            val values = arrayOf("")
            val post = SendPostRequest(m, field, values)
            post.execute(getString(R.string.server) + "getparkinglot.php")

            val json = JSONObject(post.get())
            val jsonarr = json.getJSONArray("parkinglot")

            //ADD VALUES TO INTERNAL ARRAY
            for(i in 0 until jsonarr.length())
            {
                val jsonParkingLot = jsonarr.getJSONObject(i)
                m.parkingLots.add(ParkingLot(
                        jsonParkingLot.getInt("lot_id"),
                        jsonParkingLot.getString("lot_name")
                ))
            }

        }
        catch(e: Exception)
        {
            Toast.makeText(m,"cant parse json for parking lot",Toast.LENGTH_SHORT).show()
        }

        //CONVERTING CLASS ARRAY TO STRING ARRAY
        try
        {
            for(i in 0 until m.parkingLots.size)
            {
                m.parkingLots1.add(m.parkingLots[i].name)
            }
        }
        catch (e: Exception)
        {
            Toast.makeText(m, "cant convert class to string",Toast.LENGTH_SHORT).show()
        }
        //SETTING SPINNER
        val aa = ArrayAdapter(context, android.R.layout.simple_spinner_item, m.parkingLots1)
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerParkingLot.adapter = aa


        //SHOW AVAILABLE SLOT WHEN BUTTON CLICKED
        btnGetSlot.setOnClickListener{

            //GET PARKINGLOT ID
            var selectedParkingLot = spinnerParkingLot.selectedItemPosition
            var parkingLotId = m.parkingLots[selectedParkingLot].parkinglotId.toString()

            //GET FREE SLOT ON SELECTED PARKING LOT
            val field = arrayOf("lot_id")
            val values = arrayOf(parkingLotId)
            val post = SendPostRequest(m, field, values)
            post.execute(getString(R.string.server) + "getslot.php")
            var freeSlot = post.get()

            textViewSlot.text = freeSlot
        }

        //SHOW AVAILABLE SLOT
        //val m = activity as MainActivity
//        val field = arrayOf("")
//        val values = arrayOf("")
//        val post = SendPostRequest(m, field, values)
//        post.execute(getString(R.string.server) + "getslot.php")
//        var freeSlot = post.get()

//        textViewSlot.text = freeSlot

        super.onViewCreated(view,savedInstanceState)
    }

    private fun performAction(){
        qrIntegrator?.initiateScan()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            // If QRCode has no data.
            if (result.contents == null) {
                Toast.makeText(activity, "res not found", Toast.LENGTH_LONG).show()
            } else {
                // If QRCode contains data.
                try {
                    // Converting the data to json format
                    val obj = JSONObject(result.contents)

                    // Show values in UI.
                    txtName?.text = obj.getString("name")
                    txtSiteName?.text = obj.getString("site_name")

                } catch (e: JSONException) {
                    e.printStackTrace()

                    // Data not in the expected format. So, whole object as toast message.
                    //Toast.makeText(activity, result.contents, Toast.LENGTH_LONG).show()
                }

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}

