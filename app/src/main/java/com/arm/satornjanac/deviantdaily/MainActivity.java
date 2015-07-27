package com.arm.satornjanac.deviantdaily;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.arm.satornjanac.deviantdaily.data.ImageAdapter;
import com.arm.satornjanac.deviantdaily.data.PhotoDetails;
import com.arm.satornjanac.deviantdaily.services.DeviantRSSDownloadAndFileLoadTask;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MainActivity extends DataSetObservableActivity {

    private GridView mGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new DeviantRSSDownloadAndFileLoadTask(this).execute();
    }

    public void setUpPhotoGrid(List<PhotoDetails> list) {
        mGridView = (GridView) findViewById(R.id.photogrid);
        mGridView.setAdapter(new ImageAdapter(this, list));
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, PhotoSlideActivity.class);
                i.putExtra("imagePosition", position);
                startActivity(i);
            }
        });
        mGridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long
                    id) {
                Utils.displayPopupWindow(MainActivity.this, view, false, position);
                return true;
            }
        });
        mGridView.setOnScrollListener(new GridScrollListener(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataSetChanged() {
        if (mGridView != null) {
            ((ImageAdapter)mGridView.getAdapter()).notifyDataSetChanged();
        }
    }

    private class GridScrollListener implements AbsListView.OnScrollListener {

        private final Context context;

        private GridScrollListener(Context context) {
            this.context = context;
        }

        @Override
        public void onScrollStateChanged(AbsListView absListView, int scrollState) {
            final Picasso picasso = Picasso.with(context);
            if (scrollState == SCROLL_STATE_IDLE || scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                picasso.resumeTag(context);
            } else {
                picasso.pauseTag(context);
            }
        }

        @Override
        public void onScroll(AbsListView absListView, int i, int i1, int i2) {

        }
    }
}
