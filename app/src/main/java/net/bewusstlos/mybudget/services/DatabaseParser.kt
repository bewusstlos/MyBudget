package net.bewusstlos.mybudget.services

import com.google.firebase.database.DataSnapshot
import io.reactivex.Observable

/**
 * Created by bewusstlos on 10/14/2017.
 */
fun <T> Observable<DataSnapshot?>.toObjectObservable(type: Class<T>): Observable<T>? {
    return this.map {
        it.value as T
    }
}