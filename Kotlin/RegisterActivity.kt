package io.github.wilhelmus098.client_app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.ast4b.externaldbandre.SendPostRequest
import kotlinx.android.synthetic.main.activity_register.*
import android.text.InputType
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener



class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setTitle("SIGN UP")
        btnShowPass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.design_ic_visibility, 0,0,0)

        btnRegister.setOnClickListener{
            var name = txtName.text.toString()
            var phone = txtPhone.text.toString()
            var mail = txtMail.text.toString()
            var pass = txtPassword.text.toString()

            val field = arrayOf("name","phone","mail","pass")
            val values = arrayOf(name,phone,mail,pass)
            val post = SendPostRequest(this, field, values)
            if(name == "" || phone == "" || mail == "" || pass == "")
            {
                Toast.makeText(this, "One or more fields can't be empty", Toast.LENGTH_SHORT).show()
            }
            else
            {
                post.execute(getString(R.string.server) + "register.php")
                Log.d("aa", post.get())
                //var count = name.length
                var res = post.get()
                if(res.contains(name))
                {
                    Toast.makeText(this, res, Toast.LENGTH_SHORT).show()

                    var i = Intent(this, LoginActivity::class.java)
                    startActivity(i)
                }
                else
                {
                    Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
                }
            }

        }

        btnShowPass.setOnTouchListener(object : OnTouchListener {

            override fun onTouch(v: View, event: MotionEvent): Boolean {

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> txtPassword.setInputType(InputType.TYPE_CLASS_TEXT)
                    MotionEvent.ACTION_UP -> txtPassword.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                }
                return true
            }
        })
    }
}
