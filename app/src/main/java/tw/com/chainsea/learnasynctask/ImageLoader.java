package tw.com.chainsea.learnasynctask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
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

    /**
     * 处理子线程发送来的消息，这里是获取bitmap;
      */
    private Handler mHandler = new Handler(){
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
                Bitmap bitmap = getBitmap(mUrl);
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
        mImageView = imageView;
        mUrl = url;

        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... params) {
                return getBitmap(params[0]);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                mImageView.setImageBitmap(bitmap);
            }
        }.execute(url);
    }

    /**
     * 通过HttpURLConnection获取icon的bitmap;
     * @param urlString
     * @return
     */
    private Bitmap getBitmap(String urlString) {
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

}
