package subreddit.android.appstore.util.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseAdapter<ViewHolderT extends BaseViewHolder> extends RecyclerView.Adapter<ViewHolderT> implements
        BaseViewHolder.LongClickListener, BaseViewHolder.ClickListener {
    private BaseViewHolder.LongClickListener mLongClickListener;
    private BaseViewHolder.ClickListener mClickListener;

    @NonNull
    @Override
    public ViewHolderT onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolderT viewHolder = onCreateSDMViewHolder(LayoutInflater.from(parent.getContext()), parent, viewType);
        viewHolder.setAdapterClickListener(this);
        viewHolder.setAdapterLongClickListener(this);
        return viewHolder;
    }

    public abstract ViewHolderT onCreateSDMViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(@NonNull ViewHolderT holder, int position) {
        onBindSDMViewHolder(holder, position);
    }

    public abstract void onBindSDMViewHolder(ViewHolderT _holder, int position);

    public void setItemClickListener(BaseViewHolder.ClickListener listener) {
        mClickListener = listener;
    }

    public void setItemLongClickListener(BaseViewHolder.LongClickListener listener) {
        mLongClickListener = listener;
    }

    @Override
    public boolean onItemClick(View v, int position, long itemId) {
        return mClickListener != null && mClickListener.onItemClick(v, position, itemId);
    }

    @Override
    public boolean onItemLongClick(View v, int position, long itemId) {
        return mLongClickListener != null && mLongClickListener.onItemLongClick(v, position, itemId);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolderT holder) {
        super.onViewRecycled(holder);
    }
}