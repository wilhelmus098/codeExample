package io.github.wilhelmus098.client_app.fragment


import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.ast4b.externaldbandre.SendPostRequest
import io.github.wilhelmus098.client_app.*

import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view:View = inflater.inflate(R.layout.fragment_profile, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnShowNewPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.design_ic_visibility, 0,0,0)
        btnShowOldPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.design_ic_visibility, 0,0,0)

        val m = activity as MainActivity
        var phone = m.findUser()
        m.setTitle("PROFILE")
        //CLEARING ARRAY
        m.users.clear()
        btnSaveProfile.isEnabled = false
        btnShowOldPass.isEnabled = false
        btnShowNewPass.isEnabled = false
        btnCancelProfile.isEnabled = false

        //EXECUTING POST FOR PROFILE
        val field = arrayOf("phone")
        val values = arrayOf(phone)
        val post = SendPostRequest(m, field, values)
        post.execute(getString(R.string.server) + "getprofile.php")

        //GET AND PARSE JSON FOR PROFILE
        try
        {
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
            txtPhoneProfile.isEnabled = false
            txtNameProfile.isEnabled = false
            txtMailProfile.isEnabled = false
            txtOldPassProfile.isEnabled = false
            txtNewPassProfile.isEnabled = false

            txtPhoneProfile.setText(m.users[0].phone)
            txtNameProfile.setText(m.users[0].name)
            txtMailProfile.setText(m.users[0].email)
            textViewBalanceProfile.setText("Saldo :  " + m.users[0].balance.toString())
        }
        catch(e: Exception)
        {
            Toast.makeText(m,"cant parse json",Toast.LENGTH_SHORT).show()
        }

        btnLogout.setOnClickListener {
            val sp = SharedPreference()
            sp.setUser(m, "")

            var i = Intent(m, LoginActivity::class.java)
            startActivity(i)
        }

        btnEditProfile.setOnClickListener{

            txtNameProfile.isEnabled = true
            txtMailProfile.isEnabled = true
            txtOldPassProfile.isEnabled = true
            txtNewPassProfile.isEnabled = true
            btnSaveProfile.isEnabled = true
            btnShowNewPass.isEnabled = true
            btnShowOldPass.isEnabled = true
            btnEditProfile.isEnabled = false
            btnCancelProfile.isEnabled = true
        }

        btnSaveProfile.setOnClickListener {


            var newPass = txtNewPassProfile.text.toString()
            var oldPass = txtOldPassProfile.text.toString()
            var email = txtMailProfile.text.toString()
            var name = txtNameProfile.text.toString()

            val field = arrayOf("phone","name","email","oldpassword","newpassword")
            val values = arrayOf(phone,name,email,oldPass,newPass)
            val post1 = SendPostRequest(m, field, values)

            if(name != "" && email != "")
            {
                if(newPass == "" && oldPass != "")
                {
                    Toast.makeText(context,"To change password, fill both current and new password",Toast.LENGTH_SHORT).show()
                }
                if(newPass != "" && oldPass == "")
                {
                    Toast.makeText(context,"To change password, fill both current and new password",Toast.LENGTH_SHORT).show()
                }
                if(newPass == "" && oldPass == "")
                {
                    //EXECUTING POST FOR PROFILE
                    post1.execute(getString(R.string.server) + "setprofile.php")
                    if (post1.get() == "ok")
                    {
                        Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                        btnSaveProfile.isEnabled = false
                        btnEditProfile.isEnabled = true
                        btnShowNewPass.isEnabled = false
                        btnShowOldPass.isEnabled = false
                        txtNameProfile.isEnabled = false
                        txtMailProfile.isEnabled = false
                        txtNewPassProfile.isEnabled = false
                        txtOldPassProfile.isEnabled = false
                        btnCancelProfile.isEnabled = false
                    }
                    else
                    {
                        Toast.makeText(context,post1.get(),Toast.LENGTH_SHORT).show()
                    }
                    //Toast.makeText(context,"jalan1",Toast.LENGTH_SHORT).show()
                }
                if(newPass != "" && oldPass != "")
                {
                    if(newPass == oldPass)
                    {
                        Toast.makeText(context,"Current password and new password can't be same",Toast.LENGTH_SHORT).show()
                    }
                    else
                    {
                        //EXECUTING POST FOR PROFILE
                        post1.execute(getString(R.string.server) + "setprofile.php")
                        //Toast.makeText(context,"jalan",Toast.LENGTH_SHORT).show()
                        if (post1.get() == "ok")
                        {
                            Toast.makeText(context, "Profile updated", Toast.LENGTH_SHORT).show()
                            btnSaveProfile.isEnabled = false
                            btnEditProfile.isEnabled = true
                            btnShowNewPass.isEnabled = false
                            btnShowOldPass.isEnabled = false
                            txtNameProfile.isEnabled = false
                            txtMailProfile.isEnabled = false
                            txtNewPassProfile.isEnabled = false
                            txtOldPassProfile.isEnabled = false
                            btnCancelProfile.isEnabled = false
                        }
                        else
                        {
                            Toast.makeText(context,post1.get(),Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            else
            {
                Toast.makeText(context,"Name or email can't be empty",Toast.LENGTH_SHORT).show()
            }
        }

        btnCancelProfile.setOnClickListener {
            btnCancelProfile.isEnabled = false
            btnEditProfile.isEnabled = true
            txtNameProfile.isEnabled = false
            txtMailProfile.isEnabled = false
            txtOldPassProfile.isEnabled = false
            txtNewPassProfile.isEnabled = false
            btnSaveProfile.isEnabled = false

            val field = arrayOf("phone")
            val values = arrayOf(phone)
            val post = SendPostRequest(m, field, values)
            post.execute(getString(R.string.server) + "getprofile.php")

            //GET AND PARSE JSON FOR PROFILE
            try
            {
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

                txtPhoneProfile.setText(m.users[0].phone)
                txtNameProfile.setText(m.users[0].name)
                txtMailProfile.setText(m.users[0].email)
                txtNewPassProfile.setText("")
                txtOldPassProfile.setText("")
                m.users.clear()
            }
            catch (e:Exception)
            {

            }}

        btnShowOldPass.setOnTouchListener(object : View.OnTouchListener{
            override  fun onTouch(v:View, event: MotionEvent) : Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> txtOldPassProfile.setInputType(InputType.TYPE_CLASS_TEXT)
                    MotionEvent.ACTION_UP -> txtOldPassProfile.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                }
                return true
            }
        })

        btnShowNewPass.setOnTouchListener(object : View.OnTouchListener{
            override  fun onTouch(v:View, event: MotionEvent) : Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> txtNewPassProfile.setInputType(InputType.TYPE_CLASS_TEXT)
                    MotionEvent.ACTION_UP -> txtNewPassProfile.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                }
                return true
            }
        })
    }
}
