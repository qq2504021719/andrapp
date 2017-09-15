package com.bignerdranch.android.xundian.comm;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.util.Log;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/9/13.
 */

public class PictureUtils {

    /**
     * 确认屏幕的尺寸,然后按此缩放图像。这样,就能保证载入的ImageView永远不会过大
     * @param path
     * @param activity
     * @return
     */
    public static Bitmap getScaledBitmap(String path,Activity activity){
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(size);
        return getScaledBitmap(path,size.x,size.y);
    }



    /**
     * 图片压缩方法
     * path 图片存储路径
     * destWidth 压缩成多宽
     * destHeight 压缩成多高
     */
    public static Bitmap getScaledBitmap(String path,int destWidth,int destHeight){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        // 获取原图宽高
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        // 计算原图尺寸相对于设置下降了多少比例
        int inSampleSize = 1;
        if(srcHeight > destHeight || srcWidth > destWidth){
            if(srcWidth > srcHeight){
                inSampleSize = Math.round(srcHeight / destHeight);
            }else{
                inSampleSize = Math.round(srcWidth / destWidth);
            }
        }

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // 读取原图并创建最终的缩略图
        return BitmapFactory.decodeFile(path,options);
    }


    /**
     * 图片转为字节
     * @param path
     * @return
     */
    public static byte[] BitmapZhuanByte(String path){

        Bitmap bm = BitmapFactory.decodeFile(path);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 图片字节转为位图
     * @param bytes
     * @return
     */
    public static Bitmap ByteZhuanBitmap(byte[] bytes){
        if (bytes.length != 0) {
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } else {
            return null;
        }
    }
}
