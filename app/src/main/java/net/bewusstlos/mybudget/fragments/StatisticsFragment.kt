package net.bewusstlos.mybudget.fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.IValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.ViewPortHandler
import net.bewusstlos.mybudget.R
import net.bewusstlos.mybudget.services.ServicesContainer
import org.jetbrains.anko.find

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [StatisticsFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [StatisticsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class StatisticsFragment : Fragment() {

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
        val view = inflater!!.inflate(R.layout.fragment_statistics, container, false)
        val expensesList = ServicesContainer.budgetService.currentBudget?.transactions?.map { it.value }?.filter { it.value < 0 }?.groupBy { it.category }
        var expensesData: MutableMap<String, Double> = mutableMapOf()
        if (expensesList != null)
            for (expense in expensesList) {
                expensesData.put(expense.key, expense.value.sumByDouble { it.value.toDouble() })
            }
        var expensesChart = view.find<PieChart>(R.id.expenses_chart)
        if (expensesList != null) {
            val expensesPieEntries = mutableListOf<PieEntry>()
            for (item in expensesData) {
                expensesPieEntries.add(PieEntry(item.value.toFloat() * -1, item.key))
            }
            var pieDataSet = PieDataSet(expensesPieEntries, "Expenses")
            pieDataSet.colors = ColorTemplate.MATERIAL_COLORS.toMutableList()
            pieDataSet.valueTextSize = 20F
            pieDataSet.valueTextColor = resources.getColor(R.color.colorPrimary)
            pieDataSet.valueFormatter = object : IValueFormatter {
                override fun getFormattedValue(value: Float, entry: Entry?, dataSetIndex: Int, viewPortHandler: ViewPortHandler?): String {
                    return "${"%.2f".format(value)} %"
                }
            }
            pieDataSet.sliceSpace = 3F
            var pieData = PieData(pieDataSet)
            expensesChart.data = PieData(pieDataSet)
            expensesChart.setUsePercentValues(true)
            expensesChart.invalidate()
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
         * @return A new instance of fragment StatisticsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): StatisticsFragment {
            val fragment = StatisticsFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor
