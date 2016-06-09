package tw.com.chainsea.learnasynctask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * NewAdapter
 * Created by Fleming on 2016/6/9.
 */
public class NewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<NewEntity> mList;

    public NewAdapter(Context context, List<NewEntity> newEntities) {
        mInflater = LayoutInflater.from(context);
        mList = newEntities;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public NewEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_new, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.ivIcon.setImageResource(R.mipmap.ic_launcher);
//        new ImageLoader().showImageByThread(holder.ivIcon,getItem(position).getIcon());
        new ImageLoader().showImageByAsyncTask(holder.ivIcon,getItem(position).getIcon());
        holder.tvTitle.setText(getItem(position).getTitle());
        holder.tvContent.setText(getItem(position).getContent());
        return convertView;
    }

    static class ViewHolder{
        ImageView ivIcon;
        TextView tvTitle,tvContent;
        public ViewHolder(View view){
            ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
            tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvContent = (TextView) view.findViewById(R.id.tv_content);
        }
    }
}
