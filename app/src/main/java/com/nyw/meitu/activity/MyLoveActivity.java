package com.nyw.meitu.activity;

import android.app.ActivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.nyw.meitu.R;

public class MyLoveActivity extends AppCompatActivity {

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);}
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
