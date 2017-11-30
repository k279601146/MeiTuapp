package com.nyw.meitu.activity;

import android.os.Build;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.king.base.SplashActivity;
import com.king.base.util.SharedPreferencesUtils;
import com.nyw.meitu.Constants;
import com.nyw.meitu.MainActivity;
import com.nyw.meitu.R;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * @since 2017/2/4
 */
public class WelcomeActivity extends SplashActivity {

    @Override
    public int getContentViewId() {
        return R.layout.activity_welcome;
    }

    public void setTranslucentStatusBar(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);}
    }

    @Override
    public void initData() {
        setTranslucentStatusBar();
        ImageView iv = findView(R.id.iv);
        String url = SharedPreferencesUtils.getString(context, Constants.FIRST_GIRL_URL);
        Glide.with(context).load(url).error(R.drawable.ic_welcome).crossFade().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(iv);
        super.initData();
    }

    @Override
    public Animation.AnimationListener getAnimationListener() {
        return new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivityFinish(MainActivity.class);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };
    }
}
