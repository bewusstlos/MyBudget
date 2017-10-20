package net.bewusstlos.mybudget.fragments

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_expenses.view.*
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.adapters.TransactionAdapter
import net.bewusstlos.mybudget.services.ServicesContainer

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ExpensesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ExpensesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpensesFragment : Fragment() {

    val TRANSACTIONS_RECEIVED = "Transactions received"

    lateinit var expensesAdapter: TransactionAdapter
    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater!!.inflate(R.layout.fragment_expenses, container, false)
        ServicesContainer.budgetService.getTransactions()
        with(view) {
                expenses.layoutManager = LinearLayoutManager(this@ExpensesFragment.context)
            if (ServicesContainer.budgetService.currentBudget != null) {
                expensesAdapter = TransactionAdapter(this@ExpensesFragment.context, ServicesContainer.budgetService.currentBudget!!.expenses)
                expenses.adapter = expensesAdapter
            }

        }
        val intentFilter = IntentFilter(TRANSACTIONS_RECEIVED)

        val broadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(p0: Context?, p1: Intent?) {
                expensesAdapter.notifyDataSetChanged()
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        expensesAdapter.notifyDataSetChanged()

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExpensesFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): ExpensesFragment {
            val fragment = ExpensesFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
