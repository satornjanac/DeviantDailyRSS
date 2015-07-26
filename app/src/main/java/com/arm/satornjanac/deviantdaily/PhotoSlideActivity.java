package com.arm.satornjanac.deviantdaily;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.arm.satornjanac.deviantdaily.animations.ZoomPageTransformer;
import com.arm.satornjanac.deviantdaily.data.ImageAdapter;
import com.arm.satornjanac.deviantdaily.data.PhotoDetails;
import com.arm.satornjanac.deviantdaily.data.PhotoDetailsCache;

public class PhotoSlideActivity extends DataSetObservableActivity {

    private static final int NUM_PAGES = PhotoDetailsCache.getPhotoCacheSize();

    private ViewPager mPager;

    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_slide);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(getIntent().getIntExtra("imagePosition", 0));
        mPager.setPageTransformer(true, new ZoomPageTransformer());
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            PhotoDetails photoDetails = PhotoDetailsCache.getPhotoDetails(position);
            return PhotoSlidePageFragment.newInstance(photoDetails.getPhotoUrl(), position);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onDataSetChanged() {
        if (mPagerAdapter != null) {
            mPagerAdapter.notifyDataSetChanged();
        }
    }

}