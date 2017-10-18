package net.bewusstlos.mybudget.models

/**
 * Created by bewusstlos on 10/13/2017.
 */
open class Budget() {
    var currencyCode: String = ""
    var balance: Float = 0.0f
    var transactions: MutableMap<String, BudgetTransaction> = mutableMapOf()

    constructor(currencyCode: String, balance: Float) : this() {
        this.currencyCode = currencyCode
        this.balance = balance
    }
}