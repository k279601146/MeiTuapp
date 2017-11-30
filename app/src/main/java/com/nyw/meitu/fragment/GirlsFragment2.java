package com.nyw.meitu.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.jude.easyrecyclerview.EasyRecyclerView;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;
import com.jude.easyrecyclerview.decoration.SpaceDecoration;
import com.king.base.BaseFragment;
import com.king.base.model.EventMessage;
import com.king.base.util.LogUtils;
import com.king.base.util.SharedPreferencesUtils;
import com.king.base.util.SystemUtils;
import com.nyw.meitu.Constants;
import com.nyw.meitu.MainActivity;
import com.nyw.meitu.R;
import com.nyw.meitu.activity.ContentActivity;
import com.nyw.meitu.adapter.EsayGirlAdapter2;
import com.nyw.meitu.dao.ChannelItem;
import com.nyw.meitu.http.APIRetrofit;
import com.nyw.meitu.http.APIService;
import com.nyw.meitu.model.GirlResult2;
import com.nyw.meitu.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author Jenly <a href="mailto:jenly1314@gmail.com">Jenly</a>
 * @since 2017/1/11
 */
public class GirlsFragment2 extends BaseFragment implements MainActivity.OnScrollListener {




    public enum LayoutType{
        LinearLayout,GridLayout,StaggeredGridLayout
    }

    private LayoutType layoutType = LayoutType.StaggeredGridLayout;

    private TextView tvTips;

    private EasyRecyclerView recyclerView;

    private EsayGirlAdapter2 esayGirlAdapter;

    private List<GirlResult2.ImgsBean> listData;

    private int rn =100;//显示数量

    private String ftags;
    private int pn=1;//开始条数
    private String type="美女";

    @Override
    public void onScroll() {
        if(recyclerView!=null)
            recyclerView.scrollToPosition(0);
    }

    @Override
    public int inflaterRootView() {
        return R.layout.fragment_girl;
    }

    @Override
    public void initUI() {
        Bundle bundle = getArguments();//从activity传过来的Bundle
        if(bundle!=null){
            ftags=bundle.getString("str");
        }
        recyclerView = findView(R.id.recyclerView);

        tvTips = (TextView) recyclerView.findViewById(R.id.tvTips);

        listData = new ArrayList<>();
        esayGirlAdapter = new EsayGirlAdapter2(context,listData,layoutType == LayoutType.GridLayout);
        initLayoutManager(layoutType);

        SpaceDecoration spaceDecoration = new SpaceDecoration(DensityUtil.dp2px(context,2));
        recyclerView.addItemDecoration(spaceDecoration);
        recyclerView.setRefreshingColorResources(R.color.colorPrimary);

        recyclerView.setAdapter(esayGirlAdapter );

        esayGirlAdapter.setMore(R.layout.load_more, new RecyclerArrayAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                pn=pn+100;
                getGirls(type,ftags,pn,rn);
            }
        });
    }

    private void initLayoutManager(LayoutType layoutType){
        switch (layoutType){
            case LinearLayout:
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false));
                break;
            case GridLayout:
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
                gridLayoutManager.setSpanSizeLookup(esayGirlAdapter.obtainGridSpanSizeLookUp(2));
                recyclerView.setLayoutManager(gridLayoutManager);
                break;
            case StaggeredGridLayout:
            default:
                final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                staggeredGridLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                recyclerView.getRecyclerView().addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        //防止第一行到顶部有空白区域
                        staggeredGridLayoutManager.invalidateSpanAssignments();
                    }
                });
                break;
        }
    }

    @Override
    public void addListeners() {

        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getGirls(type,ftags,pn,rn);
            }
        });

        esayGirlAdapter.setOnClickHolderItemListener(new EsayGirlAdapter2.OnClickHolderItemListener() {
            @Override
            public void onItemClick(BaseViewHolder holder, int position) {
                startGirlDetail((ArrayList<GirlResult2.ImgsBean>) esayGirlAdapter.getAllData(),position,holder.itemView);
            }
        });

    }

    /**
     *
     * @param listData
     * @param position
     * @param source
     */
    private void startGirlDetail(ArrayList<GirlResult2.ImgsBean> listData, int position, View source){
        //Toast.makeText(context, "长按图片试试哦！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, ContentActivity.class);
        intent.putExtra(KEY_FRAGMENT, ContentActivity.GIRL_DETAIL_FRAGMENT2);
        intent.putParcelableArrayListExtra(Constants.LIST_GIRL2, listData);
        intent.putExtra(Constants.CURRENT_POSTION2,position);
        ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeScaleUpAnimation(
                source,source.getWidth()/2,source.getHeight()/2,0,0);
        ActivityCompat.startActivity(getActivity(),intent,activityOptionsCompat.toBundle());
    }


    @Override
    public void initData() {
        getGirls(type,ftags,pn,rn);
    }


    private <T> void toSetList(List<T> list,List<T> newList,boolean isMore){

        if(list == null || newList == null)
            return;

        if(!isMore){
            list.clear();
        }
        list.addAll(newList);

    }


    private void getGirls(final String type,final String ftags,final int num,final int page){

        APIRetrofit.getInstance2()
                .create(APIService.class)
                .getGirs2(type,ftags,num,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GirlResult2>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.d("onCompleted");
                        recyclerView.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e);

                        if(SystemUtils.isNetWorkActive(context)){
                            tvTips.setText(R.string.page_load_failed);
                        }else{
                            tvTips.setText(R.string.network_unavailable);
                        }

                        recyclerView.showError();
                    }

                    @Override
                    public void onNext(GirlResult2 girlResult) {
                        LogUtils.d(girlResult.toString());
                            List<GirlResult2.ImgsBean> list = girlResult.getImgs();
                            if(page<=1){
                                esayGirlAdapter.clear();
                                if(list!=null && list.size()>0){
                                    SharedPreferencesUtils.put(context,"first_girl_url2",list.get(0).getImageUrl());
                                }
                            }
                            esayGirlAdapter.addAll(girlResult.getImgs());
                            if(esayGirlAdapter.getCount() >= page * num){
                                pn++;
                            }else if(esayGirlAdapter.getCount()==0){
                                recyclerView.showEmpty();
                            }

                    }
                });
    }


    @Override
    public void onEventMessage(EventMessage em) {

    }
}
