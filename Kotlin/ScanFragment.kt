package io.github.wilhelmus098.client_app.fragment

import android.content.Intent
//import android.app.Fragment
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.Manifest
import android.annotation.TargetApi
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.widget.Button
import android.widget.TextView
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import android.widget.Toast
import com.example.ast4b.externaldbandre.SendPostRequest
import com.journeyapps.barcodescanner.CaptureActivity
import io.github.wilhelmus098.client_app.MainActivity
import org.json.JSONException
import org.json.JSONObject
import io.github.wilhelmus098.client_app.R
import kotlinx.android.synthetic.main.fragment_activity.*
import kotlinx.android.synthetic.main.fragment_scan.*
import java.time.LocalDateTime
import java.time.Period
import java.time.temporal.ChronoUnit

private lateinit var mContext:Context
private lateinit var mScannerView: ZXingScannerView
class ScanFragment : Fragment(), ZXingScannerView.ResultHandler,View.OnClickListener {
    @TargetApi(26)
    override fun handleResult(p0: Result?) {

        val m = activity as MainActivity
        var phone = m.findUser()
        var resArray = p0.toString().split(";").toTypedArray()
        var type = resArray[0]
        var buildingid = resArray[1]
        var stringdatetime = resArray[2]
        var datetime = LocalDateTime.parse(stringdatetime)
        var desc = "a"

        @TargetApi(26)
        // ENTER
        if(type == "in")
        {
            val current = LocalDateTime.now()
            var perioddate = Period.between(current.toLocalDate(), datetime.toLocalDate())

            var y = perioddate.get(ChronoUnit.YEARS)
            var month = perioddate.get(ChronoUnit.MONTHS)
            var d = perioddate.get(ChronoUnit.DAYS)

            var h = ChronoUnit.HOURS.between(datetime.toLocalTime(),current.toLocalTime())
            var min = ChronoUnit.MINUTES.between(datetime.toLocalTime(),current.toLocalTime())
            var s = ChronoUnit.SECONDS.between(datetime.toLocalTime(),current.toLocalTime())

            if(y.toString() == "0" && month.toString() == "0" && d.toString() == "0" && h.toString() == "0" && min.toString() == "0" && s.toInt() < 7)
            {
                Toast.makeText(context,"y",Toast.LENGTH_SHORT).show()
                val field = arrayOf("phone","desc","buildingid")
                val values = arrayOf(phone, desc, buildingid)
                val post = SendPostRequest(m, field, values)
                post.execute(getString(R.string.server) + "start.php")

                var msg = post.get()
                var arr = msg.split(";").toTypedArray()
                if(arr[0].toString() == "error")
                {
                    Toast.makeText(context,arr[1].toString(),Toast.LENGTH_SHORT).show()
                    Toast.makeText(context,y.toString()+m.toString()+d.toString()+h.toString()+min.toString()+s.toString(),Toast.LENGTH_SHORT).show()
                    m.setFragment(m.home)
                }
                else
                {
                    m.setFragment(m.current)
                }
            }
            else
            {
                Toast.makeText(context,"Expired QR Code",Toast.LENGTH_SHORT).show()
                m.setFragment(m.home)
            }
        }
        // OUT
        else if(type == "out")
        {
            val current = LocalDateTime.now()
            var perioddate = Period.between(current.toLocalDate(), datetime.toLocalDate())

            var y = perioddate.get(ChronoUnit.YEARS)
            var month = perioddate.get(ChronoUnit.MONTHS)
            var d = perioddate.get(ChronoUnit.DAYS)

            var h = ChronoUnit.HOURS.between(datetime.toLocalTime(),current.toLocalTime())
            var min = ChronoUnit.MINUTES.between(datetime.toLocalTime(),current.toLocalTime())
            var s = ChronoUnit.SECONDS.between(datetime.toLocalTime(),current.toLocalTime())

            if(y.toString() == "0" && month.toString() == "0" && d.toString() == "0" && h.toString() == "0" && min.toString() == "0" && s.toInt() < 7)
            {
                //GET RECORDID
                val field = arrayOf("phone","buildingid")
                val values = arrayOf(phone,buildingid)
                val post = SendPostRequest(m, field, values)
                var recordid = ""
                var recordslotno = ""

                post.execute(getString(R.string.server) + "getrecordid.php")
                var msg = post.get()

                //IF RECORD EXIST
                if (msg != "")
                {
                    var record = post.get().split(";").toTypedArray()
                    recordid = record[0]
                    recordslotno = record[1]

                    val field2 = arrayOf("buildingid","recordid")
                    val values2 = arrayOf(buildingid,recordid)
                    val post2 = SendPostRequest(m,field2,values2)
                    post2.execute(getString(R.string.server) + "getestimation.php")
                    var estCost = post2.get()
                    //Toast.makeText(context, post2.get(),Toast.LENGTH_SHORT).show()

                    val field1 = arrayOf("selectedslot","buildingid","recordid","phone")
                    val values1 = arrayOf(recordslotno,buildingid,recordid,phone)
                    val post1 = SendPostRequest(m!!,field1,values1)

                    var ok = 0

                    val builder = AlertDialog.Builder(m)
                    builder.setTitle("Payment Confirmation")
                    builder.setMessage("Cost estimation : Rp." + estCost)

                    builder.setPositiveButton("Pay") {dialog, which ->
                        post1.execute("http://192.168.0.103:8888/ta/end.php")
                        var msg1 = post1.get()
                        Toast.makeText(m.applicationContext, msg1, Toast.LENGTH_SHORT).show()
                    }

                    builder.setNegativeButton("Cancel") {dialog, which -> }
                    builder.show()
                }
                else
                {
                    Toast.makeText(context,"Scan this to get out. Make sure you have parked.",Toast.LENGTH_SHORT).show()
                }
                m.setFragment(m.home)
            }
            else
            {
                Toast.makeText(context,"Expired QR Code",Toast.LENGTH_SHORT).show()
                m.setFragment(m.home)
            }
        }
        else
        {
            Toast.makeText(context,"Unknown QRCode",Toast.LENGTH_SHORT).show()
            m.setFragment(m.home)
        }

    }

    private fun initScannerView()
    {
        mScannerView = ZXingScannerView(context)
        mScannerView.setAutoFocus(true)
        mScannerView.setResultHandler(this)
        layoutCam.addView(mScannerView)
    }

    override fun onAttach(context:Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onDetach() {
        super.onDetach()
        mContext = mContext
    }

    override fun onStart()
    {
        mScannerView.startCamera()
        super.onStart()

//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA == PackageManager.PERMISSION_DENIED))
//        {
//            Toast.makeText(context,"permission denied", Toast.LENGTH_SHORT)
//        }
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    override fun onClick(v: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    //
    var isCaptured = false
    internal var txtName: TextView? = null
    internal var txtSiteName: TextView? = null
    internal var btnScan: Button? = null
    internal var qrIntegrator: IntentIntegrator? = null
    internal var home: Fragment? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_scan, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val m = activity as MainActivity
//        if ((ContextCompat.checkSelfPermission(m, Manifest.permission.CAMERA) === PackageManager.PERMISSION_DENIED))
//        {
//            Toast.makeText(context,"permission denied", Toast.LENGTH_SHORT)
//        }
//        else
//        {
            ActivityCompat.requestPermissions(m, arrayOf<String>(Manifest.permission.CAMERA), 101)
//        }
        m.setTitle("SCAN")
        initScannerView()
        super.onViewCreated(view,savedInstanceState)

    }

}

