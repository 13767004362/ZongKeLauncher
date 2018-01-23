package com.zongke.hapilolauncher.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;



/**
 * Created by ${xinGen} on 2018/1/17.
 */

public class TypefaceUtils {
    private static TypefaceUtils instance;
    /**
     * 站酷快乐体
     */
  public static final String  font_standing_cool="fonts/站酷快乐体2016修订版.ttf";
    /**
     * 思源 Medium字体
     */
    public static final String  font_roboto_mdium="fonts/Roboto-Medium.ttf";
    /**
     * 思源 Bold字体
     */
    public static final String  font_roboto_bold="fonts/Roboto-Bold.ttf";
    /**
     * 用于存储，加载过得字体
     */
    private Map<String,Typeface> typefaceMap;
    private TypefaceUtils(){
          this.typefaceMap=new HashMap<>();
    }
    public static synchronized TypefaceUtils getInstance(){
        if (instance==null){
            instance=new TypefaceUtils();
        }
        return instance;
    }
    /**
     * 从assert文件下获取字体
     */

   public  Typeface getTypeFace(Context  context, String typeFaceName){
        if (this.typefaceMap.get(typeFaceName)==null){
            this.typefaceMap.put(typeFaceName,Typeface.createFromAsset(context.getAssets(),typeFaceName));
        }
        return   this.typefaceMap.get(typeFaceName);
    }
    public void setCustomFont(TextView textView,String typeFaceName){
        textView.setTypeface( getTypeFace(textView.getContext(),typeFaceName));
    }
}
