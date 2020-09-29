package dhu.cst.yinqingbo416.test0521;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ImageCompare {
    //获取匹配结果的下标
    public static int getImgIndex(Context context,Bitmap img){
        double maxSimilarity;
        int maxIndex;
        //获取图片的指纹序列
        int[] pixels0 = getImgPixels(img);
        //获取匹配图1的指纹序列
        Bitmap bitmap1 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.v1);
        int[] pixels1 = getImgPixels(bitmap1);
        //计算图片相似度
        double similary = getSimilarity(pixels0,pixels1);
        if(similary > 0.8){
            return 1;
        }
        maxSimilarity = similary;
        maxIndex = 1;
        //获取匹配图2的指纹序列
        Bitmap bitmap2 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.v2);
        int[] pixels2 = getImgPixels(bitmap2);
        //计算图片相似度
        similary = getSimilarity(pixels0,pixels2);
        if(similary > 0.8){
            return 2;
        }
        if(similary > maxSimilarity){
            maxIndex = 2;
            maxSimilarity = similary;
        }
        //获取匹配图3的指纹序列
        Bitmap bitmap3 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.v3);
        int[] pixels3 = getImgPixels(bitmap3);
        //计算图片相似度
        similary = getSimilarity(pixels0,pixels3);
        if(similary > 0.8){
            return 3;
        }
        if(similary > maxSimilarity){
            maxIndex = 3;
            maxSimilarity = similary;
        }
        //获取匹配图4的指纹序列
        Bitmap bitmap4 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.v4);
        int[] pixels4 = getImgPixels(bitmap4);
        //计算图片相似度
        similary = getSimilarity(pixels0,pixels4);
        if(similary > 0.8){
            return 4;
        }
        if(similary > maxSimilarity){
            maxIndex = 4;
            maxSimilarity = similary;
        }
        //获取匹配图5的指纹序列
        Bitmap bitmap5 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.v5);
        int[] pixels5 = getImgPixels(bitmap5);
        //计算图片相似度
        similary = getSimilarity(pixels0,pixels5);
        if(similary > 0.8){
            return 5;
        }
        if(similary > maxSimilarity){
            maxIndex = 5;
            maxSimilarity = similary;
        }
        //获取匹配图6的指纹序列
        Bitmap bitmap6 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.v6);
        int[] pixels6 = getImgPixels(bitmap6);
        //计算图片相似度
        similary = getSimilarity(pixels0,pixels6);
        if(similary > 0.8){
            return 6;
        }
        if(similary > maxSimilarity){
            maxIndex = 6;
            maxSimilarity = similary;
        }
        //获取匹配图7的指纹序列
        Bitmap bitmap7 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.v7);
        int[] pixels7 = getImgPixels(bitmap7);
        //计算图片相似度
        similary = getSimilarity(pixels0,pixels7);
        if(similary > 0.8){
            return 7;
        }
        if(similary > maxSimilarity){
            maxIndex = 7;
            maxSimilarity = similary;
        }
        //获取匹配图8的指纹序列
        Bitmap bitmap8 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.v8);
        int[] pixels8 = getImgPixels(bitmap8);
        //计算图片相似度
        similary = getSimilarity(pixels0,pixels8);
        if(similary > 0.8){
            return 1;
        }
        if(similary > maxSimilarity){
            maxIndex = 1;
            maxSimilarity = similary;
        }
        //获取匹配图9的指纹序列
        Bitmap bitmap9 = BitmapFactory.decodeResource(context.getResources(),R.mipmap.v9);
        int[] pixels9 = getImgPixels(bitmap9);
        //计算图片相似度
        similary = getSimilarity(pixels0,pixels9);
        if(similary > 0.8){
            return 7;
        }
        if(similary > maxSimilarity){
            maxIndex = 7;
            maxSimilarity = similary;
        }
        return maxIndex;
    }

    //计算图片指纹序列
    private static int[] getImgPixels(Bitmap img){
        //将图片缩放到8X8尺寸
        Bitmap bitmap8 = ThumbnailUtils.extractThumbnail(img,8,8);
        //将图片简化成64级灰度图像
        bitmap8 = convertGreyImg(bitmap8);
        //得到灰度像素数组
        int width = bitmap8.getWidth();
        int height = bitmap8.getHeight();
        int[]pixels = new int[width*height];
        bitmap8.getPixels(pixels,0,width,0,0,width,height);
        //计算平均灰度值
        int averageColor = getAverageOfPixelArray(pixels);
        //获取灰度像素的比较数组(即图像指纹序列)
        pixels = getPixelDeviateWeightsArray(pixels, averageColor);
        return pixels;
    }
    //计算图片相似度
    private static double getSimilarity(int[]a,int[]b){
        int hanMing = getHanmingDistance(a,b);
        return calSimilarity(hanMing);
    }
    //将图片转化为64级灰度图
    private static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width*height];
        img.getPixels(pixels,0,width,0,0,width,height);
        int alpha = 0xFF << 24;
        for(int i = 0; i < height;i++){
            for(int j = 0;j < width;j++){
                int original = pixels[width*i+j];
                int red = ((original & 0x00FF0000) >> 16);
                int green = ((original & 0x00FF0000) >> 8);
                int blue = (original & 0x000000FF);
                int grey = (int)((float)red * 0.3 + (float)green*0.59 + (float)blue * 0.11);
                grey = alpha | (grey << 16) | (grey << 8) | grey;
                pixels[width * i + j] = grey;
            }
        }
        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }
    //计算平均灰度值
    private static int getAverageOfPixelArray(int[] pixels) {
        int average = 0;
        for(int i:pixels){
            average += i;
        }
        return average / pixels.length;
    }
    //获取灰度图像素的比较数组
    public static int[] getPixelDeviateWeightsArray(int[] pixels,final int averageColor) {
        int[] dest = new int[pixels.length];
        for(int i = 0;i < pixels.length;i++) {
            dest[i] = pixels[i] > averageColor ? 1: 0;
        }
        return dest;
    }
    //获取两个缩略图的平均像素比较数组的汉明距离
    public static int getHanmingDistance(int[] a,int[] b) {
        int sum = 0;
        for(int i = 0;i < a.length;i++) {
            sum += a[i] == b[i] ? 0 : 1;
        }
        return sum;
    }
    //通过汉明距离计算相似度
    public static double calSimilarity(int hanmingDistance) {
        double length = 8.0 * 8.0;
        double similarity = (length - hanmingDistance) / length;
//        //使用指数曲线调整相似度结果
//        similarity = java.lang.Math.pow(similarity, 2);
        return similarity;
    }
}
