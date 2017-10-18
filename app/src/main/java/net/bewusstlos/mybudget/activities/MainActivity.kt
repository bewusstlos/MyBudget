package net.bewusstlos.mybudget.activities

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_main.*
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.common.addFragment
import net.bewusstlos.mybudget.common.navigateTo
import net.bewusstlos.mybudget.common.replaceFragment
import net.bewusstlos.mybudget.fragments.ExpensesFragment
import net.bewusstlos.mybudget.fragments.IncomeFragment
import net.bewusstlos.mybudget.fragments.StatisticsFragment
import net.bewusstlos.mybudget.services.ServicesContainer
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.find
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

class MainActivity : AppCompatActivity() {
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
        val budget = ServicesContainer.budgetService.getBudget()
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
                income.visibility = View.VISIBLE
                expense.visibility = View.VISIBLE
                income.startAnimation(incomeExpenseAnim)
                expense.startAnimation(incomeExpenseAnim)
                add.startAnimation(anim)
            } else {
                income.startAnimation(reverseIncomeExpenseAnim)
                expense.startAnimation(reverseIncomeExpenseAnim)
                add.startAnimation(reverseAnim)
                doAsync {
                    Thread.sleep(anim.duration)
                    runOnUiThread({
                        income.visibility = View.GONE
                        expense.visibility = View.GONE
                    })
                }

            }
            //navigateTo(inflater.context, AddTransactionActivity::class.java)
        }

        b_add_income.onClick {
            val i = Intent(this@MainActivity, AddTransactionActivity::class.java)
            i.putExtra("TypeOfTransaction", "Income")
            startActivity(i)
        }

        b_add_expense.onClick {
            val i = Intent(this@MainActivity, AddTransactionActivity::class.java)
            i.putExtra("TypeOfTransaction", "Expense")
            startActivity(i)
        }
    }

    inline fun setBudgetTitle() {
        toolbar.title = "${resources.getString(R.string.app_name)}: ${ServicesContainer.budgetService.currentBudget?.balance} " +
                "${Currency.getInstance(ServicesContainer.budgetService.currentBudget?.currencyCode).symbol}"
    }

    override fun onResume() {
        super.onResume()
        setBudgetTitle()
    }
}