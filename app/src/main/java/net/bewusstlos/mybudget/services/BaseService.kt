package net.bewusstlos.mybudget.services

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable

/**
 * Created by bewusstlos on 10/14/2017.
 */
fun Query.observe(): Observable<DataSnapshot?> {
    return Observable.create({
        val listener = this.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                //
            }

            override fun onDataChange(p0: DataSnapshot?) {
                it.onNext(p0!!)
            }
        })

        it.setCancellable {
            this.removeEventListener(listener)
        }
    })
}