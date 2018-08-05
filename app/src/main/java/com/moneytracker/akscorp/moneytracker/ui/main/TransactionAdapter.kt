package com.moneytracker.akscorp.moneytracker.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction
import kotlinx.android.synthetic.main.transaction_rv_item.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by AksCorp on 03.04.2018.
 * akscorp2014@gmail.com
 * web site aksenov-vladimir.herokuapp.com
 */
class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionHolder>() {

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): TransactionHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.transaction_rv_item, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = transactions[position]

        holder.sum.text = transaction.moneyQuantity.normalizeCountString()
        holder.currency.text = transaction.moneyQuantity.currency.toString()
        holder.icon.setImageResource(transaction.paymentPurpose.getIconResource())
        holder.description.text = transaction.paymentDescription

        holder.date.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(transaction.date)
    }

    inner class TransactionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.icon
        val description: TextView = itemView.description
        val sum: TextView = itemView.sum
        val currency: TextView = itemView.currency
        val date: TextView = itemView.date
    }

}