package tw.com.chainsea.learnasynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    private static final String URL = "http://192.168.0.112:8080/NewsAPI/news";
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = (ListView) findViewById(R.id.lv_news);
        new NewsAsyncTask().execute(URL);
    }

    class NewsAsyncTask extends AsyncTask<String, Void, List<NewEntity>> {

        @Override
        protected List<NewEntity> doInBackground(String... params) {
            return getJsonString(params[0]);
        }

        @Override
        protected void onPostExecute(List<NewEntity> newEntities) {
            NewAdapter adapter = new NewAdapter(MainActivity.this, newEntities);
            mListView.setAdapter(adapter);
        }
    }

    private List<NewEntity> getJsonString(String url) {
        List<NewEntity> newEntityList = new ArrayList<>();
        try {
            String jsonString = readStream(new URL(url).openStream());
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                NewEntity entity = new NewEntity();
                entity.setIcon(item.getString("icon"));
                entity.setTitle(item.getString("title"));
                entity.setContent(item.getString("content"));
                newEntityList.add(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newEntityList;
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
}
