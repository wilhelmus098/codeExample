package io.github.wilhelmus098.client_app.fragment

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.ast4b.externaldbandre.SendPostRequest
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import io.github.wilhelmus098.client_app.MainActivity

import io.github.wilhelmus098.client_app.R
import io.github.wilhelmus098.client_app.User
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_profile.*
import org.json.JSONObject

class ActivityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_activity, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Picasso.get().load(getString(R.string.server) + "").networkPolicy(NetworkPolicy.NO_CACHE).into(imageViewMap)
        val m = activity as MainActivity
        var phone = m.findUser()
        var imgPath = ""

        m.setTitle("PARKING INFORMATION")

        //EXECUTING POST FOR PROFILE
        val field = arrayOf("phone")
        val values = arrayOf(phone)
        val post = SendPostRequest(m, field, values)
        post.execute(getString(R.string.server) + "getcurrentactivity.php")
        //
        // Picasso.get().invalidate(getString(R.string.server) + "admin/img/9.jpg")
        try
        {
            val json = JSONObject(post.get())
            val jsonarr = json.getJSONArray("currentActivity")

            //ADD VALUES TO INTERNAL ARRAY
            for(i in 0 until jsonarr.length())
            {
                val jsonCurrentActivity = jsonarr.getJSONObject(i)

                textViewNoActivity.setText("")
                textViewParkingLot.setText(jsonCurrentActivity.getString("lot_name"))
                textViewDateIn.setText(jsonCurrentActivity.getString("record_datein"))
                textViewParkingSlot.setText("Slot number : " + jsonCurrentActivity.getString("record_slotno"))
                imgPath = jsonCurrentActivity.getString("lot_path")
                textViewDesc.setText(jsonCurrentActivity.getString("lot_desc"))

                //Picasso.get().invalidate("9.jpg")
                Picasso.get().load(getString(R.string.server) + "admin/img/" + imgPath).networkPolicy(NetworkPolicy.NO_CACHE).into(imageViewMap)
            }
        }
        catch(e: Exception)
        {
            Picasso.get().load(getString(R.string.server) + "").networkPolicy(NetworkPolicy.NO_CACHE).into(imageViewMap)
            //Toast.makeText(m,"cant parse json", Toast.LENGTH_SHORT).show()
            textViewNoActivity.setText("There is no activity")
            textViewParkingLot.setText("")
            textViewDateIn.setText("")
            textViewParkingSlot.setText("")
            textViewDesc.setText("")
            textView12.setText("")
        }
    }

}
