package com.zongke.hapilolauncher.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by ${xingen} on 2017/6/22.
 */

public class BitmapUtils {

    /**
     * 绘画一个圆形的bitmap
     * 思路：
     * 1.创建一个以bitmap作为画布
     * 2.通过画笔画一个圆形
     * 3.通过交集方(SRC_IN)式画一个bitmap
     *
     * @param bitmap
     * @return
     */
    public static Bitmap drawnRoundBitmap(Bitmap bitmap,Bitmap bgBitmap){
        //设置正方形的边长：
        int r=0;
        Bitmap roundbitmap=null;
        try{
            if(bitmap!=null){
                roundbitmap=Bitmap.createBitmap(  bgBitmap.getWidth(), bgBitmap.getHeight(), Bitmap.Config.ARGB_8888);
                //bitmap作为画布
                Canvas canvas=new Canvas(roundbitmap);
                //创建画笔
                Paint paint=new Paint();
                //抗锯齿
                paint.setAntiAlias(true);

                paint.setColor(Color.WHITE);
                if (bitmap==null){
//                    Bitmap defaultBitmap=BitmapUtils.decodeBitmapResource(BaseApplication.Companion.getInstance(),)
                    //按照外边框实际大小来绘制外边
                    r= bgBitmap.getWidth()< bgBitmap.getHeight()? bgBitmap.getWidth(): bgBitmap.getHeight();
                    RectF rectF=new RectF(0,0,r,r);
                    // 通过制定的rect画一个圆角矩形，当圆角X轴方向的半径等于Y轴方向的半径时,且都等于r/2时，画出来的圆角矩形就是圆形
                    canvas.drawRoundRect(rectF,r/2,r/2,paint);
                    //再画圆形边框
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                    canvas.drawBitmap(bgBitmap,null,rectF,paint);
                }else{
                    //按照原始尺寸绘制,外边框
                    r= bgBitmap.getWidth()< bgBitmap.getHeight()? bgBitmap.getWidth(): bgBitmap.getHeight();
                    RectF rectF=new RectF(0,0,r,r);
                    canvas.drawRoundRect(rectF,r/2,r/2,paint);
                    int r1=bitmap.getWidth()<bitmap.getHeight()?bitmap.getWidth():bitmap.getHeight();
                    if (r1<r){
                        //绘制bitmap
                        Rect dst =new Rect();
                        dst.top=(r-bitmap.getHeight())/2;
                        dst.left= (r-bitmap.getWidth())/2;
                        dst.bottom= dst.top+bitmap.getHeight();
                        dst.right= dst.left+bitmap.getWidth();
                        canvas.drawBitmap(bitmap,null,dst,paint);
                    }else{
                        //设置两种图片相交模式：SRC_IN为取SRC图形相交的部分，多余的将被去掉
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN ));
                        canvas.drawBitmap(bitmap,null,new RectF(0,0,r1,r1),paint);
                    }
                    //再画圆形边框
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
                    canvas.drawBitmap(bgBitmap,null,rectF,paint);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(bitmap!=null){
                bitmap.recycle();
            }
        }
        return roundbitmap;
    }

    /**
     * 加载原始尺寸的Bitmap
     * @param context
     * @param bitmapId
     * @return
     */
public  static Bitmap decodeBitmapResource(Context context, int bitmapId){
      return   BitmapFactory.decodeResource(context.getResources(),bitmapId,new BitmapFactory.Options());
    }

}
