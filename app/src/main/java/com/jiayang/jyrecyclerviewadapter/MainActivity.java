package com.jiayang.jyrecyclerviewadapter;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jiayang.jyrecyclerviewadapter.JYRecyclerAdapter.CommonAdapter;
import com.jiayang.jyrecyclerviewadapter.JYRecyclerAdapter.JYLoadMoreWrapper;
import com.jiayang.jyrecyclerviewadapter.JYRecyclerAdapter.RecyclerViewUtil;
import com.jiayang.jyrecyclerviewadapter.JYRecyclerAdapter.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.jiayang.jyrecyclerviewadapter.R.id.recyclerView;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private List<String> dataList = new ArrayList<>();
    private CommonAdapter<String> mAdapter;
    private View mHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeLayout);
        mRecyclerView = (RecyclerView) findViewById(recyclerView);

        initData();
        initAdapter();
        initRecyclerView();
        init();
    }

    private void initData() {
        char letter = 'A';
        for (int i = 0; i < 26; i++) {
            dataList.add(String.valueOf(letter));
            letter++;
        }

    }

    private void initAdapter() {
        mAdapter = new CommonAdapter<String>(null, R.layout.adapter_recyclerview, this, true) {


            @Override
            protected void convert(ViewHolder holder, final String strings, int position) {
                holder.setText(R.id.tv_item, strings);


                // 条目中某控件点击事件
                holder.setOnClickListener(R.id.tv_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "点击控件：" + strings, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };

        // 条目点击事件
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Toast.makeText(MainActivity.this, "点击条目：" + mAdapter.getDatas().get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        mHeadView = getLayoutInflater().inflate(R.layout.item_main_head, null);
    }

    private void initRecyclerView() {

        RecyclerViewUtil.setCommonRecyclerView2(this, mRecyclerView, mAdapter, true, mHeadView, mSwipeRefreshLayout, new RecyclerViewUtil.RefreshListener() {
            @Override
            public void refresh() {
                // 下拉刷新

                dataList.clear();
                initData();
                addHeadView();

                mAdapter.setDatas(dataList);
            }

            @Override
            public void loadMore() {
                // 上拉加载更多
                if (dataList.size() < 52) {
                    // 模拟获取网络数据，延时1s
                    new Timer().schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    initData();
                                    mAdapter.setDatas(dataList);
                                    RecyclerViewUtil.setLoadState(JYLoadMoreWrapper.LOADING_COMPLETE);
                                }
                            });
                        }
                    }, 1000);
                } else {
                    // 显示加载到底的提示
                    RecyclerViewUtil.setLoadState(JYLoadMoreWrapper.LOADING_END);
                }
            }
        });
    }

    private void addHeadView() {
        ((TextView)mHeadView.findViewById(R.id.textViewHead)).setText("这是头布局刷新改变的");
    }

    private void init() {
        mAdapter.setDatas(dataList);

    }
}
