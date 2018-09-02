package com.moneytracker.akscorp.moneytracker.ui.statistics

import com.github.mikephil.charting.data.PieEntry
import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository
import com.moneytracker.akscorp.moneytracker.model.toDefaultCurrency
import com.moneytracker.akscorp.moneytracker.utilites.StatisticsPeriod
import com.moneytracker.akscorp.moneytracker.utilites.StatisticsPeriod.*
import org.joda.time.DateTime
import org.joda.time.Days
import org.joda.time.Hours
import javax.inject.Inject

/**
 *  Created by Alexander Melnikov on 08.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class StatisticsPresenter : StatisticsContract.Presenter{


    lateinit var view: StatisticsContract.StatisticsView

    @Inject
    lateinit var transactionsRepository: ITransactionsRepository

    private var mTransactions: List<Transaction> = ArrayList()
    private var mTransactionsForPeriod = ArrayList<Transaction>()
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
                updateTransactionsForPeriod()
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
        view.setupChartData(categorySums)
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

    private fun updateTransactionsForPeriod() {
        mTransactionsForPeriod.clear()
        val currentDate = DateTime()
        mTransactions.forEach {
            val transactionDate = DateTime(it.date)
            when (mPeriod) {
                DAY -> {
                     if (Hours.hoursBetween(transactionDate.toLocalDateTime(),
                                     currentDate.toLocalDateTime()).hours >= 24)
                        mTransactionsForPeriod.add(it)
                }
                WEEK -> {
                    if (Days.daysBetween(transactionDate.toLocalDateTime(),
                                    currentDate.toLocalDateTime()).days <= 7)
                        mTransactionsForPeriod.add(it)
                }
                MONTH -> {
                    if (currentDate.year() == transactionDate.year() &&
                            currentDate.monthOfYear() == transactionDate.monthOfYear())
                        mTransactionsForPeriod.add(it)
                }
                ALL_TIME -> {
                    mTransactionsForPeriod.add(it)
                }
            }
        }
    }

    override fun statisticsPeriodButtonClick() {
        view.showPeriodDialog(mPeriod)
    }

    override fun onClick(position: Int, transaction: Transaction) {}

    override fun periodChange(period: StatisticsPeriod) {
        if (mPeriod != period) {
            mPeriod = period
            updateTransactionsForPeriod()
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
        if (!mTransactionsForPeriod.isEmpty()) view.updateChartCenterText(mStatisticsModeExpenses)
    }

    override fun incomeButtonClick() {
        mStatisticsModeExpenses = false
        view.updateButtonsState(mStatisticsModeExpenses)
        updateChartData()
        updateRecyclerData()
        if (!mTransactionsForPeriod.isEmpty()) view.updateChartCenterText(mStatisticsModeExpenses)
    }
}