package com.nyw.meitu;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.bilibili.magicasakura.utils.ThemeUtils;
import com.king.base.BaseActivity;
import com.king.base.model.EventMessage;
import com.nyw.meitu.activity.AboutActivity;
import com.nyw.meitu.activity.MyLoveActivity;
import com.nyw.meitu.activity.SettingActivity;
import com.nyw.meitu.dao.ChannelItem;
import com.nyw.meitu.dao.ChannelManage;
import com.nyw.meitu.edit.ChannelActivity;
import com.nyw.meitu.fragment.GirlsFragment;
import com.nyw.meitu.fragment.GirlsFragment2;
import com.nyw.meitu.fragment.NewsFragmentPagerAdapter;
import com.nyw.meitu.tool.CardPickerDialog;
import com.nyw.meitu.util.ExchangeUtils;
import com.nyw.meitu.util.SnackAnimationUtil;
import com.nyw.meitu.util.ThemeHelper;
import com.nyw.meitu.util.Utils;
import com.nyw.meitu.view.ColumnHorizontalScrollView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends BaseActivity implements View.OnClickListener {

    private DrawerLayout mDrawerLayout;

    private NavigationView navigationView;

    private Toolbar toolbar;

    private ViewPager viewPager;

    private FloatingActionButton fab;

    private OnScrollListener onScrollListener;
    public interface OnScrollListener {
        void onScroll();
    }
    private long exitTime;
    private ColumnHorizontalScrollView mColumnHorizontalScrollView; // 自定义HorizontalScrollView
    private LinearLayout mRadioGroup_content; // 每个标题

    private LinearLayout ll_more_columns; // 右边+号的父布局
    private ImageView button_more_columns; // 标题右边的+号

    private RelativeLayout rl_column; // +号左边的布局：包括HorizontalScrollView和左右阴影部分
    public ImageView shade_left; // 左阴影部分
    public ImageView shade_right; // 右阴影部分

    private int columnSelectIndex = 0; // 当前选中的栏目索引
    private int mItemWidth = 0; // Item宽度：每个标题的宽度

    private int mScreenWidth = 0; // 屏幕宽度

    public final static int CHANNELREQUEST = 1; // 请求码
    public final static int CHANNELRESULT = 10; // 返回码

    // tab集合：HorizontalScrollView的数据源
    private ArrayList<ChannelItem> userChannelList = new ArrayList<ChannelItem>();

    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();


    @Override
    public void initUI() {
        setContentView(R.layout.activity_main);
        //只有当系统版本大于等于6.0时才需要
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //判断该应用是否有写SD卡权限，如果没有再去申请
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
            }
          if (Build.VERSION.SDK_INT >= 17) {
                final MainActivity context = MainActivity.this;
                ActivityManager.TaskDescription taskDescription = new ActivityManager.TaskDescription(null, null,
                        ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                setTaskDescription(taskDescription);
                getWindow().setStatusBarColor(
                        ThemeUtils.getColorById(context, R.color.theme_color_primary_dark));
            }
            mScreenWidth = Utils.getWindowsWidth(this);
            mItemWidth = mScreenWidth / 7; // 一个Item宽度为屏幕的1/7
            mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
            mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
            ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
            rl_column = (RelativeLayout) findViewById(R.id.rl_column);
            button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
            // + 号监听
            button_more_columns.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent_channel = new Intent(getApplicationContext(), ChannelActivity.class);
                    startActivityForResult(intent_channel, CHANNELREQUEST);
                }
            });
            fab= (FloatingActionButton) findViewById(R.id.fab);
            shade_left = (ImageView) findViewById(R.id.shade_left);
            shade_right = (ImageView) findViewById(R.id.shade_right);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            setChangelView();
            toolbar = findView(R.id.toolbar);
            setSupportActionBar(toolbar);
            fab = findView(R.id.fab);
            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navigationView = (NavigationView) findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            mDrawerLayout.setDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.item_one:

                            break;
                        case R.id.item_two:
                            Snackbar.make(navigationView,"正在开发中", Snackbar.LENGTH_SHORT).show();
                            //startActivity(new Intent(MainActivity.this, MyLoveActivity.class));
                            break;
                        case R.id.item_three:
                            Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
                            startActivity(Intent.createChooser(intent, "选择壁纸"));
                            break;
                        case R.id.item_four:
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                            break;
                        case R.id.item_five:
                            //startActivity(new Intent(MainActivity.this,ThemeActivity.class));
                            CardPickerDialog dialog = new CardPickerDialog();
                            dialog.setClickListener(new CardPickerDialog.ClickListener() {
                                @Override
                                public void onConfirm(int currentTheme) {
                                    if (ThemeHelper.getTheme(MainActivity.this) != currentTheme) {
                                        ThemeHelper.setTheme(MainActivity.this, currentTheme);
                                        ThemeUtils.refreshUI(MainActivity.this, new ThemeUtils.ExtraRefreshable() {
                                                    @Override
                                                    public void refreshGlobal(Activity activity) {
                                                        //for global setting, just do once
                                                        if (Build.VERSION.SDK_INT >= 17) {
                                                            final MainActivity context = MainActivity.this;
                                                            ActivityManager.TaskDescription taskDescription =
                                                                    new ActivityManager.TaskDescription(null, null,
                                                                            ThemeUtils.getThemeAttrColor(context, android.R.attr.colorPrimary));
                                                            setTaskDescription(taskDescription);
                                                            getWindow().setStatusBarColor(
                                                                    ThemeUtils.getColorById(context, R.color.theme_color_primary_dark));
                                                        }
                                                    }

                                                    @Override
                                                    public void refreshSpecificView(View view) {
                                                        //TODO: will do this for each traversal
                                                    }
                                                }
                                        );
                                        View view = findViewById(R.id.snack_layout);
                                        if (view != null) {
                                            TextView textView = (TextView) view.findViewById(R.id.content);
                                            textView.setText(getSnackContent(currentTheme));
                                            SnackAnimationUtil.with(MainActivity.this, R.anim.snack_in, R.anim.snack_out)
                                                    .setDismissDelayTime(1000)
                                                    .setTarget(view)
                                                    .play();
                                        }
                                    }
                                }
                            });
                            dialog.show(getSupportFragmentManager(), CardPickerDialog.TAG);
                            break;
                      /* case R.id.item_six:
                           *//* String UIN_QQ = "992116519";
                            ExchangeUtils.chatQQ(MainActivity.this, UIN_QQ);*//*
                            break;*/
                        case R.id.item_seven:
                            startActivity(new Intent(MainActivity.this, AboutActivity.class));
                            break;
                        case R.id.item_eight:
                            new AlertView("提示", "您真的要退出软件吗？", "取消", new String[]{"确定"}, null, MainActivity.this, AlertView.Style.Alert, new OnItemClickListener() {
                                @Override
                                public void onItemClick(Object o, int position) {
                                    switch (position) {
                                        case 0:
                                            finish();
                                            break;
                                        case 1:
                                            MainActivity.this.finish();
                                            System.exit(0);
                                            break;
                                    }
                                }
                            }).show();
                            break;
                    }
                    return false;
                }
            });
        }
    }

    @Override
    public void initData() {

    }

    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }

    private void initColumnData() {
        userChannelList = ((ArrayList<ChannelItem>) ChannelManage.getManage(MyApplication.getApp().getSQLHelper()).getUserChannel());
    }
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = userChannelList.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            final TextView columnTextView = new TextView(this);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelList.get(i).getName());
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }

            // 单击监听
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v) {
                            localView.setSelected(false);
                        } else {
                            localView.setSelected(true);
                            viewPager.setCurrentItem(i);
                        }
                    }
                    Toast.makeText(getApplicationContext(), userChannelList.get(v.getId()).getName(), Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
        }
    }
        /**
         * 初始化Fragment
         */

    private void initFragment() {
        fragments.clear();//清空
        int count = userChannelList.size();
        for (int i = 0; i < 1; i++) {
            GirlsFragment fragment = new GirlsFragment();
            fragments.add(fragment);
        }
        for (int i = 1; i < count; i++) {
            GirlsFragment2 fragment2 = new GirlsFragment2();
            Bundle bundle = new Bundle();
            String strValue =userChannelList.get(i).getName() ;
            bundle.putString("str", strValue);
            fragment2.setArguments(bundle);
            fragments.add(fragment2);
        }
        onScrollListener = (OnScrollListener) fragments.get(0);
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(mAdapetr);
        viewPager.addOnPageChangeListener(pageListener);
    }

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            viewPager.setCurrentItem(position);
            onScrollListener = (OnScrollListener) fragments.get(position);
            selectTab(position);
        }
    };

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        //判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHANNELREQUEST:
                if (resultCode == CHANNELRESULT) {
                    setChangelView();
                }
                break;

            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String getSnackContent(int current) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        return getResources().getString(getResources().getIdentifier(
                "magicasrkura_prompt_" + random.nextInt(3), "string", getPackageName())) + ThemeHelper.getName(current);
    }
    @Override
    public void addListeners() {
        fab.setOnClickListener(this);
    }
    @Override
    public void onEventMessage(EventMessage em) {
    }

    private void clickFab(){
        if(onScrollListener!=null){
            onScrollListener.onScroll();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.fab:
                clickFab();
                break;
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
          /*  UMImage image = new UMImage(MainActivity.this, R.drawable.ic_500);//资源文件
            new ShareAction(MainActivity.this).withText("hello").withMedia(image).share();*/
            case R.id.action_share:
                //Toast.makeText(this,"你好",Toast.LENGTH_SHORT).show();
                UMImage thumb =  new UMImage(this, R.drawable.default_picture);
                UMWeb web = new UMWeb("https://www.pgyer.com/nywapp");
                web.setTitle("美图");//标题
                web.setThumb(thumb);  //缩略图
                web.setDescription("美图(美女图片)，可以保存高清壁纸，可以分享精美图片"+ "给您的朋友!将精彩图片设置为手机壁纸，或者使用侧边导航中的“壁纸设置（本地）”" +
                        "功能将手机相册中的精美图片设置成手机壁纸，也可以设置手机的锁屏壁纸。");//描述
                new ShareAction(MainActivity.this)
                        .withMedia(web)
                        .setDisplayList(SHARE_MEDIA.SINA, SHARE_MEDIA.QQ,SHARE_MEDIA.QZONE,SHARE_MEDIA.WEIXIN,SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.WEIXIN_FAVORITE,
                                SHARE_MEDIA.SMS,SHARE_MEDIA.EMAIL,SHARE_MEDIA.MORE)
                        .setCallback(shareListener)
                        .open();
                break;
            case R.id.action_gengxin:
                Snackbar.make(mDrawerLayout ,"已经是最新版本哦", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_fankui:
                Snackbar.make(mDrawerLayout,"邮箱992116519@qq.com", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.action_lianxi:
                Snackbar.make(mDrawerLayout,"邮箱992116519@qq.com", Snackbar.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
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
                Toast.makeText(MainActivity.this,platform + " 收藏成功啦",Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            }          }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            if(t!=null){
                Log.d("throw","throw:"+t.getMessage());
            }          }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,"取消",Toast.LENGTH_LONG).show();

        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                android.os.Process.killProcess(android.os.Process.myPid());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    // 调用requestPermissions会弹出对话框，用户做出选择之后的回调
        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            //requestCode 是调用requestPermissions传入的123，当然你可以设置成其他值或者某个静态变量
            if (requestCode == 123) {
                if (grantResults.length >= 1) {
                    //因为我们只申请了一个权限，所以这个数组只有一个
                    int writeResult = grantResults[0];
                    //判断是否授权，也就是用户点击的是拒绝还是接受
                    boolean writeGranted = writeResult == PackageManager.PERMISSION_GRANTED;
                    if (writeGranted) {
                        //用户点击了接受，可以进行相应处理
                    } else {
                        //用户点击了拒绝，可以进行相应处理
                    }
                }
            }
        }
    }
