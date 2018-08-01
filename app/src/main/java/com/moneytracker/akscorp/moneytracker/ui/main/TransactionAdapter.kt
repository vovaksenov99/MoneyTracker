package com.moneytracker.akscorp.moneytracker.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.DeprecetadTransaction
import kotlinx.android.synthetic.main.transaction_rv_item.view.*

/**
 * Created by AksCorp on 03.04.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */
class TransactionAdapter(private val deprecetadTransaction: List<DeprecetadTransaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    override fun getItemCount(): Int {
        return deprecetadTransaction.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): TransactionHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.transaction_rv_item, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = deprecetadTransaction[position]

        holder.sum.text = transaction.normalizeTransactionSum()
        holder.currency.text = transaction.moneyQuantity.currency.toString()
        holder.icon.setImageResource(transaction.paymentPurpose.getIconResource())
        holder.description.text = transaction.paymentDescription
    }

    inner class TransactionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.icon
        val description: TextView = itemView.description
        val sum: TextView = itemView.sum
        val currency: TextView = itemView.currency
    }

}