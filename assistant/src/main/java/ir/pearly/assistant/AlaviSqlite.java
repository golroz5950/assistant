package ir.pearly.assistant;

import android.content.Context;
import android.database.Cursor;
import android.database.CursorWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

/**
 * Created by Administrator on 1/14/2018.
 */

public class AlaviSqlite extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public AlaviSqlite(Context context, String Dir, String FileName, boolean CreateIfNecessary) throws Exception {
        super(context, "", null, 1);
        db = SQLiteDatabase.openDatabase(new File(Dir, FileName).toString(), null, (CreateIfNecessary ? 268435456 : 0) | 16);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void ExecNonQuery(String Statement) throws Exception {
        checkNull();
        this.db.execSQL(Statement);
    }

    private void checkNull() {
        if (this.db == null) {
            throw new RuntimeException("Object should first be initialized.");
        }
    }

    public Cursor2 ExecQuery(String Query) throws Exception {
        checkNull();
        return new Cursor2(this.db.rawQuery(Query, null));
    }

    public static class Cursor2 extends CursorWrapper {

        /**
         * Creates a cursor wrapper.
         *
         * @param cursor The underlying cursor to wrap.
         */
        private Cursor cs;

        public Cursor2(Cursor cursor) {
            super(cursor);
            cs = cursor;
        }

        public String getString(String ColumnNames) throws Exception {

            return cs.getString(cs.getColumnIndex(ColumnNames));

        }
    }

}
