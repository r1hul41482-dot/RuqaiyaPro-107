package com.ruqaiyapro.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class KhataDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "ruqaiyapro_khata.db"
        const val DATABASE_VERSION = 1

        const val TABLE_DIARY = "diary"
        const val TABLE_MONEY = "money_hisab"
        const val TABLE_PASSWORDS = "passwords_vault"
        const val TABLE_LOVE = "love_khata"
        const val TABLE_DREAMS = "dreams"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE $TABLE_DIARY (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                date TEXT,
                entry TEXT,
                emotion TEXT,
                timestamp INTEGER
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_MONEY (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                amount REAL,
                type TEXT,
                category TEXT,
                timestamp INTEGER
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_PASSWORDS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                site_name TEXT,
                username TEXT,
                encrypted_password TEXT,
                timestamp INTEGER
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_LOVE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                memory TEXT,
                date TEXT,
                timestamp INTEGER
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE $TABLE_DREAMS (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT,
                story TEXT,
                mood TEXT,
                timestamp INTEGER
            )
        """.trimIndent())
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DIARY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MONEY")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PASSWORDS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_LOVE")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_DREAMS")
        onCreate(db)
    }

    fun insertDiary(date: String, entry: String, emotion: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("date", date)
            put("entry", entry)
            put("emotion", emotion)
            put("timestamp", System.currentTimeMillis())
        }
        return db.insert(TABLE_DIARY, null, values)
    }

    fun insertMoney(title: String, amount: Double, type: String, category: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("title", title)
            put("amount", amount)
            put("type", type)
            put("category", category)
            put("timestamp", System.currentTimeMillis())
        }
        return db.insert(TABLE_MONEY, null, values)
    }
}
