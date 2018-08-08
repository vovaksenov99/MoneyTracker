package com.moneytracker.akscorp.moneytracker.ui.statistics

import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository
import com.moneytracker.akscorp.moneytracker.model.toDefaultCurrency
import com.moneytracker.akscorp.moneytracker.utilites.StatisticsPeriod
import com.moneytracker.akscorp.moneytracker.utilites.StatisticsPeriod.WEEK
import javax.inject.Inject

/**
 *  Created by Alexander Melnikov on 08.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class StatisticsPresenter : StatisticsContract.Presenter{

    private val TAG = "debug"

    lateinit var view: StatisticsContract.StatisticsView

    @Inject
    lateinit var transactionsRepository: ITransactionsRepository

    private var mTransactions: List<Transaction> = ArrayList()
    private var mStatisticsModeExpenses: Boolean = true
    private var mPeriod: StatisticsPeriod = WEEK

    override fun attach(view: StatisticsContract.StatisticsView) {
        ScashApp.instance.component.inject(this)
        this.view = view
    }

    override fun start() {
        view.updatePeriodTextView(mPeriod.getStringResource())
        requestTransactions()
    }

    override fun requestTransactions() {
        transactionsRepository.getAllTransactions(object : ITransactionsRepository.DefaultTransactionsRepoCallback() {
            override fun onAllTransactionsLoaded(transactions: List<Transaction>) {
                super.onAllTransactionsLoaded(transactions)
                mTransactions = transactions
                updateRecyclerData()
                updateChartData()
            }
        })
    }

    private fun updateChartData() {

        val chartEntries: ArrayList<PieEntry> = ArrayList()
        val categorySums: HashMap<Transaction.PaymentPurpose, Double> = HashMap()
        var transactionSum = 0.0

        mTransactions.forEach {
            if (mStatisticsModeExpenses && it.moneyQuantity.count < 0) {
                transactionSum -= toDefaultCurrency(it.moneyQuantity).count
            } else if (!mStatisticsModeExpenses && it.moneyQuantity.count > 0) {
                transactionSum += toDefaultCurrency(it.moneyQuantity).count
            }


            if (transactionSum != 0.0) {
                if (categorySums.containsKey(it.paymentPurpose)) {
                    transactionSum += categorySums[it.paymentPurpose] ?: 0.0
                }
                categorySums[it.paymentPurpose] = transactionSum
            }


            transactionSum = 0.0
        }

        categorySums.forEach {
            chartEntries.add(PieEntry(it.value.toFloat(), it.key.toString()))
        }
        view.setupChartData(PieDataSet(chartEntries, "Expenses"))
    }

    private fun updateRecyclerData() {
        val transactionsForUpdate = ArrayList<Transaction>()
        mTransactions.forEach {
            if ((mStatisticsModeExpenses && it.moneyQuantity.count < 0) ||
                    (!mStatisticsModeExpenses && it.moneyQuantity.count > 0))
                transactionsForUpdate.add(it)
        }
        view.setupTransactionsRecycler(transactionsForUpdate)
    }

    override fun statisticsPeriodButtonClick() {
        view.showPeriodDialog(mPeriod)
    }

    override fun onClick(position: Int, transaction: Transaction) {}

    override fun periodChange(period: StatisticsPeriod) {
        if (mPeriod != period) {
            mPeriod = period
            updateChartData()
            updateRecyclerData()
            view.updatePeriodTextView(period.getStringResource())
        }
    }

    override fun expensesButtonClick() {
        mStatisticsModeExpenses = true
        view.updateButtonsState(mStatisticsModeExpenses)
        updateChartData()
        updateRecyclerData()
    }

    override fun incomeButtonClick() {
        mStatisticsModeExpenses = false
        view.updateButtonsState(mStatisticsModeExpenses)
        updateChartData()
        updateRecyclerData()
    }
}