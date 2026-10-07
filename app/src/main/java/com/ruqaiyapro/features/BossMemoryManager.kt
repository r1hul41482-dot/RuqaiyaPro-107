package com.ruqaiyapro.features
import android.content.Context
object BossMemoryManager {
    fun save(ctx: Context, key: String, value: String){ ctx.getSharedPreferences("boss_memory",0).edit().putString(key.lowercase(),value).apply() }
    fun get(ctx: Context, key: String): String? { return ctx.getSharedPreferences("boss_memory",0).getString(key.lowercase(),null) }
    fun getAll(ctx: Context): Map<String,String> { return ctx.getSharedPreferences("boss_memory",0).all.mapNotNull{ (it.value as? String)?.let{ v-> it.key to v } }.toMap() }
}
