package com.moneytracker.akscorp.moneytracker.ui.statistics

import com.github.mikephil.charting.data.PieDataSet
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.ui.main.TransactionsAdapter
import com.moneytracker.akscorp.moneytracker.utilites.StatisticsPeriod

/**
 *  Created by Alexander Melnikov on 08.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

interface StatisticsContract {

    interface StatisticsView {

        var presenter: Presenter

        fun setupTransactionsRecycler(transactions: List<Transaction>)

        fun setupChartData(dataSet: PieDataSet)

        fun showPeriodDialog(period: StatisticsPeriod)

        fun updatePeriodTextView(stringResource: Int)

        fun updateButtonsState(expensesState: Boolean)

    }

    interface Presenter : TransactionsAdapter.TransactionsRecyclerEventListener {

        fun attach(view: StatisticsView)

        fun start()

        fun requestTransactions()

        fun statisticsPeriodButtonClick()

        fun periodChange(period: StatisticsPeriod)

        fun expensesButtonClick()

        fun incomeButtonClick()
    }
}
