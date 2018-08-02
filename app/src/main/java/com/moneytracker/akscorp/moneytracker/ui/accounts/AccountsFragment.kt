package com.moneytracker.akscorp.moneytracker.ui.accounts

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.di.component.DaggerFragmentComponent
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.getCurrencyStringArray
import com.moneytracker.akscorp.moneytracker.ui.account_card.AccountsLinearLayoutManager
import kotlinx.android.synthetic.main.fragment_accounts.*
import org.jetbrains.anko.defaultSharedPreferences
import javax.inject.Inject

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

class AccountsFragment : Fragment(), AccountsContract.AccountsView {

    private val TAG = "debug"

    @Inject
    override lateinit var presenter: AccountsContract.Presenter

    private lateinit var accountsAdapter: AccountsAdapter

    private var fromWelcomeScreen = false

    private lateinit var dialogCurrencySpinner: Spinner
    private lateinit var dialogEditTextName: EditText

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        DaggerFragmentComponent.builder().build().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        presenter.attach(this)
        fromWelcomeScreen = arguments!!.getBoolean(FROM_WELCOME_SCREEN_KEY)
        return inflater.inflate(R.layout.fragment_accounts, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEventListeners()

        //Setup Accounts Recycler View
        accountsAdapter = AccountsAdapter(activity!!, ArrayList(), presenter)
        accountsAdapter.setHasStableIds(true)
        rv_accounts.setHasFixedSize(true)
        rv_accounts.adapter = accountsAdapter
        rv_accounts.layoutManager = AccountsLinearLayoutManager(activity!!)

        if (fromWelcomeScreen) presenter.addAccountButtonClick()

    }

    private fun setupEventListeners() {
        fab_new_account.setOnClickListener {
            presenter.addAccountButtonClick()
        }
    }

    override fun onStart() {
        super.onStart()
        presenter.requestAccounts()
    }

    override fun replaceAccountsRecyclerData(accounts: List<Account>) {
        accountsAdapter.replaceData(accounts)
    }

    override fun addAccountToRecyclerData(account: Account) {
        Log.d(TAG, "addAccountToRecyclerData: ")
        accountsAdapter.addAccountToEnd(account)

        //Check if app has been launched first time
        val firstLaunch = activity!!.defaultSharedPreferences.getBoolean(
                getString(R.string.sp_key_first_launch), true)
        if (firstLaunch) {
            activity!!.defaultSharedPreferences.edit()
                    .putBoolean(getString(R.string.sp_key_first_launch), false).apply()

            view?.postDelayed({openWelcomeDialog()}, 500)

        }
    }

    override fun openAddAccountDialog(changeExistingAccount: Boolean, accountForChangeName: String) {
        if (!changeExistingAccount) {
            val dialog = MaterialDialog.Builder(activity!!)
                    .title(R.string.add_account_dialog_label)
                    .customView(R.layout.dialog_add_account, true)
                    .positiveText(android.R.string.ok)
                    .negativeText(android.R.string.cancel)
                    .autoDismiss(false)
                    .onNegative { d, w ->
                        d.dismiss()
                        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view?.windowToken, 0)
                    }
                    .onPositive { d, w ->
                        val name = dialogEditTextName.text.toString().trim()
                        if (name.isEmpty()) {
                            dialogEditTextName.error = getString(R.string.dialog_emtpty_acc_name_error)
                        } else if (dialogCurrencySpinner.selectedItem is String) {
                            presenter.addAccount(name, dialogCurrencySpinner.selectedItem as String)
                            d.dismiss()
                            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            imm.hideSoftInputFromWindow(view?.windowToken, 0)
                        }
                    }
                    .build()

            //Setup currency spinner
            dialogCurrencySpinner = dialog.view.findViewById(R.id.currency_spinner)
            dialogEditTextName = dialog.view.findViewById(R.id.et_account_name)
            val currencyAdapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_spinner_item,
                    getCurrencyStringArray())
            currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            dialogCurrencySpinner.adapter = currencyAdapter

            dialogEditTextName.requestFocus()

            dialog.show()
        }
    }

    override fun showAccountAlreadyExistsErrorToast() {
        Toast.makeText(activity!!, resources.getString(R.string.account_already_exists_error),
                Toast.LENGTH_LONG).show()
    }

    private fun openWelcomeDialog() {
        MaterialDialog.Builder(activity!!)
                .title(R.string.dialog_welcome_h1)
                .content(R.string.dialog_welcome_h2)
                .positiveText(R.string.dialog_continue)
                .onPositive {d, w ->
                    d.dismiss()
                    activity!!.onBackPressed()}
                .show()
    }

    companion object {

        const val FROM_WELCOME_SCREEN_KEY: String = "FROM_WELCOME_SCREEN_KEY"

        fun newInstance(fromWelcomeScreen: Boolean): AccountsFragment {
            val args = Bundle()
            args.putBoolean(FROM_WELCOME_SCREEN_KEY, fromWelcomeScreen)
            val fragment = AccountsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}