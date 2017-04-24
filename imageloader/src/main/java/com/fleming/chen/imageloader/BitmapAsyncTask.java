package com.fleming.chen.imageloader;

import android.graphics.Bitmap;
import android.os.AsyncTask;

public class BitmapAsyncTask extends AsyncTask<String, Void, Bitmap> {

    private BitmapCallBack mCallBack;

    public BitmapAsyncTask(BitmapCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        //从网络上获取到的图片存到缓存中
        return BitmapUtils.getBitmapFromUrl(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (mCallBack != null) {
            mCallBack.onComplete(bitmap);
        }
    }

    public interface BitmapCallBack {
        void onComplete(Bitmap bitmap);
    }
}

