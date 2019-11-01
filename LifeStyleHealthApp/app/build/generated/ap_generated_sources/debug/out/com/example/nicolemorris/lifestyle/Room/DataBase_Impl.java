package com.example.nicolemorris.lifestyle.Room;

import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomOpenHelper;
import androidx.room.RoomOpenHelper.Delegate;
import androidx.room.RoomOpenHelper.ValidationResult;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.room.util.TableInfo.Column;
import androidx.room.util.TableInfo.ForeignKey;
import androidx.room.util.TableInfo.Index;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Callback;
import androidx.sqlite.db.SupportSQLiteOpenHelper.Configuration;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "deprecation"})
public final class DataBase_Impl extends DataBase {
  private volatile WeatherDao _weatherDao;

  private volatile UserDao _userDao;

  @Override
  protected SupportSQLiteOpenHelper createOpenHelper(DatabaseConfiguration configuration) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(configuration, new RoomOpenHelper.Delegate(2) {
      @Override
      public void createAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("CREATE TABLE IF NOT EXISTS `WeatherDataTable` (`did` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `location` TEXT NOT NULL, `weatherdata` TEXT NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS `UserTable` (`did` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT, `age` INTEGER NOT NULL, `city` TEXT, `state` TEXT, `feet` INTEGER NOT NULL, `inches` INTEGER NOT NULL, `weight` INTEGER NOT NULL, `sex` TEXT, `hasGoal` INTEGER NOT NULL, `goal` INTEGER, `act_level` INTEGER, `weight_amt` INTEGER, `uri` TEXT, `stepTimeStamp` INTEGER NOT NULL, `dailySteps` INTEGER NOT NULL)");
        _db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        _db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'cbae57969a3d720a714d50abb68d40c2')");
      }

      @Override
      public void dropAllTables(SupportSQLiteDatabase _db) {
        _db.execSQL("DROP TABLE IF EXISTS `WeatherDataTable`");
        _db.execSQL("DROP TABLE IF EXISTS `UserTable`");
        if (mCallbacks != null) {
          for (int _i = 0, _size = mCallbacks.size(); _i < _size; _i++) {
            mCallbacks.get(_i).onDestructiveMigration(_db);
          }
        }
      }

      @Override
      protected void onCreate(SupportSQLiteDatabase _db) {
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
      protected RoomOpenHelper.ValidationResult onValidateSchema(SupportSQLiteDatabase _db) {
        final HashMap<String, TableInfo.Column> _columnsWeatherDataTable = new HashMap<String, TableInfo.Column>(3);
        _columnsWeatherDataTable.put("did", new TableInfo.Column("did", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeatherDataTable.put("location", new TableInfo.Column("location", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsWeatherDataTable.put("weatherdata", new TableInfo.Column("weatherdata", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysWeatherDataTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesWeatherDataTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoWeatherDataTable = new TableInfo("WeatherDataTable", _columnsWeatherDataTable, _foreignKeysWeatherDataTable, _indicesWeatherDataTable);
        final TableInfo _existingWeatherDataTable = TableInfo.read(_db, "WeatherDataTable");
        if (! _infoWeatherDataTable.equals(_existingWeatherDataTable)) {
          return new RoomOpenHelper.ValidationResult(false, "WeatherDataTable(com.example.nicolemorris.lifestyle.Room.WeatherDataTable).\n"
                  + " Expected:\n" + _infoWeatherDataTable + "\n"
                  + " Found:\n" + _existingWeatherDataTable);
        }
        final HashMap<String, TableInfo.Column> _columnsUserTable = new HashMap<String, TableInfo.Column>(16);
        _columnsUserTable.put("did", new TableInfo.Column("did", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("name", new TableInfo.Column("name", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("age", new TableInfo.Column("age", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("city", new TableInfo.Column("city", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("state", new TableInfo.Column("state", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("feet", new TableInfo.Column("feet", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("inches", new TableInfo.Column("inches", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("weight", new TableInfo.Column("weight", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("sex", new TableInfo.Column("sex", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("hasGoal", new TableInfo.Column("hasGoal", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("goal", new TableInfo.Column("goal", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("act_level", new TableInfo.Column("act_level", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("weight_amt", new TableInfo.Column("weight_amt", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("uri", new TableInfo.Column("uri", "TEXT", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("stepTimeStamp", new TableInfo.Column("stepTimeStamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsUserTable.put("dailySteps", new TableInfo.Column("dailySteps", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysUserTable = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesUserTable = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoUserTable = new TableInfo("UserTable", _columnsUserTable, _foreignKeysUserTable, _indicesUserTable);
        final TableInfo _existingUserTable = TableInfo.read(_db, "UserTable");
        if (! _infoUserTable.equals(_existingUserTable)) {
          return new RoomOpenHelper.ValidationResult(false, "UserTable(com.example.nicolemorris.lifestyle.Room.UserTable).\n"
                  + " Expected:\n" + _infoUserTable + "\n"
                  + " Found:\n" + _existingUserTable);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "cbae57969a3d720a714d50abb68d40c2", "ba8cfd5a5e858a216d471611e51b062e");
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
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "WeatherDataTable","UserTable");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    try {
      super.beginTransaction();
      _db.execSQL("DELETE FROM `WeatherDataTable`");
      _db.execSQL("DELETE FROM `UserTable`");
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
  public WeatherDao weatherDao() {
    if (_weatherDao != null) {
      return _weatherDao;
    } else {
      synchronized(this) {
        if(_weatherDao == null) {
          _weatherDao = new WeatherDao_Impl(this);
        }
        return _weatherDao;
      }
    }
  }

  @Override
  public UserDao userDao() {
    if (_userDao != null) {
      return _userDao;
    } else {
      synchronized(this) {
        if(_userDao == null) {
          _userDao = new UserDao_Impl(this);
        }
        return _userDao;
      }
    }
  }
}
