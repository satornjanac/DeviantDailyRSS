package com.arm.satornjanac.deviantdaily;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class PhotoSlidePageFragment extends Fragment {

    public static final String ARG_PHOTO_URL = "page";
    public static final String ARG_POSITION = "position";

    private String mPhotoUrl;
    private int mPositionInAdapter;
    private LinearLayout mColorPalette;

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
        mColorPalette = (LinearLayout) rootView.findViewById(R.id.palletColor);
        if (!TextUtils.isEmpty(mPhotoUrl)) {
            Picasso.with(getActivity()).load(mPhotoUrl).into(networkImageView, new com.squareup.picasso.Callback() {
                @Override
                public void onSuccess() {
                    Bitmap loadedImage = ((BitmapDrawable)networkImageView.getDrawable()).getBitmap();
                    Palette.generateAsync(loadedImage, new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette p) {
                            List<Palette.Swatch> swatches = new ArrayList<Palette.Swatch>();
                            swatches.addAll(p.getSwatches());
                            Collections.sort(swatches, PALLETE_ORDER);
                            ImageView[] listOfColorImages = new ImageView[6];
                            listOfColorImages[0] = (ImageView)mColorPalette.findViewById(R.id.color1);
                            listOfColorImages[1] = (ImageView)mColorPalette.findViewById(R.id.color2);
                            listOfColorImages[2] = (ImageView)mColorPalette.findViewById(R.id.color3);
                            listOfColorImages[3] = (ImageView)mColorPalette.findViewById(R.id.color4);
                            listOfColorImages[4] = (ImageView)mColorPalette.findViewById(R.id.color5);
                            listOfColorImages[5] = (ImageView)mColorPalette.findViewById(R.id.color6);
                            int endOfLoop = swatches.size() < 6 ? swatches.size() : 6;
                            for (int i = 0; i < endOfLoop; i++) {
                                listOfColorImages[i].setBackgroundColor(swatches.get(i).getRgb());
                            }
                            mColorPalette.setVisibility(View.VISIBLE);
                        }
                    });
                }

                @Override
                public void onError() {
                    networkImageView.setImageResource(android.R.drawable.stat_notify_error);
                }
            });

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

    static final Comparator<Palette.Swatch> PALLETE_ORDER =
            new Comparator<Palette.Swatch>() {
                public int compare(Palette.Swatch e1, Palette.Swatch e2) {

                    return (e2.getPopulation() < e1.getPopulation() ? -1 :
                            (e1.getPopulation() == e2.getPopulation() ? 0 : 1));
                }
            };

}