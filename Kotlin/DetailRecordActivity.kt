package io.github.wilhelmus098.client_app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ast4b.externaldbandre.SendPostRequest
import kotlinx.android.synthetic.main.activity_detail_record.*
import org.json.JSONObject

class DetailRecordActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_record)

        var m = MainActivity()
        m.parkingLots.clear()
        setTitle("PARKING DETAIL")
        //GET RECORDID FROM HISTORY FRAGMENT
        val intents = this.intent
        var recordid = intents.getIntExtra("recordid", 0)

        //EXECUTE POST
        val field = arrayOf("recordid")
        val values = arrayOf(recordid.toString())
        val post = SendPostRequest(this, field, values)
        post.execute(getString(R.string.server) + "getrecorddetails.php")

        //GETTING POST
        //['record_id'] ; ['record_user'] ; ['record_slotno'] ; ['record_lotid'] ; ['record_datein'] ; ['record_dateout'] ; ['record_cost']
        var recordDetail = post.get().split(";").toTypedArray()

        var user = recordDetail[1]
        var slotno = recordDetail[2]
        var lotid = recordDetail[3]
        var datein = recordDetail[4]
        var dateout = recordDetail[5]
        var cost = recordDetail[6]
        var parkingLotName = ""

        var i = m.parkingLots.size


        //EXECUTING POST FOR PARKING LOT
        val field1 = arrayOf("")
        val values1 = arrayOf("")
        val post1 = SendPostRequest(m, field1, values1)
        post1.execute(getString(R.string.server) + "getbuildings.php")

        //GET AND PARSE JSON FOR PARKING LOT
        try
        {
            val json = JSONObject(post1.get())
            val jsonarr = json.getJSONArray("parkinglots")
            //ADD VALUE TO INTERNAL ARRAY
            for(i in 0 until jsonarr.length())
            {
                val jsonLot = jsonarr.getJSONObject(i)
                m.parkingLots.add(ParkingLot(
                        jsonLot.getInt("building_id"),
                        jsonLot.getString("building_name")
                ))
            }
        }
        catch (e:Exception)
        {
            Toast.makeText(m, "cant parse json parking lot", Toast.LENGTH_SHORT).show()
        }

        //GETTING PARKING LOT NAME
        for (j in 0 until m.parkingLots.size)
        {
            if (lotid.toInt() == m.parkingLots[j].parkinglotId)
            {
                parkingLotName = m.parkingLots[j].name
            }
        }

        textViewGedungParkir.text = parkingLotName
        textViewSlotParkir.text = slotno
        textViewTotalBiaya.text = "Rp."+cost
        textViewWaktuKeluar.text = dateout
        textViewWaktuMasuk.text = datein

    }
}
