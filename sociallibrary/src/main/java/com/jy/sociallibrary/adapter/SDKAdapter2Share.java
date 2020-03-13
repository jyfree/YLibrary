package com.jy.sociallibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jy.sociallibrary.R;
import com.jy.sociallibrary.bean.SDKShareChannel;

import java.util.List;


/**
 * Administrator
 * created at 2016/2/23 18:26
 * TODO:分享适配器
 */
public class SDKAdapter2Share extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<SDKShareChannel> mData;

    public SDKAdapter2Share(Context mContext, List<SDKShareChannel> mData) {
        this.mData = mData;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.social_sdk_gv_item_share, parent, false);
            holder = new ViewHolder();
            convertView.setTag(holder);


            holder.iv_icon = convertView.findViewById(R.id.social_sdk_imageView);
            holder.tv_name = convertView.findViewById(R.id.social_sdk_textView);


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.iv_icon.setImageResource(mData.get(position).getIcon());
        holder.tv_name.setText(mData.get(position).getName());
        return convertView;
    }

    static class ViewHolder {

        ImageView iv_icon;//图标
        TextView tv_name;//名字


    }

    public void dataChange(List<SDKShareChannel> dataList) {
        if (dataList != null) {
            mData = dataList;
            this.notifyDataSetChanged();
        }
    }
}
