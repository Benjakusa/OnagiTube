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
public final class RichDownloadDao_Impl implements RichDownloadDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<RichDownload> __insertionAdapterOfRichDownload;

  private final EntityDeletionOrUpdateAdapter<RichDownload> __deletionAdapterOfRichDownload;

  private final EntityDeletionOrUpdateAdapter<RichDownload> __updateAdapterOfRichDownload;

  public RichDownloadDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfRichDownload = new EntityInsertionAdapter<RichDownload>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR REPLACE INTO `RichDownload` (`id`,`key`,`url`,`referrer`,`title`,`coverUrl`,`durationMs`,`mime`,`ext`,`quality`,`totalBytes`,`downloadedBytes`,`status`,`pauseReason`,`errorCode`,`errorMessage`,`filePath`,`source`,`verifyType`,`digest`,`createdAt`,`updatedAt`,`completedAt`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RichDownload value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getKey() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getKey());
        }
        if (value.getUrl() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUrl());
        }
        if (value.getReferrer() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getReferrer());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTitle());
        }
        if (value.getCoverUrl() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getCoverUrl());
        }
        if (value.getDurationMs() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, value.getDurationMs());
        }
        if (value.getMime() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getMime());
        }
        if (value.getExt() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getExt());
        }
        if (value.getQuality() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getQuality());
        }
        stmt.bindLong(11, value.getTotalBytes());
        stmt.bindLong(12, value.getDownloadedBytes());
        if (value.getStatus() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getStatus());
        }
        if (value.getPauseReason() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getPauseReason());
        }
        if (value.getErrorCode() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getErrorCode());
        }
        if (value.getErrorMessage() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getErrorMessage());
        }
        if (value.getFilePath() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getFilePath());
        }
        if (value.getSource() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getSource());
        }
        if (value.getVerifyType() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getVerifyType());
        }
        if (value.getDigest() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getDigest());
        }
        stmt.bindLong(21, value.getCreatedAt());
        stmt.bindLong(22, value.getUpdatedAt());
        if (value.getCompletedAt() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindLong(23, value.getCompletedAt());
        }
      }
    };
    this.__deletionAdapterOfRichDownload = new EntityDeletionOrUpdateAdapter<RichDownload>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `RichDownload` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RichDownload value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
      }
    };
    this.__updateAdapterOfRichDownload = new EntityDeletionOrUpdateAdapter<RichDownload>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `RichDownload` SET `id` = ?,`key` = ?,`url` = ?,`referrer` = ?,`title` = ?,`coverUrl` = ?,`durationMs` = ?,`mime` = ?,`ext` = ?,`quality` = ?,`totalBytes` = ?,`downloadedBytes` = ?,`status` = ?,`pauseReason` = ?,`errorCode` = ?,`errorMessage` = ?,`filePath` = ?,`source` = ?,`verifyType` = ?,`digest` = ?,`createdAt` = ?,`updatedAt` = ?,`completedAt` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, RichDownload value) {
        if (value.getId() == null) {
          stmt.bindNull(1);
        } else {
          stmt.bindString(1, value.getId());
        }
        if (value.getKey() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getKey());
        }
        if (value.getUrl() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getUrl());
        }
        if (value.getReferrer() == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindString(4, value.getReferrer());
        }
        if (value.getTitle() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindString(5, value.getTitle());
        }
        if (value.getCoverUrl() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getCoverUrl());
        }
        if (value.getDurationMs() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindLong(7, value.getDurationMs());
        }
        if (value.getMime() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getMime());
        }
        if (value.getExt() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getExt());
        }
        if (value.getQuality() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindString(10, value.getQuality());
        }
        stmt.bindLong(11, value.getTotalBytes());
        stmt.bindLong(12, value.getDownloadedBytes());
        if (value.getStatus() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getStatus());
        }
        if (value.getPauseReason() == null) {
          stmt.bindNull(14);
        } else {
          stmt.bindString(14, value.getPauseReason());
        }
        if (value.getErrorCode() == null) {
          stmt.bindNull(15);
        } else {
          stmt.bindString(15, value.getErrorCode());
        }
        if (value.getErrorMessage() == null) {
          stmt.bindNull(16);
        } else {
          stmt.bindString(16, value.getErrorMessage());
        }
        if (value.getFilePath() == null) {
          stmt.bindNull(17);
        } else {
          stmt.bindString(17, value.getFilePath());
        }
        if (value.getSource() == null) {
          stmt.bindNull(18);
        } else {
          stmt.bindString(18, value.getSource());
        }
        if (value.getVerifyType() == null) {
          stmt.bindNull(19);
        } else {
          stmt.bindString(19, value.getVerifyType());
        }
        if (value.getDigest() == null) {
          stmt.bindNull(20);
        } else {
          stmt.bindString(20, value.getDigest());
        }
        stmt.bindLong(21, value.getCreatedAt());
        stmt.bindLong(22, value.getUpdatedAt());
        if (value.getCompletedAt() == null) {
          stmt.bindNull(23);
        } else {
          stmt.bindLong(23, value.getCompletedAt());
        }
        if (value.getId() == null) {
          stmt.bindNull(24);
        } else {
          stmt.bindString(24, value.getId());
        }
      }
    };
  }

  @Override
  public Object insert(final RichDownload item, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfRichDownload.insert(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final RichDownload item, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfRichDownload.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final RichDownload item, final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfRichDownload.handle(item);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<RichDownload>> observeAll() {
    final String _sql = "SELECT * FROM RichDownload ORDER BY createdAt DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"RichDownload"}, new Callable<List<RichDownload>>() {
      @Override
      public List<RichDownload> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfReferrer = CursorUtil.getColumnIndexOrThrow(_cursor, "referrer");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfMime = CursorUtil.getColumnIndexOrThrow(_cursor, "mime");
          final int _cursorIndexOfExt = CursorUtil.getColumnIndexOrThrow(_cursor, "ext");
          final int _cursorIndexOfQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "quality");
          final int _cursorIndexOfTotalBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBytes");
          final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPauseReason = CursorUtil.getColumnIndexOrThrow(_cursor, "pauseReason");
          final int _cursorIndexOfErrorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "errorCode");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfVerifyType = CursorUtil.getColumnIndexOrThrow(_cursor, "verifyType");
          final int _cursorIndexOfDigest = CursorUtil.getColumnIndexOrThrow(_cursor, "digest");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<RichDownload> _result = new ArrayList<RichDownload>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RichDownload _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpKey;
            if (_cursor.isNull(_cursorIndexOfKey)) {
              _tmpKey = null;
            } else {
              _tmpKey = _cursor.getString(_cursorIndexOfKey);
            }
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final String _tmpReferrer;
            if (_cursor.isNull(_cursorIndexOfReferrer)) {
              _tmpReferrer = null;
            } else {
              _tmpReferrer = _cursor.getString(_cursorIndexOfReferrer);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final Long _tmpDurationMs;
            if (_cursor.isNull(_cursorIndexOfDurationMs)) {
              _tmpDurationMs = null;
            } else {
              _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            }
            final String _tmpMime;
            if (_cursor.isNull(_cursorIndexOfMime)) {
              _tmpMime = null;
            } else {
              _tmpMime = _cursor.getString(_cursorIndexOfMime);
            }
            final String _tmpExt;
            if (_cursor.isNull(_cursorIndexOfExt)) {
              _tmpExt = null;
            } else {
              _tmpExt = _cursor.getString(_cursorIndexOfExt);
            }
            final String _tmpQuality;
            if (_cursor.isNull(_cursorIndexOfQuality)) {
              _tmpQuality = null;
            } else {
              _tmpQuality = _cursor.getString(_cursorIndexOfQuality);
            }
            final long _tmpTotalBytes;
            _tmpTotalBytes = _cursor.getLong(_cursorIndexOfTotalBytes);
            final long _tmpDownloadedBytes;
            _tmpDownloadedBytes = _cursor.getLong(_cursorIndexOfDownloadedBytes);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpPauseReason;
            if (_cursor.isNull(_cursorIndexOfPauseReason)) {
              _tmpPauseReason = null;
            } else {
              _tmpPauseReason = _cursor.getString(_cursorIndexOfPauseReason);
            }
            final String _tmpErrorCode;
            if (_cursor.isNull(_cursorIndexOfErrorCode)) {
              _tmpErrorCode = null;
            } else {
              _tmpErrorCode = _cursor.getString(_cursorIndexOfErrorCode);
            }
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final String _tmpVerifyType;
            if (_cursor.isNull(_cursorIndexOfVerifyType)) {
              _tmpVerifyType = null;
            } else {
              _tmpVerifyType = _cursor.getString(_cursorIndexOfVerifyType);
            }
            final String _tmpDigest;
            if (_cursor.isNull(_cursorIndexOfDigest)) {
              _tmpDigest = null;
            } else {
              _tmpDigest = _cursor.getString(_cursorIndexOfDigest);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new RichDownload(_tmpId,_tmpKey,_tmpUrl,_tmpReferrer,_tmpTitle,_tmpCoverUrl,_tmpDurationMs,_tmpMime,_tmpExt,_tmpQuality,_tmpTotalBytes,_tmpDownloadedBytes,_tmpStatus,_tmpPauseReason,_tmpErrorCode,_tmpErrorMessage,_tmpFilePath,_tmpSource,_tmpVerifyType,_tmpDigest,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
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
  public Object findById(final String id, final Continuation<? super RichDownload> continuation) {
    final String _sql = "SELECT * FROM RichDownload WHERE id = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (id == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, id);
    }
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<RichDownload>() {
      @Override
      public RichDownload call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfReferrer = CursorUtil.getColumnIndexOrThrow(_cursor, "referrer");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfMime = CursorUtil.getColumnIndexOrThrow(_cursor, "mime");
          final int _cursorIndexOfExt = CursorUtil.getColumnIndexOrThrow(_cursor, "ext");
          final int _cursorIndexOfQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "quality");
          final int _cursorIndexOfTotalBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBytes");
          final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPauseReason = CursorUtil.getColumnIndexOrThrow(_cursor, "pauseReason");
          final int _cursorIndexOfErrorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "errorCode");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfVerifyType = CursorUtil.getColumnIndexOrThrow(_cursor, "verifyType");
          final int _cursorIndexOfDigest = CursorUtil.getColumnIndexOrThrow(_cursor, "digest");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final RichDownload _result;
          if(_cursor.moveToFirst()) {
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpKey;
            if (_cursor.isNull(_cursorIndexOfKey)) {
              _tmpKey = null;
            } else {
              _tmpKey = _cursor.getString(_cursorIndexOfKey);
            }
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final String _tmpReferrer;
            if (_cursor.isNull(_cursorIndexOfReferrer)) {
              _tmpReferrer = null;
            } else {
              _tmpReferrer = _cursor.getString(_cursorIndexOfReferrer);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final Long _tmpDurationMs;
            if (_cursor.isNull(_cursorIndexOfDurationMs)) {
              _tmpDurationMs = null;
            } else {
              _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            }
            final String _tmpMime;
            if (_cursor.isNull(_cursorIndexOfMime)) {
              _tmpMime = null;
            } else {
              _tmpMime = _cursor.getString(_cursorIndexOfMime);
            }
            final String _tmpExt;
            if (_cursor.isNull(_cursorIndexOfExt)) {
              _tmpExt = null;
            } else {
              _tmpExt = _cursor.getString(_cursorIndexOfExt);
            }
            final String _tmpQuality;
            if (_cursor.isNull(_cursorIndexOfQuality)) {
              _tmpQuality = null;
            } else {
              _tmpQuality = _cursor.getString(_cursorIndexOfQuality);
            }
            final long _tmpTotalBytes;
            _tmpTotalBytes = _cursor.getLong(_cursorIndexOfTotalBytes);
            final long _tmpDownloadedBytes;
            _tmpDownloadedBytes = _cursor.getLong(_cursorIndexOfDownloadedBytes);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpPauseReason;
            if (_cursor.isNull(_cursorIndexOfPauseReason)) {
              _tmpPauseReason = null;
            } else {
              _tmpPauseReason = _cursor.getString(_cursorIndexOfPauseReason);
            }
            final String _tmpErrorCode;
            if (_cursor.isNull(_cursorIndexOfErrorCode)) {
              _tmpErrorCode = null;
            } else {
              _tmpErrorCode = _cursor.getString(_cursorIndexOfErrorCode);
            }
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final String _tmpVerifyType;
            if (_cursor.isNull(_cursorIndexOfVerifyType)) {
              _tmpVerifyType = null;
            } else {
              _tmpVerifyType = _cursor.getString(_cursorIndexOfVerifyType);
            }
            final String _tmpDigest;
            if (_cursor.isNull(_cursorIndexOfDigest)) {
              _tmpDigest = null;
            } else {
              _tmpDigest = _cursor.getString(_cursorIndexOfDigest);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _result = new RichDownload(_tmpId,_tmpKey,_tmpUrl,_tmpReferrer,_tmpTitle,_tmpCoverUrl,_tmpDurationMs,_tmpMime,_tmpExt,_tmpQuality,_tmpTotalBytes,_tmpDownloadedBytes,_tmpStatus,_tmpPauseReason,_tmpErrorCode,_tmpErrorMessage,_tmpFilePath,_tmpSource,_tmpVerifyType,_tmpDigest,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
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

  @Override
  public Flow<List<RichDownload>> findByStatus(final String status) {
    final String _sql = "SELECT * FROM RichDownload WHERE status = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (status == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, status);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"RichDownload"}, new Callable<List<RichDownload>>() {
      @Override
      public List<RichDownload> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfKey = CursorUtil.getColumnIndexOrThrow(_cursor, "key");
          final int _cursorIndexOfUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "url");
          final int _cursorIndexOfReferrer = CursorUtil.getColumnIndexOrThrow(_cursor, "referrer");
          final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
          final int _cursorIndexOfCoverUrl = CursorUtil.getColumnIndexOrThrow(_cursor, "coverUrl");
          final int _cursorIndexOfDurationMs = CursorUtil.getColumnIndexOrThrow(_cursor, "durationMs");
          final int _cursorIndexOfMime = CursorUtil.getColumnIndexOrThrow(_cursor, "mime");
          final int _cursorIndexOfExt = CursorUtil.getColumnIndexOrThrow(_cursor, "ext");
          final int _cursorIndexOfQuality = CursorUtil.getColumnIndexOrThrow(_cursor, "quality");
          final int _cursorIndexOfTotalBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "totalBytes");
          final int _cursorIndexOfDownloadedBytes = CursorUtil.getColumnIndexOrThrow(_cursor, "downloadedBytes");
          final int _cursorIndexOfStatus = CursorUtil.getColumnIndexOrThrow(_cursor, "status");
          final int _cursorIndexOfPauseReason = CursorUtil.getColumnIndexOrThrow(_cursor, "pauseReason");
          final int _cursorIndexOfErrorCode = CursorUtil.getColumnIndexOrThrow(_cursor, "errorCode");
          final int _cursorIndexOfErrorMessage = CursorUtil.getColumnIndexOrThrow(_cursor, "errorMessage");
          final int _cursorIndexOfFilePath = CursorUtil.getColumnIndexOrThrow(_cursor, "filePath");
          final int _cursorIndexOfSource = CursorUtil.getColumnIndexOrThrow(_cursor, "source");
          final int _cursorIndexOfVerifyType = CursorUtil.getColumnIndexOrThrow(_cursor, "verifyType");
          final int _cursorIndexOfDigest = CursorUtil.getColumnIndexOrThrow(_cursor, "digest");
          final int _cursorIndexOfCreatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "createdAt");
          final int _cursorIndexOfUpdatedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "updatedAt");
          final int _cursorIndexOfCompletedAt = CursorUtil.getColumnIndexOrThrow(_cursor, "completedAt");
          final List<RichDownload> _result = new ArrayList<RichDownload>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final RichDownload _item;
            final String _tmpId;
            if (_cursor.isNull(_cursorIndexOfId)) {
              _tmpId = null;
            } else {
              _tmpId = _cursor.getString(_cursorIndexOfId);
            }
            final String _tmpKey;
            if (_cursor.isNull(_cursorIndexOfKey)) {
              _tmpKey = null;
            } else {
              _tmpKey = _cursor.getString(_cursorIndexOfKey);
            }
            final String _tmpUrl;
            if (_cursor.isNull(_cursorIndexOfUrl)) {
              _tmpUrl = null;
            } else {
              _tmpUrl = _cursor.getString(_cursorIndexOfUrl);
            }
            final String _tmpReferrer;
            if (_cursor.isNull(_cursorIndexOfReferrer)) {
              _tmpReferrer = null;
            } else {
              _tmpReferrer = _cursor.getString(_cursorIndexOfReferrer);
            }
            final String _tmpTitle;
            if (_cursor.isNull(_cursorIndexOfTitle)) {
              _tmpTitle = null;
            } else {
              _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
            }
            final String _tmpCoverUrl;
            if (_cursor.isNull(_cursorIndexOfCoverUrl)) {
              _tmpCoverUrl = null;
            } else {
              _tmpCoverUrl = _cursor.getString(_cursorIndexOfCoverUrl);
            }
            final Long _tmpDurationMs;
            if (_cursor.isNull(_cursorIndexOfDurationMs)) {
              _tmpDurationMs = null;
            } else {
              _tmpDurationMs = _cursor.getLong(_cursorIndexOfDurationMs);
            }
            final String _tmpMime;
            if (_cursor.isNull(_cursorIndexOfMime)) {
              _tmpMime = null;
            } else {
              _tmpMime = _cursor.getString(_cursorIndexOfMime);
            }
            final String _tmpExt;
            if (_cursor.isNull(_cursorIndexOfExt)) {
              _tmpExt = null;
            } else {
              _tmpExt = _cursor.getString(_cursorIndexOfExt);
            }
            final String _tmpQuality;
            if (_cursor.isNull(_cursorIndexOfQuality)) {
              _tmpQuality = null;
            } else {
              _tmpQuality = _cursor.getString(_cursorIndexOfQuality);
            }
            final long _tmpTotalBytes;
            _tmpTotalBytes = _cursor.getLong(_cursorIndexOfTotalBytes);
            final long _tmpDownloadedBytes;
            _tmpDownloadedBytes = _cursor.getLong(_cursorIndexOfDownloadedBytes);
            final String _tmpStatus;
            if (_cursor.isNull(_cursorIndexOfStatus)) {
              _tmpStatus = null;
            } else {
              _tmpStatus = _cursor.getString(_cursorIndexOfStatus);
            }
            final String _tmpPauseReason;
            if (_cursor.isNull(_cursorIndexOfPauseReason)) {
              _tmpPauseReason = null;
            } else {
              _tmpPauseReason = _cursor.getString(_cursorIndexOfPauseReason);
            }
            final String _tmpErrorCode;
            if (_cursor.isNull(_cursorIndexOfErrorCode)) {
              _tmpErrorCode = null;
            } else {
              _tmpErrorCode = _cursor.getString(_cursorIndexOfErrorCode);
            }
            final String _tmpErrorMessage;
            if (_cursor.isNull(_cursorIndexOfErrorMessage)) {
              _tmpErrorMessage = null;
            } else {
              _tmpErrorMessage = _cursor.getString(_cursorIndexOfErrorMessage);
            }
            final String _tmpFilePath;
            if (_cursor.isNull(_cursorIndexOfFilePath)) {
              _tmpFilePath = null;
            } else {
              _tmpFilePath = _cursor.getString(_cursorIndexOfFilePath);
            }
            final String _tmpSource;
            if (_cursor.isNull(_cursorIndexOfSource)) {
              _tmpSource = null;
            } else {
              _tmpSource = _cursor.getString(_cursorIndexOfSource);
            }
            final String _tmpVerifyType;
            if (_cursor.isNull(_cursorIndexOfVerifyType)) {
              _tmpVerifyType = null;
            } else {
              _tmpVerifyType = _cursor.getString(_cursorIndexOfVerifyType);
            }
            final String _tmpDigest;
            if (_cursor.isNull(_cursorIndexOfDigest)) {
              _tmpDigest = null;
            } else {
              _tmpDigest = _cursor.getString(_cursorIndexOfDigest);
            }
            final long _tmpCreatedAt;
            _tmpCreatedAt = _cursor.getLong(_cursorIndexOfCreatedAt);
            final long _tmpUpdatedAt;
            _tmpUpdatedAt = _cursor.getLong(_cursorIndexOfUpdatedAt);
            final Long _tmpCompletedAt;
            if (_cursor.isNull(_cursorIndexOfCompletedAt)) {
              _tmpCompletedAt = null;
            } else {
              _tmpCompletedAt = _cursor.getLong(_cursorIndexOfCompletedAt);
            }
            _item = new RichDownload(_tmpId,_tmpKey,_tmpUrl,_tmpReferrer,_tmpTitle,_tmpCoverUrl,_tmpDurationMs,_tmpMime,_tmpExt,_tmpQuality,_tmpTotalBytes,_tmpDownloadedBytes,_tmpStatus,_tmpPauseReason,_tmpErrorCode,_tmpErrorMessage,_tmpFilePath,_tmpSource,_tmpVerifyType,_tmpDigest,_tmpCreatedAt,_tmpUpdatedAt,_tmpCompletedAt);
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
