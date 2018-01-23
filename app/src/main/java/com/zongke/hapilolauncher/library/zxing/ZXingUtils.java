package com.zongke.hapilolauncher.library.zxing;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.text.TextUtils;

import com.qd.croe.BarcodeFormat;
import com.qd.croe.EncodeHintType;
import com.qd.croe.common.BitMatrix;
import com.qd.croe.qrcode.QRCodeWriter;
import com.qd.croe.qrcode.decoder.ErrorCorrectionLevel;
import com.zongke.hapilolauncher.base.BaseApplication;
import com.zongke.hapilolauncher.utils.FileUtils;

import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ${xinGen} on 2017/11/15.
 */

public class ZXingUtils {
    /**
     * 生成带有Logo的二维码的Bitmap,且保存在文件中
     *
     * @param content
     * @param widthPix
     * @param heightPix
     * @param logoBm
     * @return
     */
    public static String createQRImageSaveFile(String content, int widthPix, int heightPix, Bitmap logoBm) {
       Bitmap bitmap =createQRImage(content,widthPix,heightPix,logoBm);
        if (bitmap==null){
            return null;
        }else{
         return    writeFile(bitmap);
        }
    }

    /**
     * 生成带有Logo的二维码的Bitmap
     * @param content
     * @param widthPix
     * @param heightPix
     * @param logoBm
     * @return
     */
    public static Bitmap createQRImage(String content, int widthPix, int heightPix, Bitmap logoBm) {
        Bitmap bitmap = null;
        try {
            if (TextUtils.isEmpty(content)) {
                return bitmap;
            }
            QRCodeWriter writer = new QRCodeWriter();
            Map<EncodeHintType, Object> hintsMap = new HashMap<>();
            hintsMap.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 容错级别
            hintsMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE,
                    widthPix, heightPix, hintsMap);
            int[] pixels = new int[widthPix * heightPix];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < heightPix; y++) {
                for (int x = 0; x < widthPix; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * widthPix + x] = 0xff000000;
                    } else {
                        pixels[y * widthPix + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap codeBitmap = Bitmap.createBitmap(widthPix, heightPix,
                    Bitmap.Config.ARGB_8888);
            codeBitmap.setPixels(pixels, 0, widthPix, 0, 0, widthPix, heightPix);
            Bitmap logCodeBitmap = null;
            if (logoBm != null) {
                logCodeBitmap = addLogo(codeBitmap, logoBm);
            }
            if (logCodeBitmap != null) {
                bitmap = logCodeBitmap;
                if (logoBm != null) {
                    logoBm.recycle();
                }
                if (codeBitmap!=null){
                    codeBitmap.recycle();
                }
            }else{//绘制带有Logo的二维码失败
                bitmap=codeBitmap;
                if (logoBm != null) {
                    logoBm.recycle();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 写入磁盘中，返回图片文件路径
     *
     * @param bitmap
     * @return
     */
    private static String writeFile(Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        String filePath = null;
        try {
            filePath = FileUtils.getBitmapDiskFile(BaseApplication.getInstance());
            fileOutputStream = new FileOutputStream(filePath);
            // 必须使用compress方法将bitmap保存到文件中再进行读取。直接返回的bitmap是没有任何压缩的，内存消耗巨大！
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
            filePath = null;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (bitmap != null) {
                    bitmap.recycle();
                }
            } catch (Exception e2) {
            }
        }
        return filePath;
    }

    /**
     * 采用Canvas绘制bitmap，形成叠加效果的图片
     * @param src
     * @param logo
     * @return
     */
    private static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }
        if (logo == null) {
            return src;
        }
        // 获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();
        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }
        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        // logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);
            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }
        return bitmap;
    }
}
