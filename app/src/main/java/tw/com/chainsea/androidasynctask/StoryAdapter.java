package tw.com.chainsea.androidasynctask;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fleming.chen.imageloader.ImageLoader;

import java.util.LinkedList;
import java.util.List;

/**
 * StoryAdapter
 * Created by Fleming on 2016/6/9.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private List<Story.StoriesBean> mStoryList = new LinkedList<>();

    public Story.StoriesBean getItem(int position) {
        return mStoryList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_story, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url = getItem(position).getImages().get(0);
        //第三个参数，true加载一个圆角图，false则为方图;最后一个参数是圆角值
        ImageLoader.with().showImage(holder.ivIcon, url, true, 16f);
        holder.ivIcon.setTag(url);//为icon设置tag
        holder.tvTitle.setText(getItem(position).getTitle());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mStoryList != null ? mStoryList.size() : 0;
    }

    public void addListData(List<Story.StoriesBean> storys) {
        mStoryList = storys;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView ivIcon;
        TextView tvTitle;

        public ViewHolder(View itemView){
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv_icon);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        }
    }
}
