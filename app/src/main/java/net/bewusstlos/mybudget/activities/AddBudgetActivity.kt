package net.bewusstlos.mybudget.activities

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_budget.*
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.common.navigateTo
import net.bewusstlos.mybudget.models.Budget
import net.bewusstlos.mybudget.services.ServicesContainer
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.util.*

class AddBudgetActivity : AppCompatActivity() {

    val allCurrencies = Currency.getAvailableCurrencies().toList()
    var selectedCurrency: Currency? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_budget)
        selectedCurrency = Currency.getInstance(resources.configuration.locale)
        selected_currency.text = selectedCurrency?.currencyCode
        selected_currency2.text = selectedCurrency?.currencyCode


        s_currency.onClick {
            val selectCurrencyDialog = AlertDialog.Builder(this@AddBudgetActivity)
                    .setTitle(resources.getString(R.string.select_currency))
                    .setItems(allCurrencies.map { it -> it.displayName }.toTypedArray(), DialogInterface.OnClickListener { _, i ->
                        selected_currency.text = allCurrencies[i].currencyCode
                        selected_currency2.text = allCurrencies[i].currencyCode
                    })
                    .create()
            selectCurrencyDialog.show()
        }

        b_done.onClick {
            if (selectedCurrency != null)
                ServicesContainer.budgetService.addBudget(Budget(selected_currency.text.toString(), et_money_amount.text.toString().toFloat()))
            navigateTo(MainActivity::class.java)
        }
    }
}
