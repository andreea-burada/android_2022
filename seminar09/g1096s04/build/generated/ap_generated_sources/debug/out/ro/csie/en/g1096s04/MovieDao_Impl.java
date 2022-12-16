package ro.csie.en.g1096s04;

import android.database.Cursor;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unchecked", "deprecation"})
public final class MovieDao_Impl implements MovieDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<Movie> __insertionAdapterOfMovie;

  private final EntityDeletionOrUpdateAdapter<Movie> __deletionAdapterOfMovie;

  private final EntityDeletionOrUpdateAdapter<Movie> __updateAdapterOfMovie;

  private final SharedSQLiteStatement __preparedStmtOfDeleteMovieById;

  public MovieDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMovie = new EntityInsertionAdapter<Movie>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `movie` (`id`,`title`,`genre`,`release`,`duration`) VALUES (nullif(?, 0),?,?,?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Movie value) {
        stmt.bindLong(1, value.getMovieId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getGenre() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, __Genre_enumToString(value.getGenre()));
        }
        final Long _tmp = DateConverter.toTimestamp(value.getRelease());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.getDuration() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getDuration());
        }
      }
    };
    this.__deletionAdapterOfMovie = new EntityDeletionOrUpdateAdapter<Movie>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `movie` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Movie value) {
        stmt.bindLong(1, value.getMovieId());
      }
    };
    this.__updateAdapterOfMovie = new EntityDeletionOrUpdateAdapter<Movie>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `movie` SET `id` = ?,`title` = ?,`genre` = ?,`release` = ?,`duration` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, Movie value) {
        stmt.bindLong(1, value.getMovieId());
        if (value.getTitle() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getTitle());
        }
        if (value.getGenre() == null) {
          stmt.bindNull(3);
        } else {
          stmt.bindString(3, __Genre_enumToString(value.getGenre()));
        }
        final Long _tmp = DateConverter.toTimestamp(value.getRelease());
        if (_tmp == null) {
          stmt.bindNull(4);
        } else {
          stmt.bindLong(4, _tmp);
        }
        if (value.getDuration() == null) {
          stmt.bindNull(5);
        } else {
          stmt.bindLong(5, value.getDuration());
        }
        stmt.bindLong(6, value.getMovieId());
      }
    };
    this.__preparedStmtOfDeleteMovieById = new SharedSQLiteStatement(__db) {
      @Override
      public String createQuery() {
        final String _query = "DELETE FROM movie WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public long insert(final Movie movie) {
    __db.assertNotSuspendingTransaction();
    __db.beginTransaction();
    try {
      long _result = __insertionAdapterOfMovie.insertAndReturnId(movie);
      __db.setTransactionSuccessful();
      return _result;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int delete(final Movie movie) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__deletionAdapterOfMovie.handle(movie);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final Movie movie) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__updateAdapterOfMovie.handle(movie);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public int update(final List<Movie> movies) {
    __db.assertNotSuspendingTransaction();
    int _total = 0;
    __db.beginTransaction();
    try {
      _total +=__updateAdapterOfMovie.handleMultiple(movies);
      __db.setTransactionSuccessful();
      return _total;
    } finally {
      __db.endTransaction();
    }
  }

  @Override
  public void deleteMovieById(final int movieId) {
    __db.assertNotSuspendingTransaction();
    final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteMovieById.acquire();
    int _argIndex = 1;
    _stmt.bindLong(_argIndex, movieId);
    __db.beginTransaction();
    try {
      _stmt.executeUpdateDelete();
      __db.setTransactionSuccessful();
    } finally {
      __db.endTransaction();
      __preparedStmtOfDeleteMovieById.release(_stmt);
    }
  }

  @Override
  public List<Movie> getAllMovies() {
    final String _sql = "select * from movie";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
      final int _cursorIndexOfRelease = CursorUtil.getColumnIndexOrThrow(_cursor, "release");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final List<Movie> _result = new ArrayList<Movie>(_cursor.getCount());
      while(_cursor.moveToNext()) {
        final Movie _item;
        _item = new Movie();
        final long _tmpMovieId;
        _tmpMovieId = _cursor.getLong(_cursorIndexOfMovieId);
        _item.setMovieId(_tmpMovieId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _item.setTitle(_tmpTitle);
        final Genre _tmpGenre;
        _tmpGenre = __Genre_stringToEnum(_cursor.getString(_cursorIndexOfGenre));
        _item.setGenre(_tmpGenre);
        final Date _tmpRelease;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfRelease)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfRelease);
        }
        _tmpRelease = DateConverter.toDate(_tmp);
        _item.setRelease(_tmpRelease);
        final Integer _tmpDuration;
        if (_cursor.isNull(_cursorIndexOfDuration)) {
          _tmpDuration = null;
        } else {
          _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        }
        _item.setDuration(_tmpDuration);
        _result.add(_item);
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  @Override
  public Cursor getCursorMovies() {
    final String _sql = "select * from movie";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final Cursor _tmpResult = __db.query(_statement);
    return _tmpResult;
  }

  @Override
  public Movie getMovieById(final long movieId) {
    final String _sql = "select * from movie where id=?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, movieId);
    __db.assertNotSuspendingTransaction();
    final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
    try {
      final int _cursorIndexOfMovieId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
      final int _cursorIndexOfTitle = CursorUtil.getColumnIndexOrThrow(_cursor, "title");
      final int _cursorIndexOfGenre = CursorUtil.getColumnIndexOrThrow(_cursor, "genre");
      final int _cursorIndexOfRelease = CursorUtil.getColumnIndexOrThrow(_cursor, "release");
      final int _cursorIndexOfDuration = CursorUtil.getColumnIndexOrThrow(_cursor, "duration");
      final Movie _result;
      if(_cursor.moveToFirst()) {
        _result = new Movie();
        final long _tmpMovieId;
        _tmpMovieId = _cursor.getLong(_cursorIndexOfMovieId);
        _result.setMovieId(_tmpMovieId);
        final String _tmpTitle;
        if (_cursor.isNull(_cursorIndexOfTitle)) {
          _tmpTitle = null;
        } else {
          _tmpTitle = _cursor.getString(_cursorIndexOfTitle);
        }
        _result.setTitle(_tmpTitle);
        final Genre _tmpGenre;
        _tmpGenre = __Genre_stringToEnum(_cursor.getString(_cursorIndexOfGenre));
        _result.setGenre(_tmpGenre);
        final Date _tmpRelease;
        final Long _tmp;
        if (_cursor.isNull(_cursorIndexOfRelease)) {
          _tmp = null;
        } else {
          _tmp = _cursor.getLong(_cursorIndexOfRelease);
        }
        _tmpRelease = DateConverter.toDate(_tmp);
        _result.setRelease(_tmpRelease);
        final Integer _tmpDuration;
        if (_cursor.isNull(_cursorIndexOfDuration)) {
          _tmpDuration = null;
        } else {
          _tmpDuration = _cursor.getInt(_cursorIndexOfDuration);
        }
        _result.setDuration(_tmpDuration);
      } else {
        _result = null;
      }
      return _result;
    } finally {
      _cursor.close();
      _statement.release();
    }
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }

  private String __Genre_enumToString(final Genre _value) {
    if (_value == null) {
      return null;
    } switch (_value) {
      case ADVENTURE: return "ADVENTURE";
      case COMEDY: return "COMEDY";
      case DRAMA: return "DRAMA";
      case ACTION: return "ACTION";
      case ANIMATION: return "ANIMATION";
      default: throw new IllegalArgumentException("Can't convert enum to string, unknown enum value: " + _value);
    }
  }

  private Genre __Genre_stringToEnum(final String _value) {
    if (_value == null) {
      return null;
    } switch (_value) {
      case "ADVENTURE": return Genre.ADVENTURE;
      case "COMEDY": return Genre.COMEDY;
      case "DRAMA": return Genre.DRAMA;
      case "ACTION": return Genre.ACTION;
      case "ANIMATION": return Genre.ANIMATION;
      default: throw new IllegalArgumentException("Can't convert value to enum, unknown value: " + _value);
    }
  }
}
