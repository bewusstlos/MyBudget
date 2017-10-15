package net.bewusstlos.mybudget.services

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query

/**
 * Created by bewusstlos on 10/14/2017.
 */
class DatabaseQuery {

    lateinit var path: String
    var orderByChild: String? = null

    fun build(): Query {
        var query: Query = FirebaseDatabase.getInstance().reference.child(path)
        if (orderByChild != null) {
            query = query.orderByChild(orderByChild)
        }
        return query
    }
}