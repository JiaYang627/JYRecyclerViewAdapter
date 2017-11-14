package com.jiayang.jyrecyclerviewadapter.JYRecyclerAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.jiayang.jyrecyclerviewadapter.JYRecyclerAdapter.base.ItemViewDelegate;
import com.jiayang.jyrecyclerviewadapter.JYRecyclerAdapter.base.ItemViewDelegateManager;
import com.jiayang.jyrecyclerviewadapter.JYRecyclerAdapter.base.ViewHolder;

import java.util.List;


/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    private final ItemViewDelegateManager mItemViewDelegateManager;
    protected Context mContext;
    protected boolean hasHead;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected OnItemClickListener mOnItemClickListener;


    public CommonAdapter(List<T> datas, int layoutId, Context context, boolean hasHead) {
        this.mDatas = datas;
        this.mLayoutId = layoutId;
        this.mContext = context;
        this.hasHead = hasHead;
        mItemViewDelegateManager = new ItemViewDelegateManager();

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return mLayoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter.this.convert(holder, t, position);
            }
        });
    }


    protected abstract void convert(ViewHolder holder, T t, int position);


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.createViewHolder(mContext, parent, mLayoutId);
        onViewHolderCreated(holder, holder.getConvertView());
        setListener(parent, holder, viewType);
        return holder;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mItemViewDelegateManager.convert(holder, mDatas.get(position ), holder.getAdapterPosition());
    }


    @Override
    public int getItemCount() {
        int itemCount = mDatas.size() ;
        return itemCount;
    }


    protected void setListener(final ViewGroup parent, final ViewHolder viewHolder, int viewType) {
        if (!isEnabled(viewType)) return;
        viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition() - (hasHead ? 1 : 0);
                    mOnItemClickListener.onItemClick(v, viewHolder, position);
                }
            }
        });

        viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition() - (hasHead ? 1 : 0);
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, position);
                }
                return false;
            }
        });
    }

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder holder, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    protected boolean isEnabled(int viewType) {
        return true;
    }

    public void onViewHolderCreated(ViewHolder holder, View itemView) {

    }

    private void addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        mItemViewDelegateManager.addDelegate(itemViewDelegate);
    }

    public void setDatas(List<T> datas) {
        this.mDatas = datas;
        notifyDataSetChanged();
    }

    public void addDatas(List<T> datas) {
        mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addData(T data) {
        mDatas.add(data);
        notifyDataSetChanged();
    }

    public void insertData(T data, int position) {
        mDatas.add(position, data);
        notifyDataSetChanged();
    }

    public void removeData(int positon) {
        mDatas.remove(positon);
        notifyDataSetChanged();
    }

    public void changeData(T data, int position) {
        mDatas.remove(position);
        mDatas.add(position, data);
        notifyDataSetChanged();
    }

    public void clear() {
        mDatas.clear();
        notifyDataSetChanged();
    }

    public List<T> getDatas() {
        return mDatas;
    }
}
