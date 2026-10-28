package com.onagitube.data;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
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
public final class DownloadFailRecordDao_Impl implements DownloadFailRecordDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<DownloadFailRecord> __insertionAdapterOfDownloadFailRecord;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByDownloadId;

  public DownloadFailRecordDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfDownloadFailRecord = new EntityInsertionAdapter<DownloadFailRecord>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `DownloadFailRecord` (`id`,`downloadId`,`errorCode`,`timestamp`,`retryCount`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, DownloadFailRecord value) {
        stmt.bindLong(1, value.getId());
        if (value.getDownloadId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDownloadId());
        }
        if (value.getErrorCode() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getErrorCode());
        }
        stmt.bindLong(4, value.getTimestamp());
        stmt.bindLong(5, value.getRetryCount());
      }
    };
    this.__preparedStmtOfDeleteByDownloadId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM DownloadFailRecord WHERE downloadId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insert(final DownloadFailRecord item,
      final Continuation<? super Long> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          long _result = __insertionAdapterOfDownloadFailRecord.insertAndReturnId(item);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object deleteByDownloadId(final String downloadId,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteByDownloadId.acquire();
        int _argIndex = 1;
        if (downloadId == null) {
          _stmt.bindNull(_argIndex);
        } else {
          _stmt.bindString(_argIndex, downloadId);
        }
        __db.beginTransaction();
        try {
          _stmt.executeUpdateDelete();
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
          __preparedStmtOfDeleteByDownloadId.release(_stmt);
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<DownloadFailRecord>> findByDownloadId(final String downloadId) {
    final String _sql = "SELECT * FROM DownloadFailRecord WHERE downloadId = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (downloadId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, downloadId);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"DownloadFailRecord"}, new Callable<List<DownloadFailRecord>>() {
      @Override
      public List<DownloadFailRecord> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDownloadId = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadId");
          final int _cursorIndexOfErrorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "errorCode");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfRetryCount = CursorUtil.getColumnIndexOrThrow(_cursor, "retryCount");
          final List<DownloadFailRecord> _result = new ArrayList<DownloadFailRecord>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final DownloadFailRecord _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDownloadId;
            if (_cursor.isNull(_cursorIndexOfDownloadId)) {
              _tmpDownloadId = null;
            } else {
              _tmpDownloadId = _cursor.getString(_cursorIndexOfDownloadId);
            }
            final String _tmpErrorCode;
            if (_cursor.isNull(_cursorIndexOfErrorCode)) {
              _tmpErrorCode = null;
            } else {
              _tmpErrorCode = _cursor.getString(_cursorIndexOfErrorCode);
            }
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final int _tmpRetryCount;
            _tmpRetryCount = _cursor.getInt(_cursorIndexOfRetryCount);
            _item = new DownloadFailRecord(_tmpId,_tmpDownloadId,_tmpErrorCode,_tmpTimestamp,_tmpRetryCount);
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

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
