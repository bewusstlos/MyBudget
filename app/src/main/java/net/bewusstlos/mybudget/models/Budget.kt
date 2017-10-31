package net.bewusstlos.mybudget.models

import com.google.firebase.database.Exclude

/**
 * Created by bewusstlos on 10/13/2017.
 */
open class Budget() {
    var currencyCode: String = ""
    var balance: Float = 0.0f
    var transactions: MutableMap<String, BudgetTransaction> = mutableMapOf()
        set(value) {
            expenses = value.map { it.value }.filter { it.value < 0 }.sortedByDescending { it.date }.toMutableList()
            incomes = value.map { it.value }.filter { it.value > 0 }.sortedByDescending { it.date }.toMutableList()
            field = mutableMapOf()
        }
    @Exclude
    var expenses: MutableList<BudgetTransaction> = mutableListOf()
    @Exclude
    var incomes: MutableList<BudgetTransaction> = mutableListOf()

    constructor(currencyCode: String, balance: Float) : this() {
        this.currencyCode = currencyCode
        this.balance = balance
    }
}