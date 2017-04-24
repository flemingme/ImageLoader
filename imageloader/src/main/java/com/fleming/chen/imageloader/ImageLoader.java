package com.fleming.chen.imageloader;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

/**
 * ImageLoader
 * Created by Fleming on 2016/6/9.
 */
public class ImageLoader {

    private static final String TAG = "ImageLoader";
    private static ImageLoader sLoader;
    private LruCache<String, Bitmap> mCache;

    public static ImageLoader with() {
        if (sLoader == null) {
            synchronized (ImageLoader.class) {
                if (sLoader == null) {
                    sLoader = new ImageLoader();
                }
            }
        }
        return sLoader;
    }

    /**
     * 利用Runtime运行时来获取最大的内存大小
     */
    public ImageLoader() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;  //官方推荐1/8，可根据要缓存的图片大小进行调整
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                Log.d(TAG, "sizeOf: "+value.getByteCount());
                return value.getByteCount() / 1024;
            }
        };
    }

    /**
     * 将图片添加到缓存中，以url为key，以bitmap为value;
     * @param key
     * @param bitmap
     */
    public void addBitmapToCache(String key, Bitmap bitmap) {
        if (getBitmapFromCache(key) == null) {
            mCache.put(key, bitmap);
        }
    }

    /**
     * 从缓存中获取图片
     * @param key
     * @return
     */
    public Bitmap getBitmapFromCache(String key) {
        return !TextUtils.isEmpty(key) ? mCache.get(key) : null;
    }

    /**
     * 从缓存中删除图片
     * @param url
     */
    public void removeBitmapFromCache(String url) {
        if (!TextUtils.isEmpty(url)) {
            mCache.remove(url);
        }
    }

    /**
     * 利用异步工具类AsyncTask来展示icon;
     * @param imageView
     * @param url
     * @param round
     * @param roundPx
     */
    public void showImage(final ImageView imageView, final String url,
                          final boolean round, final float roundPx) {
        Bitmap bitmap = getBitmapFromCache(url);//先从缓存中获取
        //如果缓存中存在就直接设置给imageView，否则就去下载
        if (bitmap != null) {
            //缓存大小要设置的合理一些，否则有的图片无法进入缓存，则需要通过网络获取
            imageView.setImageBitmap(bitmap);
            Log.d(TAG, "showImage: from cache");
        } else {
            new BitmapAsyncTask(new BitmapAsyncTask.BitmapCallBack() {
                @Override
                public void onComplete(Bitmap bitmap) {
                    if (imageView.getTag().equals(url)) {
                        if (round) {
                            bitmap = BitmapUtils.getRoundedCornerBitmap(bitmap, roundPx);
                        }
                        imageView.setImageBitmap(bitmap);
                        addBitmapToCache(url, bitmap);
                    }
                }
            }).execute(url);
            Log.d(TAG, "showImage: from net");
        }
    }
}
