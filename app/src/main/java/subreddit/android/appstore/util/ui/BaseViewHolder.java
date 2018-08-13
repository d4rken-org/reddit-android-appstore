package subreddit.android.appstore.util.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

public class BaseViewHolder extends RecyclerView.ViewHolder {
    private ClickListener clickListener;
    private LongClickListener longClickListener;

    public BaseViewHolder(final View itemView) {
        super(itemView);
        setOnClickListener(buildWrapperForAdapterClickListener());
        setOnLongClickListener(buildWrapperForAdapterLongClickListener());
    }


    void setAdapterClickListener(@Nullable ClickListener listener) {
        clickListener = listener;
    }

    private View.OnClickListener buildWrapperForAdapterClickListener() {
        return v -> {
            if (clickListener == null) return;
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
            clickListener.onItemClick(v, getAdapterPosition(), getItemId());
        };
    }

    void setAdapterLongClickListener(@Nullable LongClickListener listener) {
        longClickListener = listener;
    }

    private View.OnLongClickListener buildWrapperForAdapterLongClickListener() {
        return v -> longClickListener != null && (getAdapterPosition() == RecyclerView.NO_POSITION ||
                longClickListener.onItemLongClick(itemView, getAdapterPosition(), getItemId()));
    }

    private void setOnClickListener(@Nullable View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }

    private void setOnLongClickListener(@Nullable View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
    }

    public void post(Runnable runnable) {
        getRootView().post(runnable);
    }

    private View getRootView() {
        return itemView;
    }

    public Context getContext() {
        return this.itemView.getContext();
    }

    public String getString(@StringRes int string) {
        return getResources().getString(string);
    }

    public String getString(@StringRes int string, Object... args) {
        return getResources().getString(string, args);
    }

    protected String getQuantityString(int id, int quantity, Object... formatArgs) throws Resources.NotFoundException {
        return getResources().getQuantityString(id, quantity, formatArgs);
    }

    public Resources getResources() {
        return getContext().getResources();
    }

    public int getColor(@ColorRes int colorRes) {
        return ContextCompat.getColor(getContext(), colorRes);
    }

    public Drawable getDrawable(@DrawableRes int drawableRes) {
        return ContextCompat.getDrawable(getContext(), drawableRes);
    }

    protected LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    public interface ClickListener {
        boolean onItemClick(View view, int position, long itemId);
    }

    public interface LongClickListener {
        boolean onItemLongClick(View view, int position, long itemId);
    }
}