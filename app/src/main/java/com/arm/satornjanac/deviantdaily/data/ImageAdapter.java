package com.arm.satornjanac.deviantdaily.data;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.arm.satornjanac.deviantdaily.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private final List photoDetails;

    public ImageAdapter(Context context, List photoDetails) {
        this.context = context;
        this.photoDetails = photoDetails;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rootView = convertView;
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.image_adapter_item, null);
            // configure view holder
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) rootView.findViewById(R.id.networkImageView);
            rootView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rootView.getTag();

        PhotoDetails photoDetails = (PhotoDetails) getItem(position);
        String thumbUrl = photoDetails.getPhotoUrl();
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!TextUtils.isEmpty(thumbUrl)) {
            imageLoader.displayImage(thumbUrl, holder.imageView);
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
