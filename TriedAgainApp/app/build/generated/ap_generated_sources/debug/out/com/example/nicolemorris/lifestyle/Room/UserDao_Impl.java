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
import java.lang.Integer;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class UserDao_Impl implements UserDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<UserTable> __insertionAdapterOfUserTable;

  private final EntityDeletionOrUpdateAdapter<UserTable> __deletionAdapterOfUserTable;

  private final EntityDeletionOrUpdateAdapter<UserTable> __updateAdapterOfUserTable;

  private final SharedSQLiteStatement __preparedStmtOfDeleteAll;

  public UserDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfUserTable = new EntityInsertionAdapter<UserTable>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `UserTable` (`did`,`age`,`feet`,`inches`,`weight`,`name`,`city`,`state`,`sex`,`goal`,`act_level`,`weight_amt`,`uri`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserTable value) {
        stmt.bindLong(1, value.did);
        stmt.bindLong(2, value.getAge());
        stmt.bindLong(3, value.getFeet());
        stmt.bindLong(4, value.getInches());
        stmt.bindLong(5, value.getWeight());
        if (value.getName() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getName());
        }
        if (value.getCity() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getCity());
        }
        if (value.getState() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getState());
        }
        if (value.getSex() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getSex());
        }
        if (value.getGoal() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, value.getGoal());
        }
        if (value.getAct_level() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindLong(11, value.getAct_level());
        }
        if (value.getWeight_amt() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindLong(12, value.getWeight_amt());
        }
        if (value.getUri() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getUri());
        }
      }
    };
    this.__deletionAdapterOfUserTable = new EntityDeletionOrUpdateAdapter<UserTable>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `UserTable` WHERE `did` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserTable value) {
        stmt.bindLong(1, value.did);
      }
    };
    this.__updateAdapterOfUserTable = new EntityDeletionOrUpdateAdapter<UserTable>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `UserTable` SET `did` = ?,`age` = ?,`feet` = ?,`inches` = ?,`weight` = ?,`name` = ?,`city` = ?,`state` = ?,`sex` = ?,`goal` = ?,`act_level` = ?,`weight_amt` = ?,`uri` = ? WHERE `did` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, UserTable value) {
        stmt.bindLong(1, value.did);
        stmt.bindLong(2, value.getAge());
        stmt.bindLong(3, value.getFeet());
        stmt.bindLong(4, value.getInches());
        stmt.bindLong(5, value.getWeight());
        if (value.getName() == null) {
          stmt.bindNull(6);
        } else {
          stmt.bindString(6, value.getName());
        }
        if (value.getCity() == null) {
          stmt.bindNull(7);
        } else {
          stmt.bindString(7, value.getCity());
        }
        if (value.getState() == null) {
          stmt.bindNull(8);
        } else {
          stmt.bindString(8, value.getState());
        }
        if (value.getSex() == null) {
          stmt.bindNull(9);
        } else {
          stmt.bindString(9, value.getSex());
        }
        if (value.getGoal() == null) {
          stmt.bindNull(10);
        } else {
          stmt.bindLong(10, value.getGoal());
        }
        if (value.getAct_level() == null) {
          stmt.bindNull(11);
        } else {
          stmt.bindLong(11, value.getAct_level());
        }
        if (value.getWeight_amt() == null) {
          stmt.bindNull(12);
        } else {
          stmt.bindLong(12, value.getWeight_amt());
        }
        if (value.getUri() == null) {
          stmt.bindNull(13);
        } else {
          stmt.bindString(13, value.getUri());
        }
        stmt.bindLong(14, value.did);
      }
    };
    this.__preparedStmtOfDeleteAll = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "delete from UserTable";
        return _query;
      }
    };
  }

  @Override
  public void inserUserTable(final UserTable ut) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __insertionAdapterOfUserTable.insert(ut);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteUserTable(final UserTable ut) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __deletionAdapterOfUserTable.handle(ut);
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void updateUserTable(final UserTable ut) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      __updateAdapterOfUserTable.handle(ut);
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
  public List<UserTable> getAll() {
    final String _sql = "select * from UserTable";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfDid = CursorUtil.getColumnIndexOrThrow(_cursor, "did");
      final int _cursorIndexOfAge = CursorUtil.getColumnIndexOrThrow(_cursor, "age");
      final int _cursorIndexOfFeet = CursorUtil.getColumnIndexOrThrow(_cursor, "feet");
      final int _cursorIndexOfInches = CursorUtil.getColumnIndexOrThrow(_cursor, "inches");
      final int _cursorIndexOfWeight = CursorUtil.getColumnIndexOrThrow(_cursor, "weight");
      final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
      final int _cursorIndexOfCity = CursorUtil.getColumnIndexOrThrow(_cursor, "city");
      final int _cursorIndexOfState = CursorUtil.getColumnIndexOrThrow(_cursor, "state");
      final int _cursorIndexOfSex = CursorUtil.getColumnIndexOrThrow(_cursor, "sex");
      final int _cursorIndexOfGoal = CursorUtil.getColumnIndexOrThrow(_cursor, "goal");
      final int _cursorIndexOfActLevel = CursorUtil.getColumnIndexOrThrow(_cursor, "act_level");
      final int _cursorIndexOfWeightAmt = CursorUtil.getColumnIndexOrThrow(_cursor, "weight_amt");
      final int _cursorIndexOfUri = CursorUtil.getColumnIndexOrThrow(_cursor, "uri");
      final List<UserTable> _result = new ArrayList<UserTable>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final UserTable _item;
        _item = new UserTable();
        _item.did = _cursor.getInt(_cursorIndexOfDid);
        final int _tmpAge;
        _tmpAge = _cursor.getInt(_cursorIndexOfAge);
        _item.setAge(_tmpAge);
        final int _tmpFeet;
        _tmpFeet = _cursor.getInt(_cursorIndexOfFeet);
        _item.setFeet(_tmpFeet);
        final int _tmpInches;
        _tmpInches = _cursor.getInt(_cursorIndexOfInches);
        _item.setInches(_tmpInches);
        final int _tmpWeight;
        _tmpWeight = _cursor.getInt(_cursorIndexOfWeight);
        _item.setWeight(_tmpWeight);
        final String _tmpName;
        _tmpName = _cursor.getString(_cursorIndexOfName);
        _item.setName(_tmpName);
        final String _tmpCity;
        _tmpCity = _cursor.getString(_cursorIndexOfCity);
        _item.setCity(_tmpCity);
        final String _tmpState;
        _tmpState = _cursor.getString(_cursorIndexOfState);
        _item.setState(_tmpState);
        final String _tmpSex;
        _tmpSex = _cursor.getString(_cursorIndexOfSex);
        _item.setSex(_tmpSex);
        final Integer _tmpGoal;
        if (_cursor.isNull(_cursorIndexOfGoal)) {
          _tmpGoal = null;
        } else {
          _tmpGoal = _cursor.getInt(_cursorIndexOfGoal);
        }
        _item.setGoal(_tmpGoal);
        final Integer _tmpAct_level;
        if (_cursor.isNull(_cursorIndexOfActLevel)) {
          _tmpAct_level = null;
        } else {
          _tmpAct_level = _cursor.getInt(_cursorIndexOfActLevel);
        }
        _item.setAct_level(_tmpAct_level);
        final Integer _tmpWeight_amt;
        if (_cursor.isNull(_cursorIndexOfWeightAmt)) {
          _tmpWeight_amt = null;
        } else {
          _tmpWeight_amt = _cursor.getInt(_cursorIndexOfWeightAmt);
        }
        _item.setWeight_amt(_tmpWeight_amt);
        final String _tmpUri;
        _tmpUri = _cursor.getString(_cursorIndexOfUri);
        _item.setUri(_tmpUri);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }
}
