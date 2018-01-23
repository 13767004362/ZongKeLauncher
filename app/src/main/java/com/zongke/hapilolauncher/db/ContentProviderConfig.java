package com.zongke.hapilolauncher.db;

import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by ${xinGen} on 2017/11/2.
 *
 *  ContentProvider配置类
 */

public final class ContentProviderConfig  {
    /**
     * ContentProvider的authorities
     */
    public static final  String AUTHORITY="com.zongke.hapilolauncher.db.DataContentProvider";
    /**
     * Scheme
     */
    public static final String SCHEME="content";
    /**
     *  ContentProvider的URI
     */
    public static final Uri CONTENT_URI=Uri.parse(SCHEME+"://"+AUTHORITY);

    public static final  String TABLE_NAME_COLLECTION_APP="collection_app";
    public static final  String TABLE_NAME_ALL_INSTANCE_APP="all_instance_app";
    /**
     * collection app表的URI
     */
    public static final Uri URI_COLLECTION_APP=Uri.withAppendedPath(CONTENT_URI,TABLE_NAME_COLLECTION_APP);
    public static final Uri URI_ALL_INSTANCE_APP=Uri.withAppendedPath(CONTENT_URI,TABLE_NAME_COLLECTION_APP);
    /**
     * 收藏程序的包名
     */
    public static final String COLUMN_NAME_APP_PACAKAGE="appPackage";
    /**
     * 收藏程序的位置
     */
    public static final  String COLUMN_NAME_APP_POSTION="appPosition";
    /**
     * 收藏程序的显示： true 0,false 1
     */
    public static final  String COLUMN_NAME_APP_VISIBLE="appVisible";

    /**
     * 从索引值1开始检索15个数据
     */
    private  static final String LIMIT_SQL=" LIMIT 8 OFFSET 0 ";
    /**
     * 从第一行开始，获取8个数据
     *
     * @return
     */
    public static String createQuery(String selection ) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" select * from ");
        stringBuilder.append(Data.TABLE_NAME_COLLECTION_APP);
        if (!TextUtils.isEmpty(selection)){
            stringBuilder.append(" where ");
            stringBuilder.append(selection);
        }
        stringBuilder.append(" ORDER BY ");
        stringBuilder.append(ContentProviderConfig.COLUMN_NAME_APP_POSTION);
        stringBuilder.append(" ASC ");
        stringBuilder.append(LIMIT_SQL);
        return stringBuilder.toString();
    }
}
