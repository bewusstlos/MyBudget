package net.bewusstlos.mybudget.services

import com.google.android.gms.tasks.TaskCompletionSource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import net.bewusstlos.mybudget.models.Budget
import net.bewusstlos.mybudget.models.BudgetTransaction
import net.bewusstlos.mybudget.models.Expense
import net.bewusstlos.mybudget.models.Income


/**
 * Created by bewusstlos on 10/13/2017.
 */
class BudgetService {
    var currentBudget: Budget? = null
    fun getBudget(): Budget? {
        val tcs: TaskCompletionSource<Budget?> = TaskCompletionSource()
        val ref = FirebaseDatabase.getInstance().getReference("/users/${FirebaseAuth.getInstance().currentUser?.uid}/budget")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onDataChange(p0: DataSnapshot?) {
                currentBudget = p0?.getValue(Budget::class.java)
                tcs.setResult(p0?.getValue(Budget::class.java))
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
        val budgetTransaction = if (item is Income) item else item as Expense
        FirebaseDatabase.getInstance()
                .getReference("/users/" + FirebaseAuth.getInstance().currentUser?.uid + "/budget/" + budgetTransaction.javaClass.name.toLowerCase() + "s")
                .setValue(item)
    }
}