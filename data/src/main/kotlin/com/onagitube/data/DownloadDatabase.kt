package com.onagitube.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.TypeConverter
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportDatabase
import kotlinx.serialization.SerializationConstructor
import kotlinx.serialization.json.Json

@Database(entities = [
    SlogDownload::class,
    RichDownload::class,
    Segment::class,
    DownloadFailRecord::class,
    PluginMeta::class,
], version = 1, exportSchema = false)
abstract class DownloadDatabase : RoomDatabase() {

    abstract fun slogDownloadDao(): SlogDownloadDao
    abstract fun richDownloadDao(): RichDownloadDao
    abstract fun segmentDao(): SegmentDao
    abstract fun failRecordDao(): DownloadFailRecordDao
    abstract fun pluginMetaDao(): PluginMetaDao

    @TypeConverter
    fun stringToJson(value: String?): Any? {
        return value?.let { Json.decodeFromString<any?>(it) }
    }

    @TypeConverter
    fun jsonToString(value: Any?): String? {
        return value?.let { Json.encodeToString(it) }
    }

    companion object {
        @Volatile
        private var INSTANCE: DownloadDatabase? = null

        fun getInstance(context: Context): DownloadDatabase {
            return INSTANCE ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    DownloadDatabase::class.java,
                    "onagiTube-db"
                )
                    .build()
                db?.also { INSTANCE = it }
                return db
            }
        }
    }
}

data class SlogDownload(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String?,
    val source: String?,
    val file: String?,
    val downloadUrl: String?,
    val addTime: Long = System.currentTimeMillis()
)

@Serializable
data class RichDownload(
    val id: String,
    val key: String,
    val url: String,
    val referrer: String?,
    val title: String?,
    val coverUrl: String?,
    val durationMs: Long?,
    val mime: String?,
    val ext: String?,
    val quality: String?,
    val totalBytes: Long,
    val downloadedBytes: Long,
    val status: String,
    val pauseReason: String?,
    val errorCode: String?,
    val errorMessage: String?,
    val filePath: String?,
    val source: String?,
    val verifyType: String?,
    val digest: String?,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null
)

data class Segment(
    val id: Long = 0L,
    val downloadId: String,
    val blockOffset: Long,
    val blockLength: Int,
    val downloadedBytes: Int,
    val timestamp: Long = System.currentTimeMillis()
)

@Serializable
data class DownloadFailRecord(
    val id: Long = 0L,
    val downloadId: String,
    val errorCode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val retryCount: Int = 0
)

@Serializable
data class PluginMeta(
    val id: String,
    val version: String,
    val manifestJson: String,
    val signature: String?,
    val installTime: Long = System.currentTimeMillis()
)