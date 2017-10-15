package net.bewusstlos.mybudget.models

/**
 * Created by bewusstlos on 10/13/2017.
 */
class Budget {
    lateinit var currencyCode: String
    var balance: Float = 0.0f
    val incomes: List<Income>? = null
    val expenses: List<Expense>? = null

    constructor() {}

    constructor(currencyCode: String, balance: Float) {
        this.currencyCode = currencyCode
        this.balance = balance
    }
}