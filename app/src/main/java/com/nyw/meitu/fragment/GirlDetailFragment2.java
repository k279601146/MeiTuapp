package com.nyw.meitu.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.king.base.BaseFragment;
import com.king.base.model.EventMessage;
import com.nyw.meitu.R;
import com.nyw.meitu.adapter.GirlDetailAdapter2;
import com.nyw.meitu.model.GirlResult2;

import java.util.List;


public class GirlDetailFragment2 extends BaseFragment {


    private ViewPager viewPager;

    private TextView tvCount;

    private GirlDetailAdapter2 adapter;

    private List<GirlResult2.ImgsBean> listData;

    private int position;

    private int total;




    public static GirlDetailFragment2 newInstance(@NonNull List<GirlResult2.ImgsBean> listData, int position) {
        Bundle args = new Bundle();
        GirlDetailFragment2 fragment = new GirlDetailFragment2();
        fragment.listData = listData;
        fragment.position = position;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int inflaterRootView() {
        return R.layout.fragment_girl_detail;
    }

    @Override
    public void initUI() {
        viewPager = findView(R.id.viewPager);
        tvCount = findView(R.id.tvCount);
        total = listData!=null ? listData.size() : 0;
    }

    @Override
    public void addListeners() {

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updateCurPostionView(position+1);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    @Override
    public void initData() {
        adapter = new GirlDetailAdapter2(context,listData);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(position);
        updateCurPostionView(position+1);

    }

    public void updateCurPostionView(int position){
        tvCount.setText(String.format("%d/%d",position,total));
    }

    @Override
    public void onEventMessage(EventMessage em) {

    }
}
