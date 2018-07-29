package com.moneytracker.akscorp.moneytracker.dialogs

import android.app.Activity
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.models.Currency
import kotlinx.android.synthetic.main.dialog_new_payment.view.*
import com.moneytracker.akscorp.moneytracker.models.Transaction
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import com.moneytracker.akscorp.moneytracker.*
import com.moneytracker.akscorp.moneytracker.models.Account


val PAYMENT_DIALOG_TAG = "PAYMENT_DIALOG_TAG"


interface IPaymentDialog
{
    fun showConfirmButton()
    fun hideConfirmButton()
    fun addTransaction()
}

class PaymentDialogPresenter(val view: IPaymentDialog, val account: Account)
{

}

class PaymentDialog : DialogFragment(), IPaymentDialog
{

    lateinit var presenter: PaymentDialogPresenter

    lateinit var fragmentView: View

    override fun showConfirmButton()
    {
        expand(fragmentView.confirm_btn, dpToPixel(context!!, 55F))
    }

    override fun hideConfirmButton()
    {
        expand(fragmentView.confirm_btn, 0)
    }

    override fun addTransaction()
    {

        hideKeyboard()

        fadeDown(fragmentView.appBar)
        fadeDown(fragmentView.transaction_description)
        fadeIn(fragmentView.done, endAction = {
            Handler().postDelayed({

                fadeDown(this@PaymentDialog.view!!,
                    endAction = { this@PaymentDialog.dismiss() })
            }, 500)
        })
    }

    fun hideKeyboard()
    {
        activity.let {
        (activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
        activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialog)

        if (arguments != null)
        {
            if (arguments!!.containsKey("account"))
            {
                val account = arguments!!.getParcelable("account") as Account
                presenter = PaymentDialogPresenter(this, account)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        fragmentView = inflater.inflate(R.layout.dialog_new_payment, null)

        initUI()

        return fragmentView
    }

    private fun toolbarInit()
    {
        fragmentView.toolbar.title = presenter.account.name
        fragmentView.toolbar.setNavigationIcon(R.drawable.ic_arrow_white)
        fragmentView.toolbar.setNavigationOnClickListener {
            this@PaymentDialog.dismiss()
        }
    }

    enum class ChooseButton
    {
        CATEGORY,
        CURRENCY,
        DATE,
        NONE;
    }

    var lastPressedChooser = ChooseButton.NONE

    private fun initUI()
    {
        toolbarInit()

        fragmentView.apply {
            sum_et.addTextChangedListener(SumTextWatcher())

            categoty_btn.setOnClickListener {
                if (lastPressedChooser == ChooseButton.CATEGORY)
                {
                    expand(rv_container, 0)
                    lastPressedChooser = ChooseButton.NONE
                    return@setOnClickListener
                }

                lastPressedChooser = ChooseButton.CATEGORY

                initCategories(fragmentView)
                choose_rv.measure(0, 0)
                expand(rv_container, choose_rv.measuredHeight)
            }
            currency_btn.setOnClickListener {
                if (lastPressedChooser == ChooseButton.CURRENCY)
                {
                    expand(rv_container, 0)
                    lastPressedChooser = ChooseButton.NONE
                    return@setOnClickListener
                }

                lastPressedChooser = ChooseButton.CURRENCY

                initCurrencies(fragmentView)
                choose_rv.measure(0, 0)
                expand(rv_container, choose_rv.measuredHeight)
            }

            date_btn.setOnClickListener {
                if (lastPressedChooser == ChooseButton.DATE)
                {
                    lastPressedChooser = ChooseButton.NONE
                    return@setOnClickListener
                }

                lastPressedChooser = ChooseButton.DATE
            }

            confirm_btn.setOnClickListener {
               addTransaction()
            }
        }
    }

    fun initCurrencies(view: View)
    {
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        view.choose_rv.setHasFixedSize(true)
        view.choose_rv.layoutManager = layoutManager
        view.choose_rv.isNestedScrollingEnabled = true
        view.choose_rv.adapter = ChooseCurrencyAdapter(mutableListOf(Currency.USD,
            Currency.GBP,
            Currency.EUR,
            Currency.RUR))

    }

    fun initCategories(view: View)
    {
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        view.choose_rv.setHasFixedSize(true)
        view.choose_rv.layoutManager = layoutManager
        view.choose_rv.isNestedScrollingEnabled = true

        view.choose_rv.adapter =
                ChooseCategoryAdapter(mutableListOf(Transaction.PaymentPurpose.AUTO,
                    Transaction.PaymentPurpose.AUTO,
                    Transaction.PaymentPurpose.AUTO,
                    Transaction.PaymentPurpose.AUTO,
                    Transaction.PaymentPurpose.AUTO,
                    Transaction.PaymentPurpose.FOOD))

    }

    inner class SumTextWatcher : TextWatcher
    {
        override fun afterTextChanged(s: Editable?)
        {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        {
            if (s!!.toString().isNotEmpty())
            {
                showConfirmButton()
            }
            else
            {
                hideConfirmButton()
            }
        }

    }

    inner class ChooseCurrencyAdapter(val currencies: List<Currency>) :
        RecyclerView.Adapter<ChooseCurrencyAdapter.CurrencyHolder>()
    {
        override fun getItemId(position: Int): Long
        {
            return position.toLong()
        }

        override fun getItemCount(): Int
        {
            return currencies.size
        }

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): CurrencyHolder
        {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.currency_rv_item, parent, false)

            return CurrencyHolder(view)
        }

        override fun onBindViewHolder(holder: CurrencyHolder, position: Int)
        {
            holder.designation.text = currencies[position].currencySymbol
            holder.name.text = currencies[position].toString()

            holder.itemView.setOnClickListener {
                view!!.currency_btn.text = currencies[position].currencySymbol
                expand(view!!.rv_container, 0)
            }
        }

        inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            val designation: TextView = itemView.findViewById(R.id.designation)
            val name: TextView = itemView.findViewById(R.id.name)
        }

    }

    inner class ChooseCategoryAdapter(val categories: List<Transaction.PaymentPurpose>) :
        RecyclerView.Adapter<ChooseCategoryAdapter.CurrencyHolder>()
    {

        override fun getItemId(position: Int): Long
        {
            return position.toLong()
        }

        override fun getItemCount(): Int
        {
            return categories.size
        }

        override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int): CurrencyHolder
        {
            val context = parent.context
            val inflater = LayoutInflater.from(context)
            val view = inflater.inflate(R.layout.category_rv_item, parent, false)

            return CurrencyHolder(view)
        }

        override fun onBindViewHolder(holder: CurrencyHolder, position: Int)
        {
            holder.icon.setImageResource(categories[position].getIconResource())
            holder.icon.setColorFilter(holder.itemView.context.resources.getColor(android.R.color.white),
                PorterDuff.Mode.SRC_IN)
            holder.name.setText(categories[position].getStringResource())

            holder.itemView.setOnClickListener {
                view!!.categoty_btn.setImageResource(categories[position].getIconResource())
                view!!.categoty_btn.setColorFilter(holder.itemView.context.resources.getColor(
                    android.R.color.white),
                    PorterDuff.Mode.SRC_IN)
                expand(view!!.rv_container, 0)
            }
        }

        inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            val icon: ImageView = itemView.findViewById(R.id.designation)
            val name: TextView = itemView.findViewById(R.id.name)
        }

    }
}