package com.nyw.meitu.activity;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.king.base.util.SharedPreferencesUtils;
import com.nyw.meitu.Constants;
import com.nyw.meitu.R;

public class MyLoveActivity extends AppCompatActivity {

    private ImageView mIv;

    //private RecyclerView recyclerView;
    //private ShouchangAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded);
        if (Build.VERSION.SDK_INT >= 19) {
            final MyLoveActivity context = MyLoveActivity.this;
            ActivityManager.TaskDescription taskDescription =
                    new ActivityManager.TaskDescription(null, null,
                            ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
            setTaskDescription(taskDescription);
            getWindow().setStatusBarColor(
                    ThemeUtils.getColorById(context, R.color.theme_color_primary_dark));
        }
        setContentView(R.layout.activity_loaded);
        //recyclerView = (RecyclerView) findViewById(R.id.rv_recycleview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        mIv = (ImageView) findViewById(R.id.iv);
        final String url = SharedPreferencesUtils.getString(this, Constants.FIRST_GIRL_URL2);
        Glide.with(this).load(url).into(mIv);
        //recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        //recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

}
