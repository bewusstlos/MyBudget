package net.bewusstlos.mybudget.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_main.*
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.common.addFragment
import net.bewusstlos.mybudget.common.navigateTo
import net.bewusstlos.mybudget.common.reattachFragment
import net.bewusstlos.mybudget.common.replaceFragment
import net.bewusstlos.mybudget.fragments.ExpensesFragment
import net.bewusstlos.mybudget.fragments.IncomeFragment
import net.bewusstlos.mybudget.fragments.StatisticsFragment
import net.bewusstlos.mybudget.services.FileLoaderService
import net.bewusstlos.mybudget.services.ImageRecognitionService
import net.bewusstlos.mybudget.services.ServicesContainer
import net.hockeyapp.android.CrashManager
import org.jetbrains.anko.alert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.indeterminateProgressDialog
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.opencv.android.OpenCVLoader
import pl.aprilapps.easyphotopicker.EasyImage
import java.io.File
import java.lang.Exception
import java.util.*


class MainActivity : AppCompatActivity() {
    companion object {
        val REQUEST_TAKE_PHOTO = 43
        val REQUEST_PERMISSIONS = 76
        var currentPhotoPath = ""
    }
    var prevIndex: Int = 0
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val isAnimToLeft: Boolean?

        when (item.itemId) {
            R.id.navigation_home -> {
                replaceFragment(ExpensesFragment.newInstance(), R.id.fragment_container, isAnimToLeft(0))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                replaceFragment(IncomeFragment.newInstance(), R.id.fragment_container, isAnimToLeft(1))
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                replaceFragment(StatisticsFragment.newInstance(), R.id.fragment_container, isAnimToLeft(2))
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    fun isAnimToLeft(currentIndex: Int): Boolean? {
        var res: Boolean? = null
        if (currentIndex > prevIndex)
            res = true
        else if (currentIndex < prevIndex)
            res = false
        else
            res = null
        prevIndex = currentIndex
        return res
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        }

        CrashManager.register(this)
        val toolbar: Toolbar = find(R.id.toolbar)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        b_more.onClick {
            val popupMenu = PopupMenu(this@MainActivity, b_more)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.log_out -> {
                        ServicesContainer.userInteractionService.logOut()
                        navigateTo(LoginActivity::class.java, Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        return@setOnMenuItemClickListener true
                    }
                }
                false
            }
            popupMenu.show()
        }
        addFragment(ExpensesFragment.newInstance(), R.id.fragment_container)

        add.onClick {
            val anim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.rotate_and_scale_down)
            val incomeExpenseAnim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.f_buttons_move_up)
            val reverseAnim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.rotate_and_scale_up)
            val reverseIncomeExpenseAnim = AnimationUtils.loadAnimation(this@MainActivity, R.anim.f_buttons_move_down)

            if (income.visibility == View.GONE) {
                recognize.visibility = View.VISIBLE
                income.visibility = View.VISIBLE
                expense.visibility = View.VISIBLE

                recognize.startAnimation(incomeExpenseAnim)
                income.startAnimation(incomeExpenseAnim)
                expense.startAnimation(incomeExpenseAnim)
                add.startAnimation(anim)
            } else {
                recognize.startAnimation(reverseIncomeExpenseAnim)
                income.startAnimation(reverseIncomeExpenseAnim)
                expense.startAnimation(reverseIncomeExpenseAnim)
                add.startAnimation(reverseAnim)
                doAsync {
                    Thread.sleep(anim.duration)
                    runOnUiThread({
                        recognize.visibility = View.GONE
                        income.visibility = View.GONE
                        expense.visibility = View.GONE
                    })
                }

            }
            //navigateTo(inflater.context, AddTransactionActivity::class.java)
        }

        b_add_income.onClick {
            add.performClick()
            val i = Intent(this@MainActivity, AddTransactionActivity::class.java)
            i.putExtra("TypeOfTransaction", "Income")
            startActivity(i)
        }

        b_add_expense.onClick {
            add.performClick()
            val i = Intent(this@MainActivity, AddTransactionActivity::class.java)
            i.putExtra("TypeOfTransaction", "Expense")
            startActivity(i)
        }

        b_recognize.onClick {
            if (!File("${Environment.getExternalStorageDirectory()}/tessdata/${FileLoaderService.TESS_DATA_FILE_NAME}").exists()) {
                if (ContextCompat.checkSelfPermission(this@MainActivity,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this@MainActivity,
                            arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                            REQUEST_PERMISSIONS)
                }
            } else {
                add.performClick()
                doAsync {
                    requestTessData()
                    runOnUiThread { EasyImage.openChooserWithGallery(this@MainActivity, "Choose photo", 0) }
                }
            }
        }
    }

    inline fun setBudgetTitle() {
        toolbar.title = "${resources.getString(R.string.app_name)}: ${ServicesContainer.budgetService.currentBudget?.balance} " +
                Currency.getInstance(ServicesContainer.budgetService.currentBudget?.currencyCode).symbol
    }

    override fun onResume() {
        super.onResume()
        reattachFragment()
        setBudgetTitle()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        EasyImage.handleActivityResult(requestCode, resultCode, data, this@MainActivity, object : EasyImage.Callbacks {
            override fun onImagePicked(imageFile: File?, source: EasyImage.ImageSource?, type: Int) {
                if (imageFile != null) {
                    val pd = indeterminateProgressDialog("Recognizing")
                    doAsync {
                        runOnUiThread { pd.show() }
                        val s = ImageRecognitionService.getReceiptItems(imageFile)
                        runOnUiThread {
                            alert {
                                message = s.toString()
                                positiveButton("OK", {
                                    it.dismiss()
                                })
                            }.show()
                        }
                        //Log.i("Receipt raw", s)
                        runOnUiThread { pd.hide() }
                    }
                }
            }

            override fun onImagePickerError(e: Exception?, source: EasyImage.ImageSource?, type: Int) {
                Log.e("EasyImage", e?.message)
            }

            override fun onCanceled(source: EasyImage.ImageSource?, type: Int) {
                Log.d("EasyImage", "getting image was canceled")
                if (source === EasyImage.ImageSource.CAMERA) {
                    val photoFile = EasyImage.lastlyTakenButCanceledPhoto(this@MainActivity)
                    photoFile?.delete()
                }
            }
        })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_DENIED)
            return
        requestTessData()
    }

    fun requestTessData() {
        //val alert = this@MainActivity.indeterminateProgressDialog("Downloading trained data...")
        if (FileLoaderService.shouldUpdateTessData()) {
            FileLoaderService.getTessData(this@MainActivity)
        }
    }
}