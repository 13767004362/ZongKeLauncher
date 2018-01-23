package com.zongke.hapilolauncher.library.sqlbrite;

import android.content.ContentResolver;
import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import com.squareup.sqlbrite.BriteContentResolver;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;
import com.zongke.hapilolauncher.db.DBHelper;

import rx.schedulers.Schedulers;

/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class SQLBriteProvider {

    private static SQLBriteProvider instance;
    private SqlBrite sqlBrite;
    /**
     * 获取到SQLBrite中数据库对象
     *
     * @return
     */
    private BriteDatabase briteDatabase;
    private BriteContentResolver briteContentResolver;

    public static synchronized SQLBriteProvider getInstance(Context context) {
        if (instance == null) {
            instance = new SQLBriteProvider(context);
        }
        return instance;
    }
    private SQLBriteProvider(Context context) {
        this.sqlBrite = providerSQLBrite();
        this.briteDatabase = createDatabase(this.sqlBrite, providerOpenHelper(context));
        this.briteContentResolver = createContentResolver(context.getContentResolver());
    }

    public BriteDatabase getBriteDatabase() {
        return briteDatabase;
    }

    /**
     * 创建SQLiteOpenHelper对象
     *
     * @param context *
     * @return
     */
    private SQLiteOpenHelper providerOpenHelper(Context context) {
        return new DBHelper(context);
    }
    /**
     * 创建SqlBrite对象
     *
     * @return
     */
    private SqlBrite providerSQLBrite() {
        return new SqlBrite.Builder().build();
    }
    /**
     * 通过SQLBrite对象和SQLiteOpenHel对象
     *
     * @param sqlBrite         *
     * @param sqLiteOpenHelper *
     * @return
     */
    public BriteDatabase createDatabase(SqlBrite sqlBrite, SQLiteOpenHelper sqLiteOpenHelper) {
        BriteDatabase db = sqlBrite.wrapDatabaseHelper(sqLiteOpenHelper, Schedulers.io());
        return db;
    }
    public BriteContentResolver createContentResolver(ContentResolver contentResolver){
        return sqlBrite.wrapContentProvider(contentResolver, Schedulers.io());
    }
}
