package net.bewusstlos.mybudget.models

/**
 * Created by bewusstlos on 10/17/2017.
 */
class BudgetTransaction() {
    var value: Float = 0F
    var date: Long = 0L
    var category: String = ""

    constructor(value: Float, date: Long, category: String) : this() {
        this.value = value
        this.date = date
        this.category = category
    }

}