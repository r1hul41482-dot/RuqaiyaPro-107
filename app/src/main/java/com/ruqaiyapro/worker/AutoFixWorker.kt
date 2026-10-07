package com.ruqaiyapro.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ruqaiyapro.code.CodeFixerEngine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AutoFixWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val logs = CodeFixerEngine.analyzeLogcat(applicationContext)
            android.util.Log.d("AutoFixWorker", "Periodic WorkManager check finished. Analyzed " + logs.size + " log entries.")
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.retry()
        }
    }
}
