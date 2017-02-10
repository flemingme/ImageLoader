package tw.com.chainsea.learnasynctask;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView mRecyclerView;
    private static final String URL = "http://news-at.zhihu.com/api/4/news/latest";
    private static final String TAG = "MainActivity";
    private StoryAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        downloadStory();
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_stores);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new StoryAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        downloadStory();
    }

    private void downloadStory() {
        new StoryAsyncTask().execute(URL);
    }

    private class StoryAsyncTask extends AsyncTask<String, Integer, List<Story.StoriesBean>> {

        @Override
        protected List<Story.StoriesBean> doInBackground(String... params) {
            return getJsonString(params[0]);
        }

        @Override
        protected void onPostExecute(List<Story.StoriesBean> stories) {
            mAdapter.addListData(stories);
            mRefreshLayout.setRefreshing(false);
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
}
