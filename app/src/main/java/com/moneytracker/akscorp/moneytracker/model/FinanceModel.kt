package com.moneytracker.akscorp.moneytracker.model

import com.moneytracker.akscorp.moneytracker.R
import com.moneytracker.akscorp.moneytracker.model.entities.Money
import java.util.*

@Deprecated("This class is going to be replaced after adding ROOM impl")
data class DeprecetadTransaction(var paymentPurpose: PaymentPurpose = PaymentPurpose.OTHER,
                                 var paymentDescription: String = "",
                                 var moneyQuantity: Money = Money(0.0, defaultCurrency),
                                 var date: Calendar = Calendar.getInstance()) {

    @Deprecated("This enum class is going to be replaced after adding ROOM impl")
    enum class PaymentPurpose {
        AUTO {
            override fun getStringResource(): Int {
                return R.string.transport
            }

            override fun getIconResource(): Int {
                return R.drawable.ic_auto
            }

        },
        FOOD {
            override fun getStringResource(): Int {
                return R.string.food
            }

            override fun getIconResource(): Int {
                return R.drawable.ic_food
            }
        },
        OTHER {
            override fun getStringResource(): Int {
                return R.string.other
            }

            override fun getIconResource(): Int {
                return R.drawable.ic_category
            }
        };

        abstract fun getIconResource(): Int
        abstract fun getStringResource(): Int
    }
}


/**
 * Sum of all deprecetadTransactions
 *
 * @param deprecetadTransactions - list with deprecetadTransactions to sum
 * @param resultCurrency - result balance currency
 *
 * @return balance money quantity
 *//*

@Deprecated("This method is not going to be implemented after ROOM impl adding")
fun getAccountBalance(deprecetadTransactions: List<DeprecetadTransaction>,
                      resultCurrency: Currency = Currency.USD): Money {
    val currencyConverter = CurrencyConverter()

    val balance = Money(0.0, defaultCurrency)

    for (transaction in deprecetadTransactions) {
        balance.count += currencyConverter.toDefaultCurrency(transaction.moneyQuantity).count
    }

    return currencyConverter.convertCurrency(balance, resultCurrency)
}
*/
