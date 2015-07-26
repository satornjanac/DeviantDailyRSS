package com.arm.satornjanac.deviantdaily;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

public class PhotoSlidePageFragment extends Fragment {

    public static final String ARG_PHOTO_URL = "page";
    public static final String ARG_POSITION = "position";

    private String mPhotoUrl;
    private int mPositionInAdapter;

    public static PhotoSlidePageFragment newInstance(String photoDetail, int position) {
        PhotoSlidePageFragment fragment = new PhotoSlidePageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PHOTO_URL, photoDetail);
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    public PhotoSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhotoUrl = getArguments().getString(ARG_PHOTO_URL);
        mPositionInAdapter = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater
                .inflate(R.layout.fragment_photo_slide_page, container, false);

        final ImageView networkImageView = (ImageView) rootView.findViewById(
                R.id.networkImageDetailView);
        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!TextUtils.isEmpty(mPhotoUrl)) {
            imageLoader.displayImage(mPhotoUrl, networkImageView);
        } else {
            networkImageView.setImageResource(android.R.drawable.stat_notify_error);
        }

        networkImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Utils.displayPopupWindow((DataSetObservableActivity) getActivity(), view, true,
                        mPositionInAdapter);
                return false;
            }
        });

        return rootView;
    }
}