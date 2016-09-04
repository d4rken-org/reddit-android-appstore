package subreddit.android.appstore.screens.list;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import subreddit.android.appstore.R;
import subreddit.android.appstore.backend.AppInfo;
import subreddit.android.appstore.util.ui.BaseAdapter;
import subreddit.android.appstore.util.ui.BaseViewHolder;


public class FilterListAdapter extends BaseAdapter<FilterListAdapter.ViewHolder> {
    final List<AppInfo.Tag> data = Arrays.asList(AppInfo.Tag.values());
    final SparseBooleanArray selectedItems = new SparseBooleanArray(AppInfo.Tag.values().length);
    private int[] tagCArray = new int[AppInfo.Tag.values().length];

    interface FilterListener {
        void onNewFilterTags(Collection<AppInfo.Tag> tags);
    }

    public FilterListAdapter(final FilterListener filterListener) {
        setItemClickListener(new BaseViewHolder.ClickListener() {
            @Override
            public boolean onItemClick(View view, int position, long itemId) {
                selectedItems.put(position, !selectedItems.get(position));
                notifyItemChanged(position);
                Collection<AppInfo.Tag> activeTags = new ArrayList<>();
                for (int i = 0; i < selectedItems.size(); i++) {
                    int key = selectedItems.keyAt(i);
                    if (selectedItems.get(key)) activeTags.add(data.get(key));
                }
                filterListener.onNewFilterTags(activeTags);
                return false;
            }
        });
    }

    public void updateTagCount(int[] tagCount) {
        this.tagCArray = tagCount;
    }

    @Override
    public FilterListAdapter.ViewHolder onCreateSDMViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View layout = inflater.inflate(R.layout.adapter_tagfilter_line, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindSDMViewHolder(FilterListAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position), selectedItems.get(position), tagCArray[position]);
    }

    public AppInfo.Tag getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.tagname) TextView tagName;
        @BindView(R.id.tagcount) TextView tagCount;
        @BindView(R.id.checkbox) CheckBox checkBox;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(AppInfo.Tag item, boolean checked, int tagNumber) {
            tagName.setText(item.name());
            tagCount.setText(getQuantityString(R.plurals.x_items, tagNumber, tagNumber));
            checkBox.setChecked(checked);
        }
    }
}
