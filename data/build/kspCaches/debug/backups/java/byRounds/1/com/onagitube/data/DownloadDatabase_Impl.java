package com.onagitube.data;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class DownloadDatabase_Impl extends DownloadDatabase {
  private volatile SlogDownloadDao _slogDownloadDao;

  private volatile RichDownloadDao _richDownloadDao;

  private volatile SegmentDao _segmentDao;

  private volatile DownloadFailRecordDao _downloadFailRecordDao;

  private volatile PluginMetaDao _pluginMetaDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `SlogDownload` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `type` TEXT, `source` TEXT, `file` TEXT, `downloadUrl` TEXT, `addTime` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `RichDownload` (`id` TEXT NOT NULL, `key` TEXT NOT NULL, `url` TEXT NOT NULL, `referrer` TEXT, `title` TEXT, `coverUrl` TEXT, `durationMs` INTEGER, `mime` TEXT, `ext` TEXT, `quality` TEXT, `totalBytes` INTEGER NOT NULL, `downloadedBytes` INTEGER NOT NULL, `status` TEXT NOT NULL, `pauseReason` TEXT, `errorCode` TEXT, `errorMessage` TEXT, `filePath` TEXT, `source` TEXT, `verifyType` TEXT, `digest` TEXT, `createdAt` INTEGER NOT NULL, `updatedAt` INTEGER NOT NULL, `completedAt` INTEGER, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `Segment` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `downloadId` TEXT NOT NULL, `blockOffset` INTEGER NOT NULL, `blockLength` INTEGER NOT NULL, `downloadedBytes` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `DownloadFailRecord` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `downloadId` TEXT NOT NULL, `errorCode` TEXT NOT NULL, `timestamp` INTEGER NOT NULL, `retryCount` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `PluginMeta` (`id` TEXT NOT NULL, `version` TEXT NOT NULL, `manifestJson` TEXT NOT NULL, `signature` TEXT, `installTime` INTEGER NOT NULL, PRIMARY KEY(`id`))");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '2f0426b327773d1af1a56d86aedaacc4')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `SlogDownload`");
        _db.execSQL("DROP TABLE IF EXISTS `RichDownload`");
        _db.execSQL("DROP TABLE IF EXISTS `Segment`");
        _db.execSQL("DROP TABLE IF EXISTS `DownloadFailRecord`");
        _db.execSQL("DROP TABLE IF EXISTS `PluginMeta`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      public void onCreate(SupportSQLiteDatabase _db) {
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onCreate(_db);
          }
        }
      }

      @Override
      public void onOpen(SupportSQLiteDatabase _db) {
        mDatabase = _db;
        internalInitInvalidationTracker(_db);
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onOpen(_db);
          }
        }
      }

      @Override
      public void onPreMigrate(SupportSQLiteDatabase _db) {
        DBUtil.dropFtsSyncTriggers(_db);
      }

      @Override
      public void onPostMigrate(SupportSQLiteDatabase _db) {
      }

      @Override
      public RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsSlogDownload = new HashMap<String, TableInfo.Column>(6);
        _columnsSlogDownload.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlogDownload.put("type", new TableInfo.Column("type", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlogDownload.put("source", new TableInfo.Column("source", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlogDownload.put("file", new TableInfo.Column("file", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlogDownload.put("downloadUrl", new TableInfo.Column("downloadUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSlogDownload.put("addTime", new TableInfo.Column("addTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSlogDownload = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSlogDownload = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSlogDownload = new TableInfo("SlogDownload", _columnsSlogDownload, _foreignKeysSlogDownload, _indicesSlogDownload);
        final TableInfo _existingSlogDownload = TableInfo.read(_db, "SlogDownload");
        if (! _infoSlogDownload.equals(_existingSlogDownload)) {
          return new RoomOpenHelper.ValidationResult(false, "SlogDownload(com.onagitube.data.SlogDownload).\n"
                  + " Expected:\n" + _infoSlogDownload + "\n"
                  + " Found:\n" + _existingSlogDownload);
        }
        final HashMap<String, TableInfo.Column> _columnsRichDownload = new HashMap<String, TableInfo.Column>(23);
        _columnsRichDownload.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("key", new TableInfo.Column("key", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("url", new TableInfo.Column("url", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("referrer", new TableInfo.Column("referrer", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("title", new TableInfo.Column("title", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("coverUrl", new TableInfo.Column("coverUrl", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("durationMs", new TableInfo.Column("durationMs", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("mime", new TableInfo.Column("mime", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("ext", new TableInfo.Column("ext", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("quality", new TableInfo.Column("quality", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("totalBytes", new TableInfo.Column("totalBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("downloadedBytes", new TableInfo.Column("downloadedBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("status", new TableInfo.Column("status", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("pauseReason", new TableInfo.Column("pauseReason", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("errorCode", new TableInfo.Column("errorCode", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("errorMessage", new TableInfo.Column("errorMessage", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("filePath", new TableInfo.Column("filePath", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("source", new TableInfo.Column("source", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("verifyType", new TableInfo.Column("verifyType", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("digest", new TableInfo.Column("digest", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("createdAt", new TableInfo.Column("createdAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("updatedAt", new TableInfo.Column("updatedAt", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsRichDownload.put("completedAt", new TableInfo.Column("completedAt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysRichDownload = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesRichDownload = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoRichDownload = new TableInfo("RichDownload", _columnsRichDownload, _foreignKeysRichDownload, _indicesRichDownload);
        final TableInfo _existingRichDownload = TableInfo.read(_db, "RichDownload");
        if (! _infoRichDownload.equals(_existingRichDownload)) {
          return new RoomOpenHelper.ValidationResult(false, "RichDownload(com.onagitube.data.RichDownload).\n"
                  + " Expected:\n" + _infoRichDownload + "\n"
                  + " Found:\n" + _existingRichDownload);
        }
        final HashMap<String, TableInfo.Column> _columnsSegment = new HashMap<String, TableInfo.Column>(6);
        _columnsSegment.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSegment.put("downloadId", new TableInfo.Column("downloadId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSegment.put("blockOffset", new TableInfo.Column("blockOffset", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSegment.put("blockLength", new TableInfo.Column("blockLength", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSegment.put("downloadedBytes", new TableInfo.Column("downloadedBytes", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSegment.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSegment = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSegment = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSegment = new TableInfo("Segment", _columnsSegment, _foreignKeysSegment, _indicesSegment);
        final TableInfo _existingSegment = TableInfo.read(_db, "Segment");
        if (! _infoSegment.equals(_existingSegment)) {
          return new RoomOpenHelper.ValidationResult(false, "Segment(com.onagitube.data.Segment).\n"
                  + " Expected:\n" + _infoSegment + "\n"
                  + " Found:\n" + _existingSegment);
        }
        final HashMap<String, TableInfo.Column> _columnsDownloadFailRecord = new HashMap<String, TableInfo.Column>(5);
        _columnsDownloadFailRecord.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadFailRecord.put("downloadId", new TableInfo.Column("downloadId", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadFailRecord.put("errorCode", new TableInfo.Column("errorCode", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadFailRecord.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsDownloadFailRecord.put("retryCount", new TableInfo.Column("retryCount", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysDownloadFailRecord = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesDownloadFailRecord = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoDownloadFailRecord = new TableInfo("DownloadFailRecord", _columnsDownloadFailRecord, _foreignKeysDownloadFailRecord, _indicesDownloadFailRecord);
        final TableInfo _existingDownloadFailRecord = TableInfo.read(_db, "DownloadFailRecord");
        if (! _infoDownloadFailRecord.equals(_existingDownloadFailRecord)) {
          return new RoomOpenHelper.ValidationResult(false, "DownloadFailRecord(com.onagitube.data.DownloadFailRecord).\n"
                  + " Expected:\n" + _infoDownloadFailRecord + "\n"
                  + " Found:\n" + _existingDownloadFailRecord);
        }
        final HashMap<String, TableInfo.Column> _columnsPluginMeta = new HashMap<String, TableInfo.Column>(5);
        _columnsPluginMeta.put("id", new TableInfo.Column("id", "TEXT", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPluginMeta.put("version", new TableInfo.Column("version", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPluginMeta.put("manifestJson", new TableInfo.Column("manifestJson", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPluginMeta.put("signature", new TableInfo.Column("signature", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPluginMeta.put("installTime", new TableInfo.Column("installTime", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPluginMeta = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesPluginMeta = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoPluginMeta = new TableInfo("PluginMeta", _columnsPluginMeta, _foreignKeysPluginMeta, _indicesPluginMeta);
        final TableInfo _existingPluginMeta = TableInfo.read(_db, "PluginMeta");
        if (! _infoPluginMeta.equals(_existingPluginMeta)) {
          return new RoomOpenHelper.ValidationResult(false, "PluginMeta(com.onagitube.data.PluginMeta).\n"
                  + " Expected:\n" + _infoPluginMeta + "\n"
                  + " Found:\n" + _existingPluginMeta);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "2f0426b327773d1af1a56d86aedaacc4", "cea9582793d0bcdcc829d5285fb07d67");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(configuration.context)
        .name(configuration.name)
        .callback(_openCallback)
        .build();
    final SupportSQLiteOpenHelper _helper = configuration.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "SlogDownload","RichDownload","Segment","DownloadFailRecord","PluginMeta");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `SlogDownload`");
      _db.execSQL("DELETE FROM `RichDownload`");
      _db.execSQL("DELETE FROM `Segment`");
      _db.execSQL("DELETE FROM `DownloadFailRecord`");
      _db.execSQL("DELETE FROM `PluginMeta`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SlogDownloadDao.class, SlogDownloadDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(RichDownloadDao.class, RichDownloadDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SegmentDao.class, SegmentDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(DownloadFailRecordDao.class, DownloadFailRecordDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(PluginMetaDao.class, PluginMetaDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  public List<Migration> getAutoMigrations(
      @NonNull Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecsMap) {
    return Arrays.asList();
  }

  @Override
  public SlogDownloadDao slogDownloadDao() {
    if (_slogDownloadDao != null) {
      return _slogDownloadDao;
    } else {
      synchronized(this) {
        if(_slogDownloadDao == null) {
          _slogDownloadDao = new SlogDownloadDao_Impl(this);
        }
        return _slogDownloadDao;
      }
    }
  }

  @Override
  public RichDownloadDao richDownloadDao() {
    if (_richDownloadDao != null) {
      return _richDownloadDao;
    } else {
      synchronized(this) {
        if(_richDownloadDao == null) {
          _richDownloadDao = new RichDownloadDao_Impl(this);
        }
        return _richDownloadDao;
      }
    }
  }

  @Override
  public SegmentDao segmentDao() {
    if (_segmentDao != null) {
      return _segmentDao;
    } else {
      synchronized(this) {
        if(_segmentDao == null) {
          _segmentDao = new SegmentDao_Impl(this);
        }
        return _segmentDao;
      }
    }
  }

  @Override
  public DownloadFailRecordDao failRecordDao() {
    if (_downloadFailRecordDao != null) {
      return _downloadFailRecordDao;
    } else {
      synchronized(this) {
        if(_downloadFailRecordDao == null) {
          _downloadFailRecordDao = new DownloadFailRecordDao_Impl(this);
        }
        return _downloadFailRecordDao;
      }
    }
  }

  @Override
  public PluginMetaDao pluginMetaDao() {
    if (_pluginMetaDao != null) {
      return _pluginMetaDao;
    } else {
      synchronized(this) {
        if(_pluginMetaDao == null) {
          _pluginMetaDao = new PluginMetaDao_Impl(this);
        }
        return _pluginMetaDao;
      }
    }
  }
}
