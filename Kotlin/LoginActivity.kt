package io.github.wilhelmus098.client_app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.ast4b.externaldbandre.SendPostRequest
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setTitle("SIGN IN")

        btnLogin.setOnClickListener{
            val sp = SharedPreference()


            var phone = txtPhone.text.toString()
            var pass = txtPass.text.toString()

            val field = arrayOf("phone", "pass")
            val values = arrayOf(phone, pass)

            val post = SendPostRequest(this, field, values)
            post.execute(getString(R.string.server) + "login.php")

            var res = post.get()
            if (phone == "" || pass == "")
            {
                Toast.makeText(this, "Phone or password can't be empty", Toast.LENGTH_SHORT).show()
            }
            else
            {
                if(res == "ok")
                {
                    sp.setUser(this, phone)
                    var i = Intent(this, MainActivity::class.java)
                    i.putExtra("logged", 1)
                    i.putExtra("user", phone)
                    startActivity(i)
                    //Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
                }
            }

        }

        btnReg.setOnClickListener{
            var i = Intent(this, RegisterActivity::class.java)
            startActivity(i)
        }
    }
}
