package net.bewusstlos.mybudget.fragments

import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.activities.AddTransactionActivity
import net.bewusstlos.mybudget.common.navigateTo
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ExpensesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ExpensesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpensesFragment : Fragment() {

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
        val addExpenses = view.findViewById<FloatingActionButton>(R.id.add_expenses)
        addExpenses.onClick {
            navigateTo(inflater.context, AddTransactionActivity::class.java)
        }
        return view
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
