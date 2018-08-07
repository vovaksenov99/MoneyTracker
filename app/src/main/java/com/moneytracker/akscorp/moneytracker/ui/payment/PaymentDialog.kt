package com.moneytracker.akscorp.moneytracker.ui.payment

import android.app.DatePickerDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.afollestad.materialdialogs.MaterialDialog
import com.moneytracker.akscorp.moneytracker.DEFAULT_ANIMATION_DURATION
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import com.moneytracker.akscorp.moneytracker.model.entities.Currency
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import com.moneytracker.akscorp.moneytracker.utilites.expand
import com.moneytracker.akscorp.moneytracker.utilites.fadeDown
import com.moneytracker.akscorp.moneytracker.utilites.fadeIn
import kotlinx.android.synthetic.main.dialog_new_payment.*
import kotlinx.android.synthetic.main.dialog_new_payment.view.*
import org.jetbrains.anko.dimen
import java.util.*

val PAYMENT_DIALOG_TAG = "PAYMENT_DIALOG_TAG"


class PaymentDialog : DialogFragment(), IPaymentDialog {

    private val TAG = "debug"

    lateinit var presenter: PaymentDialogPresenter

    lateinit var fragmentView: View

    var lastPressedChooser = ChooseButton.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialog)

        if (arguments != null) {
            if (arguments!!.containsKey("account")) {
                val account = arguments!!.getParcelable("account") as Account
                presenter = PaymentDialogPresenter(this, account)
            }
        }
        presenter.setModel(this)

        Log.d(TAG, "onCreate: ")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        fragmentView = inflater.inflate(R.layout.dialog_new_payment, container, false)
        Log.d(TAG, "onCreateView: ")
        return fragmentView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupEventListeners()
        initUI()

        // Setup toolbar
        fragmentView.toolbar.title = presenter.account.name
        fragmentView.toolbar.setNavigationIcon(R.drawable.ic_arrow_white)
        fragmentView.toolbar.setNavigationOnClickListener {
            this@PaymentDialog.dismiss()
        }

        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        fragmentView.choose_rv.setHasFixedSize(true)
        fragmentView.choose_rv.layoutManager = layoutManager
        fragmentView.choose_rv.isNestedScrollingEnabled = true
    }

    private fun setupEventListeners() {
        categoty_btn.setOnClickListener {
            if (lastPressedChooser == ChooseButton.CATEGORY) {
                expand(rv_container, 0)
                lastPressedChooser = ChooseButton.NONE
                return@setOnClickListener
            }

            lastPressedChooser = ChooseButton.CATEGORY

            initCategories()
            choose_rv.measure(0, 0)
            expand(rv_container,
                    choose_rv.measuredHeight)
        }
        currency_btn.setOnClickListener {
            if (lastPressedChooser == ChooseButton.CURRENCY) {
                expand(rv_container, 0)
                lastPressedChooser = ChooseButton.NONE
                return@setOnClickListener
            }

            lastPressedChooser = ChooseButton.CURRENCY

            initCurrencies()
            choose_rv.measure(0, 0)
            expand(rv_container,
                    choose_rv.measuredHeight)
        }

        date_btn.setOnClickListener {
            datePicker()
            lastPressedChooser = ChooseButton.DATE
        }

        confirm_btn.setOnClickListener {
            sum_et.isEnabled = false
            transaction_description_et.isEnabled = false

            presenter.addTransaction()
        }

        repeat_btn.setOnClickListener {
            showRepeatTransactionDialog()
        }

        sum_et.addTextChangedListener(SumTextWatcher())

        transaction_description_et.addTextChangedListener(DescriptionTextWatcher())
    }

    override fun showConfirmButton() {
        expand(fragmentView.confirm_btn, fragmentView.context.dimen(R.dimen.confirm_button_height))
        confirm_btn.isClickable = true
    }

    override fun hideConfirmButton() {
        confirm_btn.isClickable = false
        expand(fragmentView.confirm_btn, 0)
    }

    override fun finishLayoutAnim() {
        hideKeyboard()

        fadeDown(fragmentView.appBar)
        fadeDown(fragmentView.transaction_description_et)

        fadeIn(fragmentView.done, endAction = {
            fadeDown(this@PaymentDialog.fragmentView,
                endAction = {
                    try {
                        this@PaymentDialog.dismiss()
                    } catch (e: Exception) {
                        Log.e(::PaymentDialog.name, e.toString())
                    }
                }, startDelay = DEFAULT_ANIMATION_DURATION)
        })
    }

    private fun hideKeyboard() {
        activity?.let {

            try {
                it.currentFocus.clearFocus()
                activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            } catch (e: Exception) {
                Log.e(::PaymentDialog.name, e.toString())
            }

        }
    }


    enum class ChooseButton {
        CATEGORY,
        CURRENCY,
        DATE,
        NONE;
    }


    private fun initUI() {
        fragmentView.apply {
            if (presenter.model.sum.count != 0.0)
                sum_et.setText(presenter.model.sum.count.toString())

            transaction_description_et.setText(presenter.model.description)
            categoty_btn.setImageResource(presenter.model.purpose.getIconResource())
            categoty_btn.setColorFilter(ContextCompat.getColor(context, android.R.color.white),
                PorterDuff.Mode.SRC_IN)
            currency_btn.text = presenter.model.sum.currency.currencySymbol
        }
    }

    private fun initCurrencies() {
        fragmentView.choose_rv.adapter = ChooseCurrencyAdapter(Currency.values().toMutableList())
    }

    private fun initCategories() {
        fragmentView.choose_rv.adapter =
                ChooseCategoryAdapter(Transaction.PaymentPurpose.values().toMutableList())
    }

    inner class SumTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            if (s!!.toString().isNotEmpty() && s.toString().toDoubleOrNull() != null) {
                showConfirmButton()
                presenter.setSum(s.toString().toDouble())
            }
            else {
                hideConfirmButton()
            }
        }
    }

    inner class DescriptionTextWatcher : TextWatcher {
        override fun afterTextChanged(s: Editable?) {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            presenter.setDescription(s.toString())
        }
    }

    inner class ChooseCurrencyAdapter(val currencies: List<Currency>) :
        RecyclerView.Adapter<ChooseCurrencyAdapter.CurrencyHolder>() {
        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemCount(): Int {
            return currencies.size
        }

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): CurrencyHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.currency_rv_item, parent, false)

            return CurrencyHolder(view)
        }

        override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
            holder.designation.text = currencies[position].currencySymbol
            holder.name.text = currencies[position].toString()

            holder.itemView.setOnClickListener {
                fragmentView.currency_btn.text = currencies[position].currencySymbol
                expand(fragmentView.rv_container, 0)
                presenter.setCurrency(currencies[position])
            }
        }

        inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val designation: TextView = itemView.findViewById(R.id.designation)
            val name: TextView = itemView.findViewById(R.id.name)
        }

    }

    inner class ChooseCategoryAdapter(val categories: List<Transaction.PaymentPurpose>) :
        RecyclerView.Adapter<ChooseCategoryAdapter.CurrencyHolder>() {

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getItemCount(): Int {
            return categories.size
        }

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): CurrencyHolder {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.category_rv_item, parent, false)

            return CurrencyHolder(view)
        }

        override fun onBindViewHolder(holder: CurrencyHolder, position: Int) {
            holder.icon.setImageResource(categories[position].getIconResource())
            holder.icon.setColorFilter(ContextCompat.getColor(holder.itemView.context,
                android.R.color.white),
                PorterDuff.Mode.SRC_IN)
            holder.name.setText(categories[position].getStringResource())

            holder.itemView.setOnClickListener {
                fragmentView.categoty_btn.setImageResource(categories[position].getIconResource())
                fragmentView.categoty_btn.setColorFilter(ContextCompat.getColor(context!!,
                    android.R.color.white),
                    PorterDuff.Mode.SRC_IN)
                expand(fragmentView.rv_container, 0)
                presenter.setPurpose(categories[position])

                lastPressedChooser = ChooseButton.NONE
            }
        }

        inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val icon: ImageView = itemView.findViewById(R.id.designation)
            val name: TextView = itemView.findViewById(R.id.name)
        }
    }

    override fun showRepeatTransactionDialog() {
        MaterialDialog.Builder(activity!!)
                .title(R.string.dialog_repeat_title)
                .items(R.array.dialog_repeat_options)
                .itemsCallback{d,v,w,t -> presenter.setRepeat(w)}
                .show()
    }

    private fun datePicker() {
        val myCallBack = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            val cal: Calendar = Calendar.getInstance()
            cal.set(year, month, dayOfMonth)
            presenter.model.date = cal.time
        }

        val cal = Calendar.getInstance()
        val tpd = DatePickerDialog(context,
            myCallBack,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH))

        tpd.show()
    }

}
