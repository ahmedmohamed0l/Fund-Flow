package com.axoncodelabs.fundflow.data.util.backup

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Suppress("DEPRECATION")
@Singleton
class BackupFileManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        private const val BACKUP_DIR = "data_backup"
        private const val BACKUP_FILE_PREFIX = "backup_"
        private const val BACKUP_FILE_EXT = ".json"
        private val FILE_DATE_FORMAT = SimpleDateFormat("yyMMddHHmm", Locale.US)
    }

    fun getBackupDirectory(): File? {
        val mediaDirs = context.externalMediaDirs
        val mediaDir = mediaDirs.firstOrNull { dir ->
            dir != null &&
                    dir.absolutePath.endsWith("Android/media/${context.packageName}")
        } ?: return null
        val backupDir = File(mediaDir, BACKUP_DIR)
        if (!backupDir.exists()) backupDir.mkdirs()
        return backupDir
    }

    suspend fun saveBackupFile(
        content: String,
        timestamp: Long,
        isAuto: Boolean
    ): String? =
        withContext(Dispatchers.IO) {

            val backupDir = getBackupDirectory() ?: return@withContext null

            val suffix = if (isAuto) "_a" else "_m"
            val fileName =
                BACKUP_FILE_PREFIX + FILE_DATE_FORMAT.format(Date(timestamp)) + suffix + BACKUP_FILE_EXT

            val file = File(backupDir, fileName)

            return@withContext try {
                file.outputStream().bufferedWriter().use {
                    it.write(content)
                }
                fileName
            } catch (e: Exception) {
                null
            }
        }

    suspend fun readBackupFile(fileName: String): String? =
        withContext(Dispatchers.IO) {

            val backupDir = getBackupDirectory() ?: return@withContext null
            val file = File(backupDir, fileName)

            if (!file.exists()) return@withContext null

            return@withContext try {
                file.inputStream().bufferedReader().use {
                    it.readText()
                }
            } catch (e: Exception) {
                null
            }
        }

    suspend fun deleteAutoBackupsForDate(dateMillis: Long): Boolean =
        withContext(Dispatchers.IO) {

            val backupDir = getBackupDirectory() ?: return@withContext false
            val datePrefix = FILE_DATE_FORMAT.format(Date(dateMillis)).take(6) // "yyMMdd"

            val autoFile = backupDir.listFiles()?.firstOrNull { file ->
                file.isFile &&
                        file.name.startsWith(BACKUP_FILE_PREFIX) &&
                        file.name.endsWith("_a$BACKUP_FILE_EXT") &&
                        file.name.removePrefix(BACKUP_FILE_PREFIX).take(6) == datePrefix
            }
            if (autoFile == null) return@withContext false

            return@withContext autoFile.delete()
        }

    suspend fun deleteBackupFile(fileName: String): Boolean =
        withContext(Dispatchers.IO) {

            val backupDir = getBackupDirectory() ?: return@withContext false
            val file = File(backupDir, fileName)

            if (!file.exists()) return@withContext false

            return@withContext file.delete()
        }

    suspend fun listBackupFiles(): List<File> =
        withContext(Dispatchers.IO) {

            val backupDir = getBackupDirectory() ?: return@withContext emptyList()

            return@withContext backupDir.listFiles { file ->
                file.isFile &&
                        file.name.startsWith(BACKUP_FILE_PREFIX) &&
                        file.name.endsWith(BACKUP_FILE_EXT)
            }?.sortedByDescending { it.lastModified() } ?: emptyList()
        }
}