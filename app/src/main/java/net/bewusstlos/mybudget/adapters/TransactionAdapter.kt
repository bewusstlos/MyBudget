package net.bewusstlos.mybudget.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.transaction_item.view.*
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.common.toHumanString
import net.bewusstlos.mybudget.models.BudgetTransaction
import java.util.*

/**
 * Created by bewusstlos on 10/16/2017.
 */
class TransactionAdapter(val context: Context, var list: MutableList<BudgetTransaction>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater
                .from(context).inflate(R.layout.transaction_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {

        if (position == 0)
            (holder as ViewHolder).bind(list[position], true)
        else {
            val prevItemdate = Calendar.getInstance()
            prevItemdate.timeInMillis = list[position - 1].date
            val currentItemDate = Calendar.getInstance()
            currentItemDate.timeInMillis = list[position]?.date
            if (prevItemdate.get(Calendar.DATE) != currentItemDate.get(Calendar.DATE))
                (holder as ViewHolder).bind(list[position], true)
            else
                (holder as ViewHolder).bind(list[position])
        }

    }

    override fun getItemCount() = list.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: BudgetTransaction, shouldShowDate: Boolean = false) = with(itemView) {
            category.text = item.category
            value.text = "%.2f".format(item.value)
            if (shouldShowDate) {
                date.visibility = View.VISIBLE
                var calendar = Calendar.getInstance()
                calendar.timeInMillis = item.date
                date.text = calendar.toHumanString()
            }
        }
    }
}