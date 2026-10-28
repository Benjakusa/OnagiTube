package com.onagitube.data;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class PluginMetaDao_Impl implements PluginMetaDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<PluginMeta> __insertionAdapterOfPluginMeta;

  private final EntityDeletionOrUpdateAdapter<PluginMeta> __deletionAdapterOfPluginMeta;

  public PluginMetaDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfPluginMeta = new EntityInsertionAdapter<PluginMeta>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `PluginMeta` (`id`,`version`,`manifestJson`,`signature`,`installTime`) VALUES (?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PluginMeta value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getVersion() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getVersion());
        }
        if (value.getManifestJson() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getManifestJson());
        }
        if (value.getSignature() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getSignature());
        }
        stmt.bindLong(5, value.getInstallTime());
      }
    };
    this.__deletionAdapterOfPluginMeta = new EntityDeletionOrUpdateAdapter<PluginMeta>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `PluginMeta` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, PluginMeta value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
      }
    };
  }

  @Override
  public Object insert(final PluginMeta item, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPluginMeta.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final PluginMeta item, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfPluginMeta.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<PluginMeta>> observeAll() {
    final String _sql = "SELECT * FROM PluginMeta ORDER BY installTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"PluginMeta"}, new Callable<List<PluginMeta>>() {
      @Override
      public List<PluginMeta> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final int _cursorIndexOfManifestJson = CursorUtil.getColumnIndexOrThrow(_cursor, "manifestJson");
          final int _cursorIndexOfSignature = CursorUtil.getColumnIndexOrThrow(_cursor, "signature");
          final int _cursorIndexOfInstallTime = CursorUtil.getColumnIndexOrThrow(_cursor, "installTime");
          final List<PluginMeta> _result = new ArrayList<PluginMeta>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final PluginMeta _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpVersion;
            if (_cursor.isNull(_cursorIndexOfVersion)) {
              _tmpVersion = null;
            } else {
              _tmpVersion = _cursor.getString(_cursorIndexOfVersion);
            }
            final String _tmpManifestJson;
            if (_cursor.isNull(_cursorIndexOfManifestJson)) {
              _tmpManifestJson = null;
            } else {
              _tmpManifestJson = _cursor.getString(_cursorIndexOfManifestJson);
            }
            final String _tmpSignature;
            if (_cursor.isNull(_cursorIndexOfSignature)) {
              _tmpSignature = null;
            } else {
              _tmpSignature = _cursor.getString(_cursorIndexOfSignature);
            }
            final long _tmpInstallTime;
            _tmpInstallTime = _cursor.getLong(_cursorIndexOfInstallTime);
            _item = new PluginMeta(_tmpId,_tmpVersion,_tmpManifestJson,_tmpSignature,_tmpInstallTime);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object findById(final String id, final Continuation<? super PluginMeta> continuation) {
    final String _sql = "SELECT * FROM PluginMeta WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<PluginMeta>() {
      @Override
      public PluginMeta call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfVersion = CursorUtil.getColumnIndexOrThrow(_cursor, "version");
          final int _cursorIndexOfManifestJson = CursorUtil.getColumnIndexOrThrow(_cursor, "manifestJson");
          final int _cursorIndexOfSignature = CursorUtil.getColumnIndexOrThrow(_cursor, "signature");
          final int _cursorIndexOfInstallTime = CursorUtil.getColumnIndexOrThrow(_cursor, "installTime");
          final PluginMeta _result;
          if(_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpVersion;
            if (_cursor.isNull(_cursorIndexOfVersion)) {
              _tmpVersion = null;
            } else {
              _tmpVersion = _cursor.getString(_cursorIndexOfVersion);
            }
            final String _tmpManifestJson;
            if (_cursor.isNull(_cursorIndexOfManifestJson)) {
              _tmpManifestJson = null;
            } else {
              _tmpManifestJson = _cursor.getString(_cursorIndexOfManifestJson);
            }
            final String _tmpSignature;
            if (_cursor.isNull(_cursorIndexOfSignature)) {
              _tmpSignature = null;
            } else {
              _tmpSignature = _cursor.getString(_cursorIndexOfSignature);
            }
            final long _tmpInstallTime;
            _tmpInstallTime = _cursor.getLong(_cursorIndexOfInstallTime);
            _result = new PluginMeta(_tmpId,_tmpVersion,_tmpManifestJson,_tmpSignature,_tmpInstallTime);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, continuation);
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
