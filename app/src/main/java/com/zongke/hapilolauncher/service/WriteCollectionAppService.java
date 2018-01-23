package com.zongke.hapilolauncher.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.zongke.hapilolauncher.db.Data;
import com.zongke.hapilolauncher.library.sqlbrite.SQLBriteProvider;

/**
 * Created by ${xinGen} on 2017/10/28.
 * <p>
 * 将收藏的App信息写入数据库中
 */

public class WriteCollectionAppService extends IntentService {
    public static final String TAG = WriteCollectionAppService.class.getSimpleName();
    private static final String ACTION = "com.zongke.hapilolauncher.service.WriteCollectionAppService";
    public static void startWriteCollectionAppService(Context context, Bundle bundle) {
        Intent intent = new Intent();
        intent.setAction(ACTION);
        intent.putExtras(bundle);
        context.startService(intent);
    }
    public WriteCollectionAppService() {
        super(TAG);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getExtras() != null && intent.getExtras().containsKey(TAG)) {
            String packName = intent.getExtras().getString(TAG);
            Log.i(TAG, "WriteCollectionAppService  onHandleIntent "+packName);
            writeData(packName);
        }
    }

    /**
     * 先查询数据，后插入
     *
     * @param packageName
     */
    private void writeData(String packageName) {
        BriteDatabase briteDatabase = SQLBriteProvider.getInstance(this).getBriteDatabase();
        Cursor cursor = null;
        try {
            cursor = briteDatabase.query(createSelectSQL(), new String[]{packageName});
            boolean isExist = cursor != null && cursor.moveToFirst() ? true : false;
            if (!isExist) {
                briteDatabase.insert(Data.TABLE_NAME_COLLECTION_APP, createContentValues(packageName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 创建查询语句
     *
     * @return
     */
    private String createSelectSQL() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("select * from ");
        stringBuilder.append(Data.TABLE_NAME_COLLECTION_APP);
        stringBuilder.append(" where ");
        stringBuilder.append(Data.COLUMN_NAME_APP_PACAKAGE);
        stringBuilder.append(" =? ");
        return stringBuilder.toString();
    }

    private ContentValues createContentValues(String packName) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Data.COLUMN_NAME_APP_PACAKAGE, packName);
        return contentValues;
    }

}
