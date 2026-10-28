package com.onagitube.data;

import android.database.Cursor;
import android.os.CancellationSignal;
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

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SegmentDao_Impl implements SegmentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Segment> __insertionAdapterOfSegment;

  private final SharedSQLiteStatement __preparedStmtOfDeleteByDownloadId;

  public SegmentDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSegment = new EntityInsertionAdapter<Segment>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `Segment` (`id`,`downloadId`,`blockOffset`,`blockLength`,`downloadedBytes`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Segment value) {
        stmt.bindLong(1, value.getId());
        if (value.getDownloadId() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getDownloadId());
        }
        stmt.bindLong(3, value.getBlockOffset());
        stmt.bindLong(4, value.getBlockLength());
        stmt.bindLong(5, value.getDownloadedBytes());
        stmt.bindLong(6, value.getTimestamp());
      }
    };
    this.__preparedStmtOfDeleteByDownloadId = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM Segment WHERE downloadId = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertAll(final List<Segment> items,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSegment.insert(items);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
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
  public Object findByDownloadId(final String downloadId,
      final Continuation<? super List<Segment>> continuation) {
    final String _sql = "SELECT * FROM Segment WHERE downloadId = ? ORDER BY blockOffset ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (downloadId == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, downloadId);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<Segment>>() {
      @Override
      public List<Segment> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfDownloadId = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadId");
          final int _cursorIndexOfBlockOffset = CursorUtil.getColumnIndexOrThrow(_cursor, "blockOffset");
          final int _cursorIndexOfBlockLength = CursorUtil.getColumnIndexOrThrow(_cursor, "blockLength");
          final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<Segment> _result = new ArrayList<Segment>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final Segment _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpDownloadId;
            if (_cursor.isNull(_cursorIndexOfDownloadId)) {
              _tmpDownloadId = null;
            } else {
              _tmpDownloadId = _cursor.getString(_cursorIndexOfDownloadId);
            }
            final long _tmpBlockOffset;
            _tmpBlockOffset = _cursor.getLong(_cursorIndexOfBlockOffset);
            final int _tmpBlockLength;
            _tmpBlockLength = _cursor.getInt(_cursorIndexOfBlockLength);
            final int _tmpDownloadedBytes;
            _tmpDownloadedBytes = _cursor.getInt(_cursorIndexOfDownloadedBytes);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new Segment(_tmpId,_tmpDownloadId,_tmpBlockOffset,_tmpBlockLength,_tmpDownloadedBytes,_tmpTimestamp);
            _result.add(_item);
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
