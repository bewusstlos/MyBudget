package net.bewusstlos.mybudget.services

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

/**
 * Created by bewusstlos on 10/26/2017.
 */
class CategoriesService {
    companion object {
        var expenseCategories: MutableList<String> = mutableListOf()
        var incomeCategories: MutableList<String> = mutableListOf()

        fun populateCategories(categoriesPath: String) {
            val categoriesRef = FirebaseDatabase.getInstance().getReference(categoriesPath)
            categoriesRef.keepSynced(true)

            categoriesRef.addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                    w
                    if (categoriesPath == "expense_categories")
                        expenseCategories.add(p0?.value as String)
                    else
                        incomeCategories.add(p0?.value as String)
                }

                override fun onChildChanged(p0: DataSnapshot?, p1: String?) {

                }

                override fun onChildMoved(p0: DataSnapshot?, p1: String?) {

                }

                override fun onChildRemoved(p0: DataSnapshot?) {

                }

                override fun onCancelled(p0: DatabaseError?) {
                    Log.e("Categories get", p0.toString())
                }
            })
        }
    }
}