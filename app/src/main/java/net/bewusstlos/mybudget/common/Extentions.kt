package net.bewusstlos.mybudget.common

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import net.bewusstlos.mybudget.R
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


/**
 * Created by bewusstlos on 10/12/2017.
 */
fun String.isEmailAddress(): Boolean {
    val strExpression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"

    val objPattern = Pattern.compile(strExpression, Pattern.CASE_INSENSITIVE)
    val objMatcher = objPattern.matcher(this)
    return objMatcher.matches()
}

inline fun FragmentManager.performTransaction(func: FragmentTransaction.() -> Unit) {
    val fragmentTransaction = beginTransaction()
    fragmentTransaction.func()
    fragmentTransaction.commit()
}

fun AppCompatActivity.addFragment(fragment: Fragment, frameId: Int) {
    supportFragmentManager.performTransaction { add(frameId, fragment) }
}

fun Query.observe(): Observable<DataSnapshot?> {
    return Observable.create({
        val listener = this.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                //
            }

            override fun onDataChange(p0: DataSnapshot?) {
                it.onNext(p0!!)
            }
        })

        it.setCancellable {
            this.removeEventListener(listener)
        }
    })
}

fun <T> Observable<DataSnapshot?>.toObjectObservable(type: Class<T>): Observable<T>? {
    return this.map {
        it.value as T
    }
}

fun AppCompatActivity.replaceFragment(fragment: Fragment, frameId: Int, animToLeft: Boolean?) {
    supportFragmentManager.performTransaction {
        if (animToLeft != null) {
            if (animToLeft)
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            else
                setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        replace(frameId, fragment)
    }
}

fun AppCompatActivity.navigateTo(cls: Class<*>, activityFlags: Int? = null) {
    val i = Intent(this, cls)
    if (activityFlags != null)
        i.flags = activityFlags
    startActivity(i)
}

fun AppCompatActivity.reattachFragment() {
    val currentFragemnt = supportFragmentManager.findFragmentById(R.id.fragment_container)
    if (currentFragemnt != null) {
        supportFragmentManager.performTransaction {
            detach(currentFragemnt)
            attach(currentFragemnt)
        }
    }
}

fun Fragment.navigateTo(context: Context, cls: Class<*>, activityFlags: Int? = null) {
    val i = Intent(context, cls)
    if (activityFlags != null)
        i.flags = activityFlags
    startActivity(i)
}

fun Calendar.toHumanString(format: String = "dd MMMM yyyy"): String {
    val dateFormat = SimpleDateFormat(format)
    return dateFormat.format(this.time)
}
