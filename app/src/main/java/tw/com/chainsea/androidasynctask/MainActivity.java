package tw.com.chainsea.androidasynctask;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, StoryAsyncTask.StoryCallBack {

    private static final String URL = "https://news-at.zhihu.com/api/4/news/latest";
    private StoryAdapter mAdapter;
    private SwipeRefreshLayout mRefreshLayout;
    private StoryAsyncTask mStoryAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        RecyclerView rvStores = (RecyclerView) findViewById(R.id.rv_stores);
        rvStores.setLayoutManager(new LinearLayoutManager(this));
        rvStores.setHasFixedSize(true);
        mAdapter = new StoryAdapter();
        rvStores.setAdapter(mAdapter);

        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorAccent);
        mRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        downloadStory();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mStoryAsyncTask != null && !mStoryAsyncTask.isCancelled()) {
            mStoryAsyncTask.cancel(true);
        }
    }

    @Override
    public void onRefresh() {
        downloadStory();
    }

    private void downloadStory() {
        mStoryAsyncTask = new StoryAsyncTask(this);
        mStoryAsyncTask.execute(URL);
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onComplete(List<Story.StoriesBean> stories) {
        mAdapter.addListData(stories);
        mRefreshLayout.setRefreshing(false);
    }
}
