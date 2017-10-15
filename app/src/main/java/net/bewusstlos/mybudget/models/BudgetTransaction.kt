package net.bewusstlos.mybudget.models

import java.util.*

/**
 * Created by bewusstlos on 10/14/2017.
 */
interface BudgetTransaction {
    val date: Date?
    val value: Float?
}