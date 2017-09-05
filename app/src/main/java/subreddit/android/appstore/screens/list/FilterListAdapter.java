package subreddit.android.appstore.screens.list;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import subreddit.android.appstore.screens.settings.SettingsActivity;
import subreddit.android.appstore.util.ui.BaseAdapter;
import subreddit.android.appstore.util.ui.BaseViewHolder;


public class FilterListAdapter extends BaseAdapter<FilterListAdapter.ViewHolder> {
    final List<AppTags> data = Arrays.asList(AppTags.values());
    final Context context;
    final FilterListener filterListener;
    private SparseBooleanArray selectedItems;
    TagMap tagMap = new TagMap();

    interface FilterListener {
        void onNewFilterTags(Collection<AppTags> appTagses);
    }

    public FilterListAdapter(FilterListener filterListener, Context context) {
        this.context = context;
        this.filterListener = filterListener;

        setSelectedTagFilters();

        setItemClickListener((view, position, itemId) -> {

            if (data.get(position) == AppTags.FREE) {
                deselectTag(AppTags.PAID);
            } else if (data.get(position) == AppTags.PAID) {
                deselectTag(AppTags.FREE);
            }

            selectedItems.put(position, !selectedItems.get(position));
            notifyItemChanged(position);

            if (saveTagFiltersSelected()) {
                saveSelectedTagFilters();
            }

            filterListener.onNewFilterTags(getActiveAppTags());
            return false;
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

    private void deselectTag(AppTags tag) {
        for (int i = 0; i < selectedItems.size(); i++) {
            int key = selectedItems.keyAt(i);
            if (data.get(key) == tag) {
                selectedItems.delete(key);
                notifyDataSetChanged();
            }
        }
    }

    private void saveSelectedTagFilters() {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();

        for(int i = 0; i < AppTags.values().length; i++) {
            editor.putBoolean("savedTags_" + i, selectedItems.get(i, false));
        }

        editor.commit();
    }

    private SparseBooleanArray getSavedTagFilters() {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        SparseBooleanArray savedTagsArray = new SparseBooleanArray(AppTags.values().length);

        for (int i = 0; i < AppTags.values().length; i++) {
            if (prefs.getBoolean("savedTags_" + i, false)) {
                savedTagsArray.put(i, prefs.getBoolean("savedTags_" + i, false));
            }
        }

        return savedTagsArray;
    }

    private Collection<AppTags> getActiveAppTags() {
        Collection<AppTags> activeAppTagses = new ArrayList<>();

        for (int i = 0; i < selectedItems.size(); i++) {
            int key = selectedItems.keyAt(i);
            if (selectedItems.get(key)) activeAppTagses.add(data.get(key));
        }

        return activeAppTagses;
    }

    private boolean saveTagFiltersSelected() {
        SharedPreferences prefs =
                PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
        return prefs.getBoolean(SettingsActivity.PREF_KEY_SAVE_TAG_FILTERS, true);
    }

    void setSelectedTagFilters() {
        if (saveTagFiltersSelected()) {
            selectedItems = getSavedTagFilters();
        } else {
            selectedItems = new SparseBooleanArray(AppTags.values().length);
        }
        filterListener.onNewFilterTags(getActiveAppTags());
    }

}
