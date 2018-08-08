package com.moneytracker.akscorp.moneytracker.ui.statistics

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.di.component.DaggerFragmentComponent
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.ui.main.TransactionsAdapter
import com.moneytracker.akscorp.moneytracker.utilites.StatisticsPeriod
import com.moneytracker.akscorp.moneytracker.utilites.StatisticsPeriod.*
import kotlinx.android.synthetic.main.fragment_statistics.*
import javax.inject.Inject

/**
 *  Created by Alexander Melnikov on 08.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class StatisticsFragment : Fragment(), StatisticsContract.StatisticsView {

    private val TAG = "debug"

    @Inject
    override lateinit var presenter: StatisticsContract.Presenter

    private lateinit var transactionsAdapter: TransactionsAdapter

    private lateinit var mSatisticsPeriodDialog: MaterialDialog
    private lateinit var mDialogStatisticsPeriodRadioGroup: RadioGroup

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        DaggerFragmentComponent.builder().build().inject(this)
        presenter.attach(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup transactions RV
        transactionsAdapter = TransactionsAdapter(ArrayList(), presenter)
        transactions_recycler_view.layoutManager = LinearLayoutManager(activity!!, RecyclerView.VERTICAL, true)
        transactions_recycler_view.adapter = transactionsAdapter
        transactions_recycler_view.isNestedScrollingEnabled = false

        //Setup chart
        chart.centerText = getString(R.string.income_button_label_text)
        chart.setUsePercentValues(false)
        chart.description.isEnabled = false
        chart.setDrawEntryLabels(false)

        val l = chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.yOffset = 5f

        expenses_btn.setOnClickListener { presenter.expensesButtonClick() }
        income_btn.setOnClickListener { presenter.incomeButtonClick() }
    }

    override fun onStart() {
        super.onStart()
        presenter.start()
    }

    fun statisticsPeriodButtonClick() {
        presenter.statisticsPeriodButtonClick()
    }

    override fun setupTransactionsRecycler(transactions: List<Transaction>) {
        transactionsAdapter.replaceData(transactions)
    }

    override fun setupChartData(dataSet: PieDataSet) {
        Log.d(TAG, "setupChartData: dataset = $dataSet")
        val colorsArray = resources.obtainTypedArray(R.array.custom_account_colors)
        val colors = ArrayList<Int>()
        for (i in 0 until colorsArray.length()) {
            colors.add(colorsArray.getColor(i, 0))
        }
        colorsArray.recycle()

        dataSet.colors = colors
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        val data = PieData(dataSet)
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.WHITE)
        chart.data = data
        chart.invalidate()
        chart.animateY(400)
    }

    override fun showPeriodDialog(period: StatisticsPeriod) {
        var currentPeriod = period

        mSatisticsPeriodDialog = MaterialDialog.Builder(activity!!)
                .customView(R.layout.dialog_statistics_period, false)
                .title(R.string.statistics_period_dialog_title)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive {
                    d, w -> presenter.periodChange(currentPeriod)
                }
                .build()

        mDialogStatisticsPeriodRadioGroup = mSatisticsPeriodDialog
                .findViewById(R.id.period_radio_group) as RadioGroup
        val radioButtonOption1: RadioButton = mSatisticsPeriodDialog
                .findViewById(R.id.period_1_radio_button) as RadioButton
        val radioButtonOption2: RadioButton = mSatisticsPeriodDialog
                .findViewById(R.id.period_2_radio_button) as RadioButton
        val radioButtonOption3: RadioButton = mSatisticsPeriodDialog
                .findViewById(R.id.period_3_radio_button) as RadioButton
        val radioButtonOption4 = mSatisticsPeriodDialog
                .findViewById(R.id.period_4_radio_button) as RadioButton

        radioButtonOption1.text = getString(DAY.getStringResource())
        radioButtonOption2.text = getString(WEEK.getStringResource())
        radioButtonOption3.text = getString(MONTH.getStringResource())
        radioButtonOption4.text = getString(ALL_TIME.getStringResource())

        fun updateRadioButtons() {
            radioButtonOption1.isChecked = false
            radioButtonOption2.isChecked = false
            radioButtonOption3.isChecked = false
            radioButtonOption4.isChecked = false
            when (period) {
                DAY -> radioButtonOption1.isChecked = true
                WEEK -> radioButtonOption2.isChecked = true
                MONTH -> radioButtonOption3.isChecked = true
                ALL_TIME -> radioButtonOption4.isChecked = true
            }
        }

        radioButtonOption1.setOnCheckedChangeListener { compoundButton, b -> if (b) currentPeriod = DAY}
        radioButtonOption2.setOnCheckedChangeListener { compoundButton, b -> if (b) currentPeriod = WEEK}
        radioButtonOption3.setOnCheckedChangeListener { compoundButton, b -> if (b) currentPeriod = MONTH}
        radioButtonOption4.setOnCheckedChangeListener { compoundButton, b -> if (b) currentPeriod = ALL_TIME}


        updateRadioButtons()
        mSatisticsPeriodDialog.show()
    }

    override fun updatePeriodTextView(stringResource: Int) {
        period_text_view.text = getString(stringResource)
    }

    override fun updateButtonsState(expensesState: Boolean) {
        if (expensesState) {
            expenses_btn.setBackgroundResource(R.drawable.bg_card_black)
            expenses_btn.setTextColor(resources.getColor(android.R.color.white))
            income_btn.setBackgroundResource(R.drawable.bg_card_white)
            income_btn.setTextColor(resources.getColor(android.R.color.black))
            expenses_btn.isClickable = false
            income_btn.isClickable = true
        } else {
            expenses_btn.setBackgroundResource(R.drawable.bg_card_white)
            expenses_btn.setTextColor(resources.getColor(android.R.color.black))
            income_btn.setBackgroundResource(R.drawable.bg_card_black)
            income_btn.setTextColor(resources.getColor(android.R.color.white))
            expenses_btn.isClickable = true
            income_btn.isClickable = false
        }
    }
}

