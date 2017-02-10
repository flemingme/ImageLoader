package tw.com.chainsea.learnasynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ImageLoader
 * Created by Fleming on 2016/6/9.
 */
public class ImageLoader {

    private ImageView mImageView;
    private String mUrl;
    private LruCache<String, Bitmap> mCache;
    private static ImageLoader sLoader;
    private static final String TAG = "ImageLoader";

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

    public ImageLoader() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 2;  //根据要缓存的图片大小进行调整
        mCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                Log.d(TAG, "sizeOf: "+value.getByteCount());
                return value.getByteCount();
            }
        };
    }

    /**
     * 将图片添加到缓存中，以url为key，以bitmap为value;
     * @param url
     * @param bitmap
     */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (getBitmapFromCache(url) == null) {
            mCache.put(url, bitmap);
        }
    }

    /**
     * 从缓存中获取图片
     * @param url
     * @return
     */
    public Bitmap getBitmapFromCache(String url) {
        return mCache.get(url);
    }

    /**
     * 处理子线程发送来的消息，这里是获取bitmap;
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bitmap bitmap = (Bitmap) msg.obj;
            mImageView.setImageBitmap(bitmap);
        }
    };

    /**
     * 利用多线程的方式来显示icon;
     * @param imageView
     * @param url
     */
    public void showImageByThread(ImageView imageView, String url) {
        mImageView = imageView;
        mUrl = url;

        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = getBitmapFromUrl(mUrl);
                Message message = mHandler.obtainMessage();
                message.obj = bitmap;
                mHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 利用异步工具类AsyncTask来展示icon;
     * @param imageView
     * @param url
     */
    public void showImageByAsyncTask(ImageView imageView, String url) {
        Bitmap bitmap = getBitmapFromCache(url);//先从缓存中获取
        //如果缓存中存在就直接设置给imageView，否则就去下载
        if (bitmap != null) {
            //缓存大小要设置的合理一些，否则有的图片无法进入缓存，则需要通过网络获取
            imageView.setImageBitmap(bitmap);
            Log.d(TAG, "showImageByAsyncTask: from cache");
        } else {
            new ImgLoaderAsyncTask(imageView, url).execute(url);
            Log.d(TAG, "showImageByAsyncTask: from net");
        }
    }

    /**
     * 通过HttpURLConnection获取icon的bitmap;
     * @param urlString
     * @return
     */
    private Bitmap getBitmapFromUrl(String urlString) {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            is = new BufferedInputStream(urlConnection.getInputStream());
            bitmap = BitmapFactory.decodeStream(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    class ImgLoaderAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;
        private String mUrl;

        public ImgLoaderAsyncTask(ImageView imageView, String url) {
            mImageView = imageView;
            mUrl = url;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String url = params[0];
            //从网络上获取到的图片存到缓存中
            Bitmap bitmap = getBitmapFromUrl(url);
            if (bitmap != null) {
                addBitmapToCache(url, bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (mImageView.getTag().equals(mUrl)) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    }

}
