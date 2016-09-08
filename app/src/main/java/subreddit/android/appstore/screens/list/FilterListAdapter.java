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
import subreddit.android.appstore.backend.data.AppTags;
import subreddit.android.appstore.util.ui.BaseAdapter;
import subreddit.android.appstore.util.ui.BaseViewHolder;


public class FilterListAdapter extends BaseAdapter<FilterListAdapter.ViewHolder> {
    final List<AppTags> data = Arrays.asList(AppTags.values());
    final SparseBooleanArray selectedItems = new SparseBooleanArray(AppTags.values().length);
    TagMap tagMap = new TagMap();

    interface FilterListener {
        void onNewFilterTags(Collection<AppTags> appTagses);
    }

    public FilterListAdapter(final FilterListener filterListener) {
        setItemClickListener(new BaseViewHolder.ClickListener() {
            @Override
            public boolean onItemClick(View view, int position, long itemId) {
                //If selected tag is FREE, make sure paid is removed from selectedItems before it is added
                if (data.get(position) == AppTags.FREE) {
                    //Find 'PAID' in selectedItems and remove it
                    for (int i = 0; i < selectedItems.size(); i++) {
                        int key = selectedItems.keyAt(i);
                        if (data.get(key) == AppTags.PAID) {
                            selectedItems.delete(key);
                            notifyDataSetChanged();
                        }
                    }
                } else if (data.get(position) == AppTags.PAID) {
                    //Find 'FREE' in selectedItems and remove it
                    for (int i = 0; i < selectedItems.size(); i++) {
                        int key = selectedItems.keyAt(i);
                        if (data.get(key) == AppTags.FREE) {
                            selectedItems.delete(key);
                            notifyDataSetChanged();
                        }
                    }
                }
                selectedItems.put(position, !selectedItems.get(position));
                notifyItemChanged(position);
                Collection<AppTags> activeAppTagses = new ArrayList<>();
                for (int i = 0; i < selectedItems.size(); i++) {
                    int key = selectedItems.keyAt(i);
                    if (selectedItems.get(key)) activeAppTagses.add(data.get(key));
                }
                filterListener.onNewFilterTags(activeAppTagses);
                return false;
            }
        });
    }

    public void updateTagMap(TagMap tagMap) {
        this.tagMap = tagMap;
    }

    @Override
    public FilterListAdapter.ViewHolder onCreateSDMViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View layout = inflater.inflate(R.layout.adapter_tagfilter_line, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindSDMViewHolder(FilterListAdapter.ViewHolder holder, int position) {
        AppTags tag = getItem(position);
        holder.bind(tag, selectedItems.get(position), tagMap.getCount(tag));
    }

    public AppTags getItem(int position) {
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

        public void bind(AppTags item, boolean checked, int tagNumber) {
            tagName.setText(item.name());
            tagCount.setText(getQuantityString(R.plurals.x_items, tagNumber, tagNumber));
            checkBox.setChecked(checked);
        }
    }
}
