package net.bewusstlos.mybudget

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import net.bewusstlos.mybudget.services.BudgetService
import net.bewusstlos.mybudget.services.ServicesContainer
import net.bewusstlos.mybudget.services.UserInteractionService

/**
 * Created by bewusstlos on 10/11/2017.
 */
class MyBudget : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(
                RealmConfiguration.Builder()
                        .name("mybudget.realm")
                        .build()
        )

        ServicesContainer.userInteractionService = UserInteractionService()
        ServicesContainer.budgetService = BudgetService()
    }
}