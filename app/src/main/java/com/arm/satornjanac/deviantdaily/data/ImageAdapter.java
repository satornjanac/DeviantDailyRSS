package com.arm.satornjanac.deviantdaily.data;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.arm.satornjanac.deviantdaily.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private final List photoDetails;

    public ImageAdapter(Context context, List photoDetails) {
        this.context = context;
        this.photoDetails = photoDetails;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.image_adapter_item, null);
        }
        View rootView = convertView;
        Object tag = rootView.getTag();
        if (tag instanceof ViewHolder) {
            holder = (ViewHolder) tag;
        }
        if (holder == null) {
            holder = new ViewHolder();
            holder.imageView = (ImageView) rootView.findViewById(R.id.networkImageView);
            rootView.setTag(holder);
        }

        PhotoDetails photoDetails = (PhotoDetails) getItem(position);
        holder.imageView.setBackground(null);

        String thumbUrl = photoDetails.getThumbnailUrl();
        if (!TextUtils.isEmpty(thumbUrl)) {
            Picasso.with(context).load(thumbUrl).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(android.R.drawable.stat_notify_error);
        }

        return rootView;
    }

    @Override
    public int getCount() {
        return photoDetails != null ? photoDetails.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return photoDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public static class ViewHolder {
        ImageView imageView;
    }
}
