package io.github.wilhelmus098.client_app.fragment


import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.RotateAnimation
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import com.example.ast4b.externaldbandre.SendPostRequest
import io.github.wilhelmus098.client_app.*

import kotlinx.android.synthetic.main.fragment_history.*
import org.json.JSONObject


class HistoryFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_history, container, false)

        val m = activity as MainActivity
        var phone = m.findUser()

        //CLEARING ARRAYS
        m.setTitle("HISTORY")
        m.historyarray.clear()
        m.records.clear()
        m.parkingLots.clear()

        //EXECUTING POST FOR RECORDS
        val field = arrayOf("phone")
        val values = arrayOf(phone)
        val post = SendPostRequest(m, field, values)
        post.execute(getString(R.string.server) + "getrecords.php")

        //GET AND PARSE JSON FOR RECORDS
        try
        {
            val json = JSONObject(post.get())
            val jsonarr = json.getJSONArray("records")

            //ADD VALUES TO INTERNAL ARRAY
            for(i in 0 until jsonarr.length())
            {
                val jsonRecords = jsonarr.getJSONObject(i)
                m.records.add(Record(
                        jsonRecords.getInt("record_id"),
                        jsonRecords.getString("record_user"),
                        jsonRecords.getInt("record_slotno"),
                        jsonRecords.getInt("record_buildingid"),
                        jsonRecords.getString("record_datein"),
                        jsonRecords.getString("record_dateout"),
                        jsonRecords.getInt("record_cost")
                ))
            }

        }
        catch(e: Exception)
        {
            Toast.makeText(m,"There is no completed activity",Toast.LENGTH_SHORT).show()
        }

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
            //Toast.makeText(m, "cant parse json parking lot",Toast.LENGTH_SHORT).show()
        }

        //ADD VALUES TO BE SHOWN ON HISTORY
        for (i in 0 until m.records.size)
        {
            for (j in 0 until m.parkingLots.size)
            {
                if (m.records[i].parkinglotid == m.parkingLots[j].parkinglotId)
                {
                    m.historyarray.add(m.parkingLots[j].name+" - "+m.records[i].datein)
                }
            }
        }

        val adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, m.historyarray)
        var listViewHistory = view.findViewById<ListView>(R.id.listViewHistory)
        listViewHistory.adapter = adapter

        //ONCLICKITEM LISTENER
        listViewHistory.setOnItemClickListener { parent, view, position, id ->
            //Toast.makeText(m, "record_id: "+m.records[position].recordid,Toast.LENGTH_SHORT).show()
            var i = Intent(m, DetailRecordActivity::class.java)
            i.putExtra("recordid", m.records[position].recordid)
            startActivity(i)
        }

        return view
    }


}
