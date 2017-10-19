package net.bewusstlos.mybudget.services

import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import net.bewusstlos.mybudget.models.Budget
import net.bewusstlos.mybudget.models.BudgetTransaction


/**
 * Created by bewusstlos on 10/13/2017.
 */
class BudgetService {

    var currentBudget: Budget? = null

    fun getBudget(): Budget? {
        val tcs: TaskCompletionSource<Budget?> = TaskCompletionSource()
        val ref = FirebaseDatabase.getInstance().getReference("/users/${FirebaseAuth.getInstance().currentUser?.uid}/budget")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                currentBudget = p0?.getValue(Budget::class.java)
                tcs.trySetResult(p0?.getValue(Budget::class.java))
            }
        })

        var t = tcs.task
        try {
            Tasks.await(t)
        } catch (e: Exception) {
            t = Tasks.forException(e)
        }

        if (t.isSuccessful)
            return t.result
        else
            return null
    }

    fun addBudget(item: Budget) {
        FirebaseDatabase.getInstance().getReference("/users/" + FirebaseAuth.getInstance().currentUser?.uid + "/budget").setValue(item)
    }

    fun addTransaction(item: BudgetTransaction) {
        var itemName = ""
        val key = FirebaseDatabase.getInstance().getReference("/users/${FirebaseAuth.getInstance().currentUser?.uid}/budget/transactions").push().key

        FirebaseDatabase.getInstance()
                .getReference("/users/${FirebaseAuth.getInstance().currentUser?.uid}/budget/transactions/$key")
                .setValue(item)
        FirebaseDatabase.getInstance().getReference("/users/${FirebaseAuth.getInstance().currentUser?.uid}/budget/balance")
                .setValue(currentBudget?.balance?.plus(item.value!!))
    }

    fun getTransactions(): List<BudgetTransaction>? {
        val tcs: TaskCompletionSource<List<BudgetTransaction>?> = TaskCompletionSource()
        val ref = FirebaseDatabase.getInstance().getReference("/users/${FirebaseAuth.getInstance().currentUser?.uid}/budget/expenses")
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot?, p1: String?) {
                val s = p0?.value
            }

            override fun onChildChanged(p0: DataSnapshot?, p1: String?) {
                val s = p0?.value
            }

            override fun onChildMoved(p0: DataSnapshot?, p1: String?) {
                val s = p0?.value
            }

            override fun onChildRemoved(p0: DataSnapshot?) {
                val s = p0?.value
            }

            override fun onCancelled(p0: DatabaseError?) {

            }
        })

        var t = tcs.task
        try {
            Tasks.await(t)
        } catch (e: Exception) {
            t = Tasks.forException(e)
        }

        if (t.isSuccessful)
            return t.result
        else
            return null
    }
}