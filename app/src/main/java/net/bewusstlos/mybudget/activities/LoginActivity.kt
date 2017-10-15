package net.bewusstlos.mybudget.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.SignInButton
import com.google.firebase.auth.FirebaseAuth
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.common.isEmailAddress
import net.bewusstlos.mybudget.common.navigateTo
import net.bewusstlos.mybudget.services.ServicesContainer
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity() {

    private var tilEmail: TextInputLayout? = null
    private var tilPassword: TextInputLayout? = null
    private var bLogin: Button? = null
    private var bSignUp: Button? = null
    private var bGoogleLogin: SignInButton? = null
    //TODO: Add auth through facebook
    private var bFbLogin: Button? = null

    companion object {
        val RC_GOOGLE_SIGN_IN = 123
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null) {
            prepareToLogin()
        }
    }

    fun prepareToLogin() {
        val alert = indeterminateProgressDialog("Signing in...")
        alert.show()
        doAsync {
            if (ServicesContainer.budgetService.getBudget() == null)
                runOnUiThread({ navigateTo(AddBudgetActivity::class.java) })
            else
                runOnUiThread({ navigateTo(MainActivity::class.java) })
            alert.hide()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tilEmail = find(R.id.til_email)
        tilPassword = find(R.id.til_password)
        bLogin = find(R.id.b_login)
        bGoogleLogin = find(R.id.b_google_login)

        bLogin?.onClick {

            if (!isFieldsValid())
                return@onClick

            try {
                val pd = indeterminateProgressDialog("Signing in...")
                pd.show()
                doAsync {
                    ServicesContainer.userInteractionService.signIn(this@LoginActivity, tilEmail?.editText?.text.toString(), tilPassword?.editText?.text.toString())
                    runOnUiThread({ pd.hide() })
                }

                prepareToLogin()
            } catch (e: Exception) {
                this@LoginActivity.toast(e.message.toString())
            }
        }

        bSignUp?.onClick {

            if (!isFieldsValid())
                return@onClick

            try {
                ServicesContainer.userInteractionService.signUp(this@LoginActivity, tilEmail?.editText?.text.toString(), tilPassword?.editText?.text.toString())
                prepareToLogin()
            } catch (e: Exception) {
                this@LoginActivity.toast(e.message.toString())
            }
        }

        bGoogleLogin?.onClick {
            val signInIntent: Intent = Auth.GoogleSignInApi.getSignInIntent(ServicesContainer.userInteractionService.provideGoogleApiClient(this@LoginActivity))
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }
    }

    private fun isFieldsValid(): Boolean {
        var res = true
        if (tilEmail?.editText?.text.isNullOrEmpty()) {
            tilEmail?.error = resources.getString(R.string.field_email_empty)
            tilEmail?.isErrorEnabled = true
            res = false
        } else if (!tilEmail?.editText?.text.toString().isEmailAddress()) {
            tilEmail?.error = resources.getString(R.string.field_email_invalid)
            tilEmail?.isErrorEnabled = true
            res = false
        }

        if (tilPassword?.editText?.text.isNullOrEmpty()) {
            tilPassword?.error = resources.getString(R.string.field_password_empty)
            tilPassword?.isErrorEnabled = true
            res = false
        }
        return res
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                val account = result.signInAccount
                if (account != null) {
                    ServicesContainer.userInteractionService.authWithGoogle(this@LoginActivity, account)
                    prepareToLogin()
                }
            } else {
                this@LoginActivity.toast("Sign in with google failed")
            }
        }
    }
}
