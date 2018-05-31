package com.jiayang.jyrecyclerviewadapter.JYRecyclerAdapter;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


/**
 * Created by Administrator on 2017/3/7.
 */

public class RecyclerViewUtil {


    private static JYLoadMoreWrapper sLoadMoreWrapper;

    public static void setBaseCommonRecyclerView(Context context, final RecyclerView refreshView,
                                                 final RecyclerView.Adapter adapter,
                                                 boolean hasHead ,
                                                 View headView,
                                                 final SwipeRefreshLayout swipeRefreshLayout,
                                                 LinearLayoutManager linearLayoutManager, final RefreshListener listener){
//        PtrClassicDefaultHeader header = new PtrClassicDefaultHeader(context);

        sLoadMoreWrapper = new JYLoadMoreWrapper(adapter ,hasHead ,headView);


        refreshView.setLayoutManager(linearLayoutManager);

        refreshView.setAdapter(sLoadMoreWrapper);

        if (swipeRefreshLayout != null) {

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    notifyWrapper();   // 刷新Wrapper状态 防止 上滑加载更多后 脚布局状态在上滑回调中未及时更新过来
                    listener.refresh();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        refreshView.addOnScrollListener(new JYEndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                sLoadMoreWrapper.setLoadState(JYLoadMoreWrapper.LOADING);
                listener.loadMore();
            }
        });

    }


    /**
     * 单列
     * @param context
     * @param refreshView
     * @param adapter
     * @param swipeRefreshLayout
     * @param listener
     */
    public static void setCommonRecyclerView(Context context, RecyclerView refreshView, CommonAdapter adapter, boolean hasHead , View headView, SwipeRefreshLayout swipeRefreshLayout, final RefreshListener listener){
        setBaseCommonRecyclerView(context, refreshView,
                adapter,
                hasHead,
                headView,
                swipeRefreshLayout,
                new LinearLayoutManager(context),
                listener);
    }


    /**
     * 双列
     * @param context
     * @param refreshView
     * @param adapter
     * @param swipeRefreshLayout
     * @param listener
     */
    public static void setCommonRecyclerView2(Context context, RecyclerView refreshView, CommonAdapter adapter,boolean hasHead , View headView,SwipeRefreshLayout swipeRefreshLayout, final RefreshListener listener){
        setBaseCommonRecyclerView(context, refreshView,
                adapter,
                hasHead,
                headView,
                swipeRefreshLayout,
                new GridLayoutManager(context, 2 ),
                listener);
    }


    /**
     * 设置脚布局的状态
     * @param loadState
     */
    public static void setLoadState(int loadState) {
        sLoadMoreWrapper.setLoadState(loadState);
    }

    public static void notifyWrapper() {
        sLoadMoreWrapper.notifyDataSetChanged();
    }

    public interface RefreshListener{
        void refresh();
        void loadMore();
    }
}
