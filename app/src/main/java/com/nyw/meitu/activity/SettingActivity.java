package com.nyw.meitu.activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bilibili.magicasakura.utils.ThemeUtils;
import com.nyw.meitu.MainActivity;
import com.nyw.meitu.R;
import com.nyw.meitu.util.ExchangeUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

public class SettingActivity extends AppCompatActivity {

    private RelativeLayout mLine1;
    private RelativeLayout mLine2;
    private RelativeLayout mLine3;
    /**
     * 推荐给好友
     */
    private TextView mText4;

    /**
     * 检查更新
     */
    private TextView mText6;
    /**
     * 关于
     */
    private TextView mText7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        if (Build.VERSION.SDK_INT >= 19) {
            final SettingActivity context = SettingActivity.this;
            ActivityManager.TaskDescription taskDescription =
                    new ActivityManager.TaskDescription(null, null,
                            ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
            setTaskDescription(taskDescription);
            getWindow().setStatusBarColor(
                    ThemeUtils.getColorById(context, R.color.theme_color_primary_dark));
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        }
        initView();
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

    private void initView() {
        mLine1 = (RelativeLayout) findViewById(R.id.line1);
        mLine2 = (RelativeLayout) findViewById(R.id.line2);
        mLine3 = (RelativeLayout) findViewById(R.id.line3);
        mText4 = (TextView) findViewById(R.id.text4);
        mText6 = (TextView) findViewById(R.id.text6);
        mText7 = (TextView) findViewById(R.id.text7);
        mLine2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mLine3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this,"清理成功",Toast.LENGTH_SHORT).show();
            }
        });
        mText4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UMImage thumb =  new UMImage(SettingActivity.this, R.drawable.default_picture);
                UMWeb web = new UMWeb("http://dev.umeng.com/social");
                web.setTitle("美图");//标题
                web.setThumb(thumb);  //缩略图
                web.setDescription("美图(美女图片)，可以保存高清壁纸，可以分享精美图片"+ "给您的朋友!将精彩图片设置为手机壁纸，或者使用侧边导航中的“壁纸设置（本地）”" +
                        "功能将手机相册中的精美图片设置成手机壁纸，也可以设置手机的锁屏壁纸。");//描述
                new ShareAction(SettingActivity.this)
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,
                                SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                        .setCallback(shareListener)
                        .open();
            }
        });
        mText6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SettingActivity.this,"已经是最高版本，无需更新",Toast.LENGTH_SHORT).show();
            }
        });
        mText7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,AboutActivity.class));
            }
        });
    }
    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat","platform"+platform);
            if(platform.name().equals("WEIXIN_FAVORITE")){
                Toast.makeText(SettingActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(SettingActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }          }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SettingActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SettingActivity.this,"取消",Toast.LENGTH_LONG).show();

        }
    };
}
