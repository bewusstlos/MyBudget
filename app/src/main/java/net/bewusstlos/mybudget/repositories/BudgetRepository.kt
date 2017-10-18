package net.bewusstlos.mybudget.repositories

/**
 * Created by bewusstlos on 10/17/2017.
 */
class BudgetRepository {
    /*fun create(item: Budget): Boolean {
        try{
            val realm = Realm.getDefaultInstance()
                realm.executeTransaction({
                    val budget = realm.createObject(Budget::class.java, item.id)
                    budget.balance = item.balance
                    budget.currencyCode = item.currencyCode
                })
            return true
        }
        catch(e:Exception){
            Log.e("Realm", e.message)
            return false
        }
    }

    fun get(): Budget? {
        try{
            with(Realm.getDefaultInstance()){
                return where(net.bewusstlos.mybudget.models.Budget::class.java)
                        .findFirst()

            }
        }
        catch(e: Exception){
            Log.e("Realm", e.message)
            return null
        }

    }

    fun <T> updateBalance(item: T): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

     fun delete(id: String): Boolean {
        try{
            with(Realm.getDefaultInstance()){
                beginTransaction()
                val query = where(Budget::class.java)
                        .equalTo("id", id)
                        .findAll()
                query.deleteAllFromRealm()
                commitTransaction()

            }
            return true
        }
        catch (e:Exception){
            Log.e("Realm", e.message)
            return false
        }
    }*/
}