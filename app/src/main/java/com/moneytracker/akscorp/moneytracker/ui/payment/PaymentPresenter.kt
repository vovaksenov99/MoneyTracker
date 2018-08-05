package com.moneytracker.akscorp.moneytracker.ui.payment

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.DialogFragment
import com.moneytracker.akscorp.moneytracker.ScashApp
import com.moneytracker.akscorp.moneytracker.model.defaultCurrency
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Currency
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.model.repository.ITransactionsRepository
import java.util.*
import javax.inject.Inject

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

interface IPaymentDialog {
    fun showConfirmButton()
    fun hideConfirmButton()
    fun finishLayoutAnim()
    fun showRepeatTransactionDialog()
}

class PaymentDialogPresenter(val view: IPaymentDialog,
                             val account: Account) {

    private val TAG = "debug"

    lateinit var model: MyViewModel

    @Inject
    lateinit var transactionsRepository: ITransactionsRepository

    init {
        ScashApp.instance.component.inject(this)
    }

    fun setModel(fragment: DialogFragment) {
        model = ViewModelProviders.of(fragment).get(MyViewModel::class.java)
    }

    fun setSum(sum: Double) {
        model.sum.count = sum
    }

    fun setCurrency(currency: Currency) {
        model.sum.currency = currency
    }

    fun setPurpose(purpose: Transaction.PaymentPurpose) {
        model.purpose = purpose
    }

    fun setDescription(description: String) {
        model.description = description
    }

    fun setDate(date: Date) {
        model.date = date
    }

    fun addTransaction() {
        transactionsRepository.insertTransaction(account, model.sum, model.purpose,
                model.description, model.date, model.repeat, model.repeatMode,
                object : ITransactionsRepository.DefaultTransactionsRepoCallback() {
            override fun onTransactionInsertSuccess(transaction: Transaction) {
                super.onTransactionInsertSuccess(transaction)
                view.finishLayoutAnim()
            }
        })
    }

    fun setRepeat(repeatMode: Int) {
        model.repeat = true
        when (repeatMode) {
            0 -> {
                model.repeat = false
                model.repeatMode = Transaction.RepeatMode.NONE
            }
            1 -> model.repeatMode = Transaction.RepeatMode.DAY
            2 -> model.repeatMode = Transaction.RepeatMode.WEEK
            3 -> model.repeatMode = Transaction.RepeatMode.MONTH
        }
    }
}

class MyViewModel : ViewModel() {
    var sum = Money(0.0, defaultCurrency)
    var purpose = Transaction.PaymentPurpose.OTHER
    var description = ""
    var date = Date()
    var repeat = false
    var repeatMode = Transaction.RepeatMode.NONE
}