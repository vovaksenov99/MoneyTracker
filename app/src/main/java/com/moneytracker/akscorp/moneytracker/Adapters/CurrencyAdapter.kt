package com.moneytracker.akscorp.moneytracker.Adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.Models.Money
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.dpToPixel
import com.moneytracker.akscorp.moneytracker.roundToDigit
import com.moneytracker.akscorp.moneytracker.spToPixel
import kotlinx.android.synthetic.main.item_money_balance.view.*
import java.text.NumberFormat

/**
 * Created by AksCorp on 03.04.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */
class CurrencyAdapter(private val context: Context, val currencies: List<Money>) :
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

        view.amountTextView.textSize = spToPixel(context, 14f).toFloat()
        view.currencyTextView.textSize = spToPixel(context, 9f).toFloat()

        val padding = dpToPixel(context, 16f)
        view.setPadding(padding, padding, padding, padding)

        return CurrencyHolder(view)
    }

    override fun onBindViewHolder(holder: CurrencyAdapter.CurrencyHolder, position: Int)
    {
        val format = NumberFormat.getNumberInstance()
        holder.amountTextView.text = format.format(roundToDigit(currencies[position].count, 2))
        holder.currencyTextView.text = currencies[position].currency.toString()
    }

    inner class CurrencyHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val currencyTextView: TextView = itemView.currencyTextView as TextView
        val amountTextView: TextView = itemView.amountTextView as TextView
    }

}