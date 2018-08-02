package com.moneytracker.akscorp.moneytracker.model.persistance.dao

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.moneytracker.akscorp.moneytracker.model.entities.Account

/**
 *  Created by Alexander Melnikov on 02.08.18.
 *  melnikov.ws@gmail.com
 *  https://github.com/millerovv
 */

@Dao
interface AccountDao {

    @Insert(onConflict = REPLACE)
    fun insert(account: Account)

    @Update
    fun update(vararg account: Account)

    @Delete
    fun delete(vararg account: Account)

    @Query("DELETE FROM accounts")
    fun deleteAll()

    @Query("SELECT * FROM accounts WHERE id=:id")
    fun findById(id: Int): Account

    @Query("SELECT * FROM accounts WHERE name=:name")
    fun findByName(name: String): Account

}