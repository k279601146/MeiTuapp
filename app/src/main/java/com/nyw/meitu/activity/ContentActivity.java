package com.nyw.meitu.activity;

import android.content.Intent;
import android.provider.SyncStateContract;
import android.support.v4.app.Fragment;

import com.king.base.BaseActivity;
import com.king.base.model.EventMessage;
import com.king.base.util.LogUtils;
import com.nyw.meitu.Constants;
import com.nyw.meitu.R;
import com.nyw.meitu.fragment.GirlDetailFragment;
import com.nyw.meitu.fragment.GirlDetailFragment2;
import com.nyw.meitu.model.GirlResult;
import com.nyw.meitu.model.GirlResult2;

import java.util.List;


public class ContentActivity extends BaseActivity {

    public static final int GIRL_DETAIL_FRAGMENT = 0X01;
    public static final int GIRL_DETAIL_FRAGMENT2 = 0X02;

    @Override
    public void initUI() {
        setContentView(R.layout.activity_content);
        Intent intent = getIntent();
        int fragmentId = intent.getIntExtra(KEY_FRAGMENT,0);
        switch (fragmentId){
            case GIRL_DETAIL_FRAGMENT:
                List<GirlResult.Girl> listData = intent.getParcelableArrayListExtra(Constants.LIST_GIRL);
                int position = intent.getIntExtra(Constants.CURRENT_POSTION,0);
                replaceFragment(GirlDetailFragment.newInstance(listData,position));
                break;
            case GIRL_DETAIL_FRAGMENT2:
                List<GirlResult2.ImgsBean> listData2 = intent.getParcelableArrayListExtra(Constants.LIST_GIRL2);
                int position2 = intent.getIntExtra(Constants.CURRENT_POSTION2,0);
                replaceFragment(GirlDetailFragment2.newInstance(listData2,position2));
                break;
            default:
                LogUtils.w("Not found fragment.");
                break;
        }

    }

    public void replaceFragment(Fragment fragment){
        replaceFragment(R.id.fragment_content,fragment);
    }

    @Override
    public void addListeners() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onEventMessage(EventMessage em) {

    }
}
