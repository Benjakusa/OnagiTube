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
import java.lang.Long;
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
public final class SlogDownloadDao_Impl implements SlogDownloadDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SlogDownload> __insertionAdapterOfSlogDownload;

  private final EntityDeletionOrUpdateAdapter<SlogDownload> __deletionAdapterOfSlogDownload;

  private final EntityDeletionOrUpdateAdapter<SlogDownload> __updateAdapterOfSlogDownload;

  public SlogDownloadDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSlogDownload = new EntityInsertionAdapter<SlogDownload>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `SlogDownload` (`id`,`type`,`source`,`file`,`downloadUrl`,`addTime`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SlogDownload value) {
        stmt.bindLong(1, value.getId());
        if (value.getType() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getType());
        }
        if (value.getSource() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSource());
        }
        if (value.getFile() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getFile());
        }
        if (value.getDownloadUrl() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDownloadUrl());
        }
        stmt.bindLong(6, value.getAddTime());
      }
    };
    this.__deletionAdapterOfSlogDownload = new EntityDeletionOrUpdateAdapter<SlogDownload>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `SlogDownload` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SlogDownload value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfSlogDownload = new EntityDeletionOrUpdateAdapter<SlogDownload>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `SlogDownload` SET `id` = ?,`type` = ?,`source` = ?,`file` = ?,`downloadUrl` = ?,`addTime` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, SlogDownload value) {
        stmt.bindLong(1, value.getId());
        if (value.getType() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getType());
        }
        if (value.getSource() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getSource());
        }
        if (value.getFile() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getFile());
        }
        if (value.getDownloadUrl() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getDownloadUrl());
        }
        stmt.bindLong(6, value.getAddTime());
        stmt.bindLong(7, value.getId());
      }
    };
  }

  @Override
  public Object insert(final SlogDownload item, final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfSlogDownload.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final SlogDownload item, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfSlogDownload.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final SlogDownload item, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfSlogDownload.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<SlogDownload>> observeAll() {
    final String _sql = "SELECT * FROM SlogDownload ORDER BY addTime DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"SlogDownload"}, new Callable<List<SlogDownload>>() {
      @Override
      public List<SlogDownload> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfFile = CursorUtil.getColumnIndexOrThrow(_cursor, "file");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfAddTime = CursorUtil.getColumnIndexOrThrow(_cursor, "addTime");
          final List<SlogDownload> _result = new ArrayList<SlogDownload>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final SlogDownload _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final String _tmpFile;
            if (_cursor.isNull(_cursorIndexOfFile)) {
              _tmpFile = null;
            } else {
              _tmpFile = _cursor.getString(_cursorIndexOfFile);
            }
            final String _tmpDownloadUrl;
            if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
              _tmpDownloadUrl = null;
            } else {
              _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
            }
            final long _tmpAddTime;
            _tmpAddTime = _cursor.getLong(_cursorIndexOfAddTime);
            _item = new SlogDownload(_tmpId,_tmpType,_tmpSource,_tmpFile,_tmpDownloadUrl,_tmpAddTime);
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
  public Object findById(final int id, final Continuation<? super SlogDownload> continuation) {
    final String _sql = "SELECT * FROM SlogDownload WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, id);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SlogDownload>() {
      @Override
      public SlogDownload call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfType = CursorUtil.getColumnIndexOrThrow(_cursor, "type");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfFile = CursorUtil.getColumnIndexOrThrow(_cursor, "file");
          final int _cursorIndexOfDownloadUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadUrl");
          final int _cursorIndexOfAddTime = CursorUtil.getColumnIndexOrThrow(_cursor, "addTime");
          final SlogDownload _result;
          if(_cursor.moveToFirst()) {
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpType;
            if (_cursor.isNull(_cursorIndexOfType)) {
              _tmpType = null;
            } else {
              _tmpType = _cursor.getString(_cursorIndexOfType);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final String _tmpFile;
            if (_cursor.isNull(_cursorIndexOfFile)) {
              _tmpFile = null;
            } else {
              _tmpFile = _cursor.getString(_cursorIndexOfFile);
            }
            final String _tmpDownloadUrl;
            if (_cursor.isNull(_cursorIndexOfDownloadUrl)) {
              _tmpDownloadUrl = null;
            } else {
              _tmpDownloadUrl = _cursor.getString(_cursorIndexOfDownloadUrl);
            }
            final long _tmpAddTime;
            _tmpAddTime = _cursor.getLong(_cursorIndexOfAddTime);
            _result = new SlogDownload(_tmpId,_tmpType,_tmpSource,_tmpFile,_tmpDownloadUrl,_tmpAddTime);
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
