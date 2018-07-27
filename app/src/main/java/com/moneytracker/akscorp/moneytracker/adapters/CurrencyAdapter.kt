package com.moneytracker.akscorp.moneytracker.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.models.Money
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.roundToDigit
import kotlinx.android.synthetic.main.item_money_balance.view.*
import org.jetbrains.anko.dimen
import java.text.NumberFormat

/**
 * Created by AksCorp on 03.04.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */
class CurrencyAdapter(val currencies: List<Money>) :
    RecyclerView.Adapter<CurrencyAdapter.CurrencyHolder>()
{

    override fun getItemCount(): Int
    {
        return currencies.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): CurrencyAdapter.CurrencyHolder
    {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item_money_balance, parent, false)

        view.amountTextView.textSize = context.dimen(R.dimen.amount_text_size).toFloat()
        view.currencyTextView.textSize = context.dimen(R.dimen.currency_text_size).toFloat()

        val padding = context.dimen(R.dimen.currencies_padding)
        view.setPadding(padding, padding, padding, padding)

        return CurrencyHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyAdapter.CurrencyHolder, position: Int)
    {
        holder.amountTextView.text = currencies[position].normalizeCountString()
        holder.currencyTextView.text = currencies[position].currency.toString()
    }

    inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val currencyTextView: TextView = itemView.currencyTextView as TextView
        val amountTextView: TextView = itemView.amountTextView as TextView
    }

}