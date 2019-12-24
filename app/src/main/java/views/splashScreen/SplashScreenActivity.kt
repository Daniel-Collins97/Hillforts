package views.splashScreen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.assignment1.hillforts.R
import views.login.LoginView
import java.lang.Exception


class SplashScreenActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                    val intent = Intent(this@SplashScreenActivity, LoginView::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        background.start()
    }
}