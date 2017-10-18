package net.bewusstlos.mybudget.repositories

/**
 * Created by bewusstlos on 10/17/2017.
 */
class TransactionRepository {
    /*companion object {
        fun create(item: BudgetTransaction, budgetId: String) : Boolean{
            try{
                val realm = Realm.getDefaultInstance()
                val budget = realm.where(Budget::class.java)
                        .equalTo("id", budgetId)
                        .findFirst()
                realm.executeTransaction({
                    val transaction = realm.createObject(BudgetTransaction::class.java, item.id)
                    transaction.category = item.category
                    transaction.value = item.value
                    transaction.date = item.date
                    budget?.transactions?.add(transaction)
                })
                return true
            }
            catch (e: Exception){
                Log.e("Realm", e.message)
                return false
            }
        }

        fun getAll(budgetId: String) : List<BudgetTransaction>?{
            try{
                with(Realm.getDefaultInstance()){
                    return where(BudgetTransaction::class.java)
                            .equalTo("budget.id", budgetId)
                            .findAll().toList()
                }
            }
            catch (e: Exception){
                Log.e("Realm", e.message)
                return null
            }
        }
    }*/
}