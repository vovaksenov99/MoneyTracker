package com.moneytracker.akscorp.moneytracker.ui.payment

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import com.moneytracker.akscorp.moneytracker.model.DeprecetadTransaction
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Currency
import java.util.*

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

interface IPaymentDialog {
    fun showConfirmButton()
    fun hideConfirmButton()
    fun addTransaction()
}

class PaymentDialogPresenter(val view: IPaymentDialog, val account: Account) {
    lateinit var model: MyViewModel

    init {
    }

    fun setModel(fragmentActivity: Fragment) {
        model = ViewModelProviders.of(fragmentActivity).get(MyViewModel::class.java)
    }

    fun setSum(sum: Double) {
        model.deprecetadTransaction.moneyQuantity.count = sum
    }

    fun setCurrency(currency: Currency) {
        model.deprecetadTransaction.moneyQuantity.currency = currency
    }

    fun setPurpose(purpose: DeprecetadTransaction.PaymentPurpose) {
        model.deprecetadTransaction.paymentPurpose = purpose
    }

    fun setDescription(description: String) {
        model.deprecetadTransaction.paymentDescription = description
    }

    fun setDate(date: Calendar) {
        model.deprecetadTransaction.date = date
    }

    fun addTransaction() {

    }
}

class MyViewModel : ViewModel() {
    var deprecetadTransaction: DeprecetadTransaction = DeprecetadTransaction()
}