package com.example.nicolemorris.lifestyle.Room;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class WeatherDao_Impl implements WeatherDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<WeatherDataTable> __insertionAdapterOfWeatherDataTable;

  private final EntityDeletionOrUpdateAdapter<WeatherDataTable> __deletionAdapterOfWeatherDataTable;

  private final EntityDeletionOrUpdateAdapter<WeatherDataTable> __updateAdapterOfWeatherDataTable;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public WeatherDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfWeatherDataTable = new EntityInsertionAdapter<WeatherDataTable>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `WeatherDataTable` (`did`,`location`,`weatherdata`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, WeatherDataTable value) {
        stmt.bindLong(1, value.did);
        if (value.getLocation() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getLocation());
        }
        if (value.getWeatherJson() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getWeatherJson());
        }
      }
    };
    this.__deletionAdapterOfWeatherDataTable = new EntityDeletionOrUpdateAdapter<WeatherDataTable>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `WeatherDataTable` WHERE `did` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, WeatherDataTable value) {
        stmt.bindLong(1, value.did);
      }
    };
    this.__updateAdapterOfWeatherDataTable = new EntityDeletionOrUpdateAdapter<WeatherDataTable>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `WeatherDataTable` SET `did` = ?,`location` = ?,`weatherdata` = ? WHERE `did` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, WeatherDataTable value) {
        stmt.bindLong(1, value.did);
        if (value.getLocation() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getLocation());
        }
        if (value.getWeatherJson() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, value.getWeatherJson());
        }
        stmt.bindLong(4, value.did);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from WeatherDataTable";
        return _query;
      }
    };
  }

  @Override
  public void insertWeatherDataTable(final WeatherDataTable wdt) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfWeatherDataTable.insert(wdt);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteWeatherDataTable(final WeatherDataTable wdt) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfWeatherDataTable.handle(wdt);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateWeatherDataTable(final WeatherDataTable wdt) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfWeatherDataTable.handle(wdt);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteAll() {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteAll.acquire();
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteAll.release(_stmt);
    }
  }

  @Override
  public List<WeatherDataTable> getAll() {
    final String _sql = "select * from WeatherDataTable";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfDid = CursorUtil.getColumnIndexOrThrow(_cursor, "did");
      final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
      final int _cursorIndexOfWeatherJson = CursorUtil.getColumnIndexOrThrow(_cursor, "weatherdata");
      final List<WeatherDataTable> _result = new ArrayList<WeatherDataTable>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final WeatherDataTable _item;
        final String _tmpLocation;
        _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
        final String _tmpWeatherJson;
        _tmpWeatherJson = _cursor.getString(_cursorIndexOfWeatherJson);
        _item = new WeatherDataTable(_tmpLocation,_tmpWeatherJson);
        _item.did = _cursor.getInt(_cursorIndexOfDid);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
