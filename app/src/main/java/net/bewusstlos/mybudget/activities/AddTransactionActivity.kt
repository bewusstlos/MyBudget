package net.bewusstlos.mybudget.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_transaction.*
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.services.ServicesContainer
import org.jetbrains.anko.find
import java.util.*

class AddTransactionActivity : AppCompatActivity() {

    lateinit var tvValue: TextView
    var bufferedValue: String = ""
    var bufferedOperator: String? = null
    var shouldClearAfterOperator = false

    fun calcButtonClick(view: View?) {
        when (view?.id) {
            R.id.number_0 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.number_1 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.number_2 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.number_3 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.number_4 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.number_5 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.number_6 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.number_7 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.number_8 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.number_9 -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.dot -> {
                addSymbolToValue((view as Button).text)
            }

            R.id.plus -> {
                addOperator((view as Button).text)
            }

            R.id.minus -> {
                addOperator((view as Button).text)
            }

            R.id.multiply -> {
                addOperator((view as Button).text)
            }

            R.id.divide -> {
                addOperator((view as Button).text)
            }

            R.id.equal -> {
                performCalculatiion("=")
            }

            R.id.backspace -> {
                tvValue.text = tvValue.text.toString().dropLast(1)
                tvValue.text = if (tvValue.text == "") "0" else tvValue.text
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        tvValue = find(R.id.value)
        currency.text = Currency.getInstance(ServicesContainer.budgetService.currentBudget?.currencyCode).symbol
    }

    fun addSymbolToValue(symbol: CharSequence) {
        if (shouldClearAfterOperator) {
            tvValue.text = "0"
            shouldClearAfterOperator = false
        }

        if (tvValue.text == "0")
            tvValue.text = symbol
        else if (symbol == "." && tvValue.text.contains("."))
            return
        else
            tvValue.text = tvValue.text.toString() + symbol
    }

    fun addOperator(symbol: CharSequence) {
        if (bufferedOperator == null && shouldClearAfterOperator == false) {
            bufferedOperator = symbol.toString()
            bufferedValue = tvValue.text.toString()
            tvValue.text = tvValue.text.toString() + symbol
            shouldClearAfterOperator = true
        } else {
            performCalculatiion(symbol)
            addOperator(symbol)
        }
    }

    fun performCalculatiion(symbol: CharSequence? = null) {
        //TODO: fix bug when you press operator and then equal
        if (tvValue.text.last() == '+' || tvValue.text.last() == '-' || tvValue.text.last() == '×' || tvValue.text.last() == '÷') {
            if (symbol == "=") {
                bufferedOperator = null
                tvValue.text = tvValue.text.toString().dropLast(1)
                bufferedValue = ""
                shouldClearAfterOperator = false
                return
            }
            bufferedOperator = symbol.toString()
            tvValue.text = tvValue.text.toString().dropLast(1) + symbol
            return
        }

        when (bufferedOperator) {
            "+" -> {
                tvValue.text = (bufferedValue.toFloat() + tvValue.text.toString().toFloat()).toString()
            }

            "-" -> {
                tvValue.text = (bufferedValue.toFloat() - tvValue.text.toString().toFloat()).toString()
            }

            "×" -> {
                tvValue.text = (bufferedValue.toFloat() * tvValue.text.toString().toFloat()).toString()
            }

            "÷" -> {
                tvValue.text = (bufferedValue.toFloat() / tvValue.text.toString().toFloat()).toString()
            }
        }

        bufferedOperator = null
        bufferedValue = ""
    }
}
