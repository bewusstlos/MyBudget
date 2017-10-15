package net.bewusstlos.mybudget.services

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import net.bewusstlos.mybudget.R

/**
 * Created by bewusstlos on 10/11/2017.
 */
class UserInteractionService {

    fun signIn(activity: Activity, email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(activity, { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                //
            } else {
                throw task.exception ?: IllegalArgumentException("Login failed")
            }

        })
    }

    fun signUp(activity: Activity, email: String, password: String) {
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(activity, { task: Task<AuthResult> ->
            if (task.isSuccessful)
            //
            else {
                throw Exception("Authentication failed")
            }
        })
    }

    fun provideGoogleApiClient(activity: AppCompatActivity): GoogleApiClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(activity.resources.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        return GoogleApiClient.Builder(activity)
                .enableAutoManage(activity, { connectionResult: ConnectionResult ->
                    if (!connectionResult.isSuccess)
                        throw Exception("Connection failed")
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    fun authWithGoogle(activity: Activity, acc: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(acc.idToken, null)
        val firebaseAuth = FirebaseAuth.getInstance()
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(activity, { task: Task<AuthResult> ->
            if (task.isSuccessful) {
                //
            } else {
                throw Exception("Sign in through Google failed")
            }
        })
    }

    inline fun logOut() {
        FirebaseAuth.getInstance().signOut()
    }
}