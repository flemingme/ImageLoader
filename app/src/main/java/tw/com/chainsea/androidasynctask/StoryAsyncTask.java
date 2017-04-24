package tw.com.chainsea.androidasynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

/**
 * StoryAsyncTask
 * Created by fleming on 17-4-24.
 */

public class StoryAsyncTask extends AsyncTask<String, Integer, List<Story.StoriesBean>> {

    private final static String TAG = "StoryAsyncTask";
    private StoryCallBack mCallBack;

    public StoryAsyncTask(StoryCallBack callBack) {
        mCallBack = callBack;
    }

    @Override
    protected List<Story.StoriesBean> doInBackground(String... params) {
        return getJsonString(params[0]);
    }

    @Override
    protected void onPostExecute(List<Story.StoriesBean> stories) {
        if (mCallBack != null) {
            mCallBack.onComplete(stories);
        }
    }

    private List<Story.StoriesBean> getJsonString(String url) {
        String jsonString = null;
        try {
            jsonString = readStream(new URL(url).openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        Story story = gson.fromJson(jsonString, Story.class);
        if (story != null) {
            return story.getStories();
        }
        return null;
    }

    private String readStream(InputStream is) {
        String result = "";
        try {
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader bfr = new BufferedReader(isr);
            String line;
            while ((line = bfr.readLine()) != null) {
                result += line;
            }
            Log.d(TAG, "readStream: " + result);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public interface StoryCallBack {
        void onComplete(List<Story.StoriesBean> stories);
    }
}
