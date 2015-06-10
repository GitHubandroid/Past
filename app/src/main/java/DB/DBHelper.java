package DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SHLSY on 2015/6/1.
 */
public class DBHelper extends SQLiteOpenHelper {
    private static final String name = "since.db";
    private static final int Current_Version = 1;
    private static final String CREATE_SINCE = "create table Since (" + "id integer primary key autoincrement, "
            + "content text, " + "days_num integer, "+"year text, "+"month text, "+"day text, " + "is_forever integer)";

    public DBHelper(Context context) {
        super(context, name, null, Current_Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SINCE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Since");
        this.onCreate(db);
    }
}
