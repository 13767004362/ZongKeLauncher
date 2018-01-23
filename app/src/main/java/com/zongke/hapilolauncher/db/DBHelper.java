package com.zongke.hapilolauncher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper( Context context) {
        super(context, Data.SQLITE_NAME, null, Data.SQLITE_VERSON);
    }

    public void onCreate( SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Data.CREATE_TABLE_COLLECTION_APP);
    }

    public void onUpgrade( SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
