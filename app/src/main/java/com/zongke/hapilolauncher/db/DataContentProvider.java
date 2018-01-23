package com.zongke.hapilolauncher.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by ${xingen} on 2017/11/2.
 *
 * 自定义ContentProvider
 */

public class DataContentProvider extends ContentProvider {
    private static final int TABLE_DIR=1;
    private static final int TABLE_DIR_2=2;
    private static UriMatcher uriMatcher;
    private DBHelper dataHelper;
    static {
        uriMatcher=new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(ContentProviderConfig.AUTHORITY,ContentProviderConfig.TABLE_NAME_COLLECTION_APP,TABLE_DIR);
        uriMatcher.addURI(ContentProviderConfig.AUTHORITY,ContentProviderConfig.TABLE_NAME_ALL_INSTANCE_APP,TABLE_DIR_2);
    }
    @Override
    public boolean onCreate() {
        dataHelper=new DBHelper(getContext());
        return true;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dataHelper.getReadableDatabase();
        Cursor cursor=null;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                cursor = db.rawQuery(ContentProviderConfig.createQuery(selection),selectionArgs);
                break;
            case TABLE_DIR_2:
               cursor= db.query(ContentProviderConfig.TABLE_NAME_COLLECTION_APP,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                break;
        }
        if(cursor!=null){
            //添加通知对象
            cursor.setNotificationUri(getContext().getContentResolver(),uri);
        }
        return cursor;
    }


    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                return     "vnd.android.cursor.dir/vnd." +ContentProviderConfig.AUTHORITY + ContentProviderConfig.TABLE_NAME_COLLECTION_APP;
            default:
                break;
        }
        return null;
    }
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase sqLiteDatabase=dataHelper.getWritableDatabase();
        Uri returnUri=null;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                long rowId= sqLiteDatabase.insert(ContentProviderConfig.TABLE_NAME_COLLECTION_APP,null,values);
                returnUri=Uri.parse("content://" +ContentProviderConfig.AUTHORITY + "/" + ContentProviderConfig.TABLE_NAME_COLLECTION_APP + "/" + rowId);
                break;
            default:
                break;
        }
        //通知，数据源发生改变
        getContext().getContentResolver().notifyChange(uri,null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase=this.dataHelper.getWritableDatabase();
        int deleteRow =0;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                deleteRow =sqLiteDatabase.delete(ContentProviderConfig.TABLE_NAME_COLLECTION_APP,selection,selectionArgs);
                break;
            default:
                break;
        }
        //通知，数据源发生改变
        getContext().getContentResolver().notifyChange(uri,null);
        return deleteRow;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase sqLiteDatabase=dataHelper.getWritableDatabase();
        int updateRow=0;
        switch (uriMatcher.match(uri)){
            case TABLE_DIR:
                updateRow=sqLiteDatabase.update(ContentProviderConfig.TABLE_NAME_COLLECTION_APP,values,selection,selectionArgs);
                break;
            default:
                break;
        }
        //通知，数据源发生改变
        if(updateRow>0){
            getContext().getContentResolver().notifyChange(uri,null);
        }
        return 0;
    }
}
