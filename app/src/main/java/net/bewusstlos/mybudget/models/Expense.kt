package net.bewusstlos.mybudget.models

import java.util.*

/**
 * Created by bewusstlos on 10/13/2017.
 */
class Expense : BudgetTransaction {
    override var date: Date? = null
    override var value: Float = 0F
        set(value: Float) {
            field = value * -1
        }
}