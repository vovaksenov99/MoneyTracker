package com.moneytracker.akscorp.moneytracker.model.persistance.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.moneytracker.akscorp.moneytracker.model.entities.Transaction

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getAll(): List<Transaction>

    @Query("SELECT * FROM transactions WHERE shouldRepeat=1")
    fun getTransactionWithRepeatTrue(): List<Transaction>

    @Query("SELECT * FROM transactions WHERE id=:id")
    fun findById(id: Long): Transaction

    @Insert(onConflict = REPLACE)
    fun insert(transaction: Transaction): Long

    @Update(onConflict = REPLACE)
    fun update(transaction: Transaction)

    @Delete
    fun delete(vararg transaction: Transaction)

    @Query("DELETE FROM accounts")
    fun deleteAll()

    @Query("SELECT * FROM transactions WHERE accountId=:accountId")
    fun getTransactionsForAccount(accountId: Long?): List<Transaction>

}
