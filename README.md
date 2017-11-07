# JYRecyclerViewAdapter

此Adapter是根据 鸿洋大神 2017-11-6 推送的《封装RecyclerView自动加载更多&下拉刷新》 再次进行封装。
[封装RecyclerView自动加载更多&下拉刷新](http://blog.csdn.net/kong_gu_you_lan/article/details/78294330)


主要有以下内容：
* 对Adapter的ViewHolder进行的处理。
* 对Recycler各种初始化进行了封装。具体可看RecyclerViewUtil。

使用：

* 初始化Adapter

```
    private void initAdapter() {
        mAdapter = new CommonAdapter<String>(null, R.layout.adapter_recyclerview, this) {


            @Override
            protected void convert(ViewHolder holder, final String strings, int position) {
                holder.setText(R.id.tv_item, strings);


                // 条目中某控件点击事件
                holder.setOnClickListener(R.id.tv_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "点击控件："+strings, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        };

        // 条目点击事件
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Toast.makeText(MainActivity.this, "点击条目："+mAdapter.getDatas().get(position), Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }


```

* 接着 初始化RecyclerView

```
     private void initRecyclerView() {

            RecyclerViewUtil.setCommonRecyclerView(this, mRecyclerView, mAdapter, mSwipeRefreshLayout, new RecyclerViewUtil.RefreshListener() {
                @Override
                public void refresh() {
                    // 下拉刷新

                    dataList.clear();
                    initData();
                    RecyclerViewUtil.notifyWrapper();   // 刷新Wrapper状态
                    mSwipeRefreshLayout.setRefreshing(false);
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
```

* 最后设置数据

```
    private void init() {
        mAdapter.setDatas(dataList);

    }
```