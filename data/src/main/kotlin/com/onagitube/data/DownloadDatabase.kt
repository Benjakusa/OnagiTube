package com.onagitube.data

import android.content.Context
import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Database(
    entities = [
        SlogDownload::class,
        RichDownload::class,
        Segment::class,
        DownloadFailRecord::class,
        PluginMeta::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class DownloadDatabase : RoomDatabase() {

    abstract fun slogDownloadDao(): SlogDownloadDao
    abstract fun richDownloadDao(): RichDownloadDao
    abstract fun segmentDao(): SegmentDao
    abstract fun failRecordDao(): DownloadFailRecordDao
    abstract fun pluginMetaDao(): PluginMetaDao

    companion object {
        @Volatile
        private var INSTANCE: DownloadDatabase? = null

        fun getInstance(context: Context): DownloadDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    DownloadDatabase::class.java,
                    "onagiTube-db",
                ).build().also { INSTANCE = it }
            }
        }
    }
}

@Entity
data class SlogDownload(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String?,
    val source: String?,
    val file: String?,
    val downloadUrl: String?,
    val addTime: Long = System.currentTimeMillis(),
)

@Serializable
@Entity
data class RichDownload(
    @PrimaryKey val id: String,
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
    val completedAt: Long? = null,
)

@Entity
data class Segment(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val downloadId: String,
    val blockOffset: Long,
    val blockLength: Int,
    val downloadedBytes: Int,
    val timestamp: Long = System.currentTimeMillis(),
)

@Serializable
@Entity
data class DownloadFailRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val downloadId: String,
    val errorCode: String,
    val timestamp: Long = System.currentTimeMillis(),
    val retryCount: Int = 0,
)

@Serializable
@Entity
data class PluginMeta(
    @PrimaryKey val id: String,
    val version: String,
    val manifestJson: String,
    val signature: String?,
    val installTime: Long = System.currentTimeMillis(),
)
