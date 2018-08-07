package com.moneytracker.akscorp.moneytracker.ui.accounts

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.entities.Account
import kotlinx.android.synthetic.main.item_account.view.*

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */
class AccountsAdapter(private val mContext: Context,
                      private val mData: ArrayList<Account>,
                      private val mEventListener: AccountsRecyclerEventListener)
    : RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder>() {

    private val TAG = "debug"

    class AccountsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.tv_acc_name
        val balance: TextView = itemView.tv_balance
        val currency: TextView = itemView.tv_currency

        fun onBind(item: Account) {
            name.text = item.name
            balance.text = item.balance.normalizeCountString()
            currency.text = item.balance.currency.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountsViewHolder {
        val holder = AccountsViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_account,
                parent, false))

        holder.itemView.setOnClickListener {
            if (holder.adapterPosition != RecyclerView.NO_POSITION) {
                mEventListener.itemClick(holder.adapterPosition, mData[holder.adapterPosition])
            }
        }

        return holder
    }

    override fun onBindViewHolder(holder: AccountsViewHolder, position: Int) {
        holder.onBind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    override fun getItemId(position: Int): Long = mData[position].hashCode().toLong()

    fun replaceData(accounts: List<Account>) {
        mData.clear()
        mData.addAll(accounts)
        notifyDataSetChanged()
    }

    fun addAccountToEnd(account: Account) {
        mData.add(account)
        notifyDataSetChanged()
    }
    
    fun updateItem(account: Account) {
        for ((index, item) in mData.withIndex()) {
            if (item.id == account.id) {
                mData[index] = account
                notifyItemChanged(index)
                break
            }
        }
    }

    fun deleteItem(account: Account) {
        for ((index, item) in mData.withIndex()) {
            if (item.id == account.id) {
                mData.remove(account)
                notifyItemChanged(index)
                break
            }
        }
    }

    interface AccountsRecyclerEventListener {

        fun itemClick(position: Int, account: Account)

    }

}