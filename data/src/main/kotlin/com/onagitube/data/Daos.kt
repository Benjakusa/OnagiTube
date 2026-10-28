package com.onagitube.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SlogDownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: SlogDownload): Long

    @Update
    suspend fun update(item: SlogDownload)

    @Delete
    suspend fun delete(item: SlogDownload)

    @Query("SELECT * FROM SlogDownload ORDER BY addTime DESC")
    fun observeAll(): Flow<List<SlogDownload>>

    @Query("SELECT * FROM SlogDownload WHERE id = :id")
    suspend fun findById(id: Int): SlogDownload?
}

@Dao
interface RichDownloadDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: RichDownload)

    @Update
    suspend fun update(item: RichDownload)

    @Delete
    suspend fun delete(item: RichDownload)

    @Query("SELECT * FROM RichDownload ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<RichDownload>>

    @Query("SELECT * FROM RichDownload WHERE id = :id")
    suspend fun findById(id: String): RichDownload?

    @Query("SELECT * FROM RichDownload WHERE status = :status")
    fun findByStatus(status: String): Flow<List<RichDownload>>
}

@Dao
interface SegmentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<Segment>)

    @Query("SELECT * FROM Segment WHERE downloadId = :downloadId ORDER BY blockOffset ASC")
    suspend fun findByDownloadId(downloadId: String): List<Segment>

    @Query("DELETE FROM Segment WHERE downloadId = :downloadId")
    suspend fun deleteByDownloadId(downloadId: String)
}

@Dao
interface DownloadFailRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: DownloadFailRecord): Long

    @Query("SELECT * FROM DownloadFailRecord WHERE downloadId = :downloadId ORDER BY timestamp DESC")
    fun findByDownloadId(downloadId: String): Flow<List<DownloadFailRecord>>

    @Query("DELETE FROM DownloadFailRecord WHERE downloadId = :downloadId")
    suspend fun deleteByDownloadId(downloadId: String)
}

@Dao
interface PluginMetaDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: PluginMeta)

    @Delete
    suspend fun delete(item: PluginMeta)

    @Query("SELECT * FROM PluginMeta ORDER BY installTime DESC")
    fun observeAll(): Flow<List<PluginMeta>>

    @Query("SELECT * FROM PluginMeta WHERE id = :id")
    suspend fun findById(id: String): PluginMeta?
}
