package io.github.wilhelmus098.client_app
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.ContactsContract
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.FrameLayout
import android.widget.Toast
import io.github.wilhelmus098.client_app.fragment.*
import kotlinx.android.synthetic.main.activity_main.*

var records: ArrayList<Record> = ArrayList<Record>()
var records1: ArrayList<String> = ArrayList<String>()
var parkingLot: ArrayList<ParkingLot> = ArrayList<ParkingLot>()
var parkingLot1: ArrayList<String> = ArrayList<String>()

class MainActivity : AppCompatActivity() {

    private var mainNav: BottomNavigationView? = null
    private var frame: FrameLayout? = null
    public var user = ""

    //HISTORY FRAGMENT PROPERTY
    var recordId = ArrayList<String>()
    var recordUser = ArrayList<String>()
    var recordSlotno = ArrayList<String>()
    var recordBuildingid = ArrayList<String>()
    var recordDatein = ArrayList<String>()
    var recordDateout = ArrayList<String>()
    var recordDesc = ArrayList<String>()
    var recordCost = ArrayList<String>()
    var records = ArrayList<Record>()
    var parkingLots = ArrayList<ParkingLot>()
    var parkingLots1 = ArrayList<String>()
    var historyarray = ArrayList<String>()
    var users = ArrayList<User>()

    val home = HomeFragment()
    val history = HistoryFragment()
    val profile = ProfileFragment()
    val scan = ScanFragment()
    val current = ActivityFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        var sp = SharedPreference()
        user = sp.getUser(this)
        var usernameLength = sp.getUser(this).length

        if(usernameLength == 0)
        {
            var i = Intent(this, LoginActivity::class.java)
            startActivity(i)
        }

//        val intents = this.intent
//        var log = intents.getIntExtra("logged", 0)
//        try
//        {
//            user = intents.getStringExtra("user")
//        }
//        catch (e : Throwable)
//        {
//            println(e.message)
//        }
//
//        if(log == 0)
//        {
//            var i = Intent(this, LoginActivity::class.java)
//            startActivity(i)
//        }

        setContentView(R.layout.activity_main)

        mainNav = findViewById(R.id.bottom_nav)
        frame = findViewById(R.id.fragment_container)


        setFragment(home)

        findUser()


        mainNav?.setOnNavigationItemSelectedListener {item ->
            when(item.itemId){
                R.id.navigation_home -> {
                    setFragment(home)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_scan -> {
                    setFragment(scan)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_history -> {
                    setFragment(history)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    setFragment(profile)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_activity -> {
                    setFragment(current)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false

        }

        }

    public fun setFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,fragment,fragment.javaClass.getSimpleName())
                .commit()
    }

    fun findUser(): String {
        //Toast.makeText(this, user, Toast.LENGTH_LONG).show()
        return user

    }
}


//    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//        when (item.itemId) {
//            R.id.navigation_home -> {
//                message.setText(R.string.title_home)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_dashboard -> {
//                message.setText(R.string.title_dashboard)
//                return@OnNavigationItemSelectedListener true
//            }
//            R.id.navigation_notifications -> {
//                message.setText(R.string.title_notifications)
//                return@OnNavigationItemSelectedListener true
//            }
//        }
//        false
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val home = HomeFragment()
//        setFragment(home)
//
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
//    }
//}
