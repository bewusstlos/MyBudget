package net.bewusstlos.mybudget.common

import android.content.Context
import android.content.Intent
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
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
