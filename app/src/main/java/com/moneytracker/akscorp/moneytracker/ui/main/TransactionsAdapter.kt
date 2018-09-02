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
class TransactionsAdapter(private val mData: ArrayList<Transaction>,
                          private val mEventListener: TransactionsRecyclerEventListener) :
    RecyclerView.Adapter<TransactionsAdapter.TransactionHolder>() {

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int): TransactionHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.transaction_rv_item, parent, false)
        return TransactionHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionHolder, position: Int) {
        val transaction = mData[position]

        holder.sum.text = StringBuilder(transaction.moneyQuantity.normalizeCountString())
                .append(transaction.moneyQuantity.currency.currencySymbol)
        holder.icon.setImageResource(transaction.paymentPurpose.getIconResource())
        if (transaction.paymentDescription != "") {
            holder.description.visibility = View.VISIBLE
            holder.description.text = transaction.paymentDescription
        }

        holder.date.text = SimpleDateFormat("d MMM yyyy HH:mm", Locale.getDefault())
                .format(transaction.date)

        holder.itemView.setOnClickListener {
            mEventListener.onClick(position, transaction)
        }

        if (transaction.shouldRepeat) holder.repeatIcon.visibility = View.VISIBLE

    }

    fun replaceData(transactions: List<Transaction>) {
        mData.clear()
        mData.addAll(transactions)
        notifyDataSetChanged()
    }

    fun updateItem(transaction: Transaction) {
        for ((index, item) in mData.withIndex()) {
            if (item.id == transaction.id) {
                mData[index] = transaction
                notifyItemChanged(index)
                break
            }
        }
    }

    fun deleteItem(transaction: Transaction) {
        for ((index, item) in mData.withIndex()) {
            if (item.id == transaction.id) {
                mData.remove(transaction)
                notifyItemRemoved(index)
                break
            }
        }
    }

    inner class TransactionHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.icon
        val description: TextView = itemView.description
        val sum: TextView = itemView.sum
        val date: TextView = itemView.date
        val repeatIcon: ImageView = itemView.on_repeat_icon
    }

    interface TransactionsRecyclerEventListener {

        fun onClick(position: Int, transaction: Transaction)

    }

}