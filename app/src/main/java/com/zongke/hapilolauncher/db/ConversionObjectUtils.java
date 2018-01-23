package com.zongke.hapilolauncher.db;

import android.database.Cursor;

import com.zongke.hapilolauncher.view.LauncherRotatingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${xingen} on 2017/10/28.
 */

public class ConversionObjectUtils {

    public static List<LauncherRotatingMenu.TestEntity> conversionEntity(Cursor cursor){
        List<LauncherRotatingMenu.TestEntity> list=new ArrayList<>();
        try{
            if (cursor!=null&&cursor.moveToFirst()){
                do {
                    LauncherRotatingMenu.TestEntity testEntity=new LauncherRotatingMenu.TestEntity();
                    testEntity.packageName=cursor.getString(cursor.getColumnIndex(Data.COLUMN_NAME_APP_PACAKAGE));
                    list.add(testEntity);
                }while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return list;
    }
}
