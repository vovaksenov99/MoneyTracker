package com.moneytracker.akscorp.moneytracker.dialogs

import android.app.DatePickerDialog
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.dialog_new_payment.view.*
import com.moneytracker.akscorp.moneytracker.*
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.*
import com.moneytracker.akscorp.moneytracker.models.*
import com.moneytracker.akscorp.moneytracker.models.Currency
import com.moneytracker.akscorp.moneytracker.utilites.dpToPixel
import com.moneytracker.akscorp.moneytracker.utilites.expand
import com.moneytracker.akscorp.moneytracker.utilites.fadeDown
import com.moneytracker.akscorp.moneytracker.utilites.fadeIn
import java.util.*
import android.view.inputmethod.EditorInfo
import android.view.KeyEvent.KEYCODE_ENTER
import android.widget.TextView.OnEditorActionListener




val PAYMENT_DIALOG_TAG = "PAYMENT_DIALOG_TAG"


interface IPaymentDialog
{
    fun showConfirmButton()
    fun hideConfirmButton()
    fun addTransaction()
}

class PaymentDialogPresenter(val view: IPaymentDialog, val account: Account)
{
    lateinit var model: MyViewModel

    init
    {
    }

    fun setModel(fragmentActivity: Fragment)
    {
        model = ViewModelProviders.of(fragmentActivity).get(MyViewModel::class.java)
    }

    fun setSum(sum: Double)
    {
        model.transaction.moneyQuantity.count = sum
    }

    fun setCurrency(currency: Currency)
    {
        model.transaction.moneyQuantity.currency = currency
    }

    fun setPurpose(purpose: Transaction.PaymentPurpose)
    {
        model.transaction.paymentPurpose = purpose
    }

    fun setDescription(description: String)
    {
        model.transaction.paymentDescription = description
    }

    fun setDate(date: Calendar)
    {
        model.transaction.date = date
    }

    fun addTransaction()
    {

    }
}

class MyViewModel : ViewModel()
{
    var transaction: Transaction = Transaction()
}

class PaymentDialog : DialogFragment(), IPaymentDialog
{


    lateinit var presenter: PaymentDialogPresenter

    lateinit var fragmentView: View

    override fun showConfirmButton()
    {
        expand(fragmentView.confirm_btn,
            dpToPixel(context!!, 55F))
    }

    override fun hideConfirmButton()
    {
        expand(fragmentView.confirm_btn, 0)
    }

    override fun addTransaction()
    {
        presenter.addTransaction()
        finishLayoutAnim()
    }

    fun finishLayoutAnim()
    {
        hideKeyboard()

        fadeDown(fragmentView.appBar)
        fadeDown(fragmentView.transaction_description_et)

        fadeIn(fragmentView.done, endAction = {
            fadeDown(this@PaymentDialog.fragmentView,
                endAction = {
                    try
                    {
                        this@PaymentDialog.dismiss()
                    } catch (e: Exception)
                    {
                        Log.e(::PaymentDialog.name, e.toString())
                    }
                }, startDelay = 1000)
        })
    }

    private fun hideKeyboard()
    {
        activity?.let {

            try
            {
                it.currentFocus.clearFocus()
                activity!!.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)

                activity!!.window.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
            } catch (e: Exception)
            {
                Log.e(::PaymentDialog.name, e.toString())
            }

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
        presenter.setModel(this)
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
            if (presenter.model.transaction.moneyQuantity.count != 0.0)
                sum_et.setText(presenter.model.transaction.moneyQuantity.count.toString())
            transaction_description_et.setText(presenter.model.transaction.paymentDescription)
            categoty_btn.setImageResource(presenter.model.transaction.paymentPurpose.getIconResource())
            categoty_btn.setColorFilter(ContextCompat.getColor(context, android.R.color.white),
                PorterDuff.Mode.SRC_IN)
            currency_btn.setText(presenter.model.transaction.moneyQuantity.currency.currencySymbol)

            sum_et.addTextChangedListener(SumTextWatcher())


            transaction_description_et.addTextChangedListener(DescriptionTextWatcher())

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
                expand(rv_container,
                    choose_rv.measuredHeight)
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
                expand(rv_container,
                    choose_rv.measuredHeight)
            }

            date_btn.setOnClickListener {
                if (lastPressedChooser == ChooseButton.DATE)
                {
                    lastPressedChooser = ChooseButton.NONE
                    return@setOnClickListener
                }
                datePicker()
                lastPressedChooser = ChooseButton.DATE
            }

            confirm_btn.setOnClickListener {
                sum_et.isEnabled = false
                transaction_description_et.isEnabled = false

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
        view.choose_rv.adapter = ChooseCurrencyAdapter(Currency.values().toMutableList())

    }

    fun initCategories(view: View)
    {
        val layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        view.choose_rv.setHasFixedSize(true)
        view.choose_rv.layoutManager = layoutManager
        view.choose_rv.isNestedScrollingEnabled = true

        view.choose_rv.adapter =
                ChooseCategoryAdapter(Transaction.PaymentPurpose.values().toMutableList())

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
            if (s!!.toString().isNotEmpty() && s.toString().toDoubleOrNull() != null)
            {
                showConfirmButton()
                presenter.setSum(s.toString().toDouble())
            }
            else
            {
                hideConfirmButton()
            }
        }
    }

    inner class DescriptionTextWatcher : TextWatcher
    {
        override fun afterTextChanged(s: Editable?)
        {

        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int)
        {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int)
        {
            presenter.setDescription(s.toString())
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
                fragmentView.currency_btn.text = currencies[position].currencySymbol
                expand(fragmentView.rv_container, 0)
                presenter.setCurrency(currencies[position])
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
                fragmentView.categoty_btn.setImageResource(categories[position].getIconResource())
                fragmentView.categoty_btn.setColorFilter(ContextCompat.getColor(context!!,
                    android.R.color.white),
                    PorterDuff.Mode.SRC_IN)
                expand(fragmentView.rv_container, 0)
                presenter.setPurpose(categories[position])
            }
        }

        inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
        {
            val icon: ImageView = itemView.findViewById(R.id.designation)
            val name: TextView = itemView.findViewById(R.id.name)
        }
    }


    private fun datePicker()
    {
        val myCallBack = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            presenter.model.transaction.date.set(year, month, dayOfMonth)
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