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
    ClickListener clickListener;
    LongClickListener longClickListener;

    public BaseViewHolder(final View itemView) {
        super(itemView);
        setOnClickListener(buildWrapperForAdapterClickListener());
        setOnLongClickListener(buildWrapperForAdapterLongClickListener());
    }


    void setAdapterClickListener(@Nullable ClickListener listener) {
        clickListener = listener;
    }

    public View.OnClickListener buildWrapperForAdapterClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener == null) return;
                if (getAdapterPosition() == RecyclerView.NO_POSITION) return;
                clickListener.onItemClick(v, getAdapterPosition(), getItemId());
            }
        };
    }

    void setAdapterLongClickListener(@Nullable LongClickListener listener) {
        longClickListener = listener;
    }

    public View.OnLongClickListener buildWrapperForAdapterLongClickListener() {
        return v -> {
            if (longClickListener == null) return false;
            if (getAdapterPosition() == RecyclerView.NO_POSITION) return true;
            return longClickListener.onItemLongClick(itemView, getAdapterPosition(), getItemId());
        };
    }

    public void setOnClickListener(@Nullable View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }

    public void setOnLongClickListener(@Nullable View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
    }

    public void post(Runnable runnable) {
        getRootView().post(runnable);
    }

    public View getRootView() {
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

    public String getQuantityString(int id, int quantity, Object... formatArgs) throws Resources.NotFoundException {
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

    public LayoutInflater getLayoutInflater() {
        return LayoutInflater.from(getContext());
    }

    public interface ClickListener {
        boolean onItemClick(View view, int position, long itemId);
    }

    public interface LongClickListener {
        boolean onItemLongClick(View view, int position, long itemId);
    }

}