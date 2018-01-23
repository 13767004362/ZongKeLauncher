package com.zongke.hapilolauncher.db;

import android.provider.BaseColumns;




/**
 * Created by ${xinGen} on 2018/1/17.
 */

public final class Data implements BaseColumns {
    public static final String SQLITE_NAME = "HapiloLaucncher.db";
    public static final int SQLITE_VERSON = 1;

    public static final String TABLE_NAME_COLLECTION_APP = "collection_app";

    public static final String COLUMN_NAME_APP_PACAKAGE = "appPackage";

    public static final String CREATE_TABLE_COLLECTION_APP = "create table " + TABLE_NAME_COLLECTION_APP + "(" + "_id" + " integer primary key autoincrement," + "appPackage" + " text ," + "appPosition" + " integer , " + "appVisible" + " integer" + ")";

}
