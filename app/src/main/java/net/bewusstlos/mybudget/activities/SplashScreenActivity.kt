package net.bewusstlos.mybudget.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.common.navigateTo
import net.bewusstlos.mybudget.services.ServicesContainer
import org.jetbrains.anko.doAsync

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        if (FirebaseAuth.getInstance().currentUser != null) {
            doAsync {
                if (ServicesContainer.budgetService.getBudget() == null)
                    runOnUiThread({ navigateTo(AddBudgetActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP) })
                else
                    runOnUiThread({ navigateTo(MainActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP) })
            }
        } else {
            navigateTo(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
    }
}
