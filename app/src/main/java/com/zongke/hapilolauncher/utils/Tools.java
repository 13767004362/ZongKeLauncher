package com.zongke.hapilolauncher.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.qd.croe.BarcodeFormat;
import com.qd.croe.EncodeHintType;
import com.qd.croe.WriterException;
import com.qd.croe.common.BitMatrix;
import com.qd.croe.qrcode.QRCodeWriter;
import com.qd.croe.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

/**
 * Created by llj on 2017/9/6.
 */

public class Tools {

    /**
     * 将秒的数值转换成时间格式
     *
     * @param count 秒的数值
     * @return
     */
    public static String formatTime(int count) {
        if(count >= 3600) return "超过范围了";
        StringBuilder builder = new StringBuilder();
        if (count < 60) {
            if (count < 10) {
                builder.append("00:0").append(count);
            } else {
                builder.append("00:").append(count);
            }
        } else {
            // 分钟
            int minu = (int) Math.floor(count / 60);
            // 秒
            int second = count % 60;
            if (minu < 10) {
                builder.append("0").append(minu).append(":");
            } else {
                builder.append(minu).append(":");
            }

            if(second < 10){
                builder.append("0").append(second);
            }else {
                builder.append(second);
            }
        }
        return builder.toString();
    }


    /**
     * 生成二维码
     *
     * @param url    网络链接地址或者文本
     * @param width  二维码宽度
     * @param height 二维码高度
     * @return
     */
    public static Bitmap createQRImage(String url, int width, int height) {
//        int QR_WIDTH = width;
//        int QR_HEIGHT = height;
        try {
            // 判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1) {
                return null;
            }
            // 用于设置QR二维码参数
            Hashtable<EncodeHintType, Object> qrParam = new Hashtable<EncodeHintType, Object>();
            // 设置QR二维码的纠错级别——这里选择最高H级别
            qrParam.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 设置编码方式
            qrParam.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            // Hashtable<EncodeHintType, String> hints = new
            // Hashtable<EncodeHintType, String>();
            // hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                    BarcodeFormat.QR_CODE, width, height, qrParam);
            int[] pixels = new int[width * height];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    } else {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height,
                    Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 在二维码上绘制头像
     */
    public static void createQRCodeBitmapWithPortrait(int qecode_w, int qecode_h, Bitmap qr, Bitmap portrait) {
        // 头像图片的大小
        int portrait_W = portrait.getWidth();
        int portrait_H = portrait.getHeight();

        // 设置头像要显示的位置，即居中显示
        int left = (qecode_w - portrait_W) / 2;
        int top = (qecode_h - portrait_H) / 2;
        int right = left + portrait_W;
        int bottom = top + portrait_H;
        Rect rect1 = new Rect(left, top, right, bottom);

        // 取得qr二维码图片上的画笔，即要在二维码图片上绘制我们的头像
        Canvas canvas = new Canvas(qr);

        // 设置我们要绘制的范围大小，也就是头像的大小范围
        Rect rect2 = new Rect(0, 0, portrait_W, portrait_H);
        // 开始绘制
        canvas.drawBitmap(portrait, rect2, rect1, null);
    }
}
