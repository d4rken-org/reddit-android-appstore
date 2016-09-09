package subreddit.android.appstore.screens.list;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.wefika.flowlayout.FlowLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import subreddit.android.appstore.R;
import subreddit.android.appstore.backend.data.AppInfo;
import subreddit.android.appstore.backend.data.AppTags;
import subreddit.android.appstore.util.ui.BaseAdapter;
import subreddit.android.appstore.util.ui.BaseViewHolder;


public class AppListAdapter extends BaseAdapter<AppListAdapter.ViewHolder> implements Filterable, SectionTitleProvider {
    private AppInfoFilter filter;
    final List<AppInfo> data = new ArrayList<>();
    final List<AppInfo> originalData = new ArrayList<>();

    @Override
    public AppListAdapter.ViewHolder onCreateSDMViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        View layout = inflater.inflate(R.layout.adapter_applist_line, parent, false);
        return new ViewHolder(layout);
    }

    @Override
    public void onBindSDMViewHolder(AppListAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public AppInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public String getSectionTitle(int position) {
        return data.get(position).getAppName().substring(0,1).toUpperCase();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(@Nullable List<AppInfo> data) {
        this.data.clear();
        this.originalData.clear();
        if (data != null) {
            this.data.addAll(data);
            this.originalData.addAll(data);
        }
    }

    static class ViewHolder extends BaseViewHolder {
        @BindView(R.id.appname) TextView appName;
        @BindView(R.id.description) TextView description;
        @BindView(R.id.tag_container) FlowLayout tagContainer;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(AppInfo item) {
            appName.setText(item.getAppName());
            description.setText(item.getDescription());

            tagContainer.removeAllViews();
            for (AppTags appTags : item.getTags()) {
                TextView tv = (TextView) getLayoutInflater().inflate(R.layout.view_tagtemplate, tagContainer, false);
                tv.setText(appTags.name());
                FlowLayout.LayoutParams params = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 0, 8, 0);
                tv.setLayoutParams(params);
                tagContainer.addView(tv);
            }
            tagContainer.setVisibility(item.getTags().isEmpty() ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public AppInfoFilter getFilter() {
        if (filter == null) filter = new AppInfoFilter();
        return filter;
    }

    public class AppInfoFilter extends Filter {
        private final Collection<AppTags> filterAppTagses = new HashSet<>();
        private CharSequence filterString;

        public void setFilterAppTagses(@Nullable Collection<AppTags> filterAppTagses) {
            this.filterAppTagses.clear();
            if (filterAppTagses != null) this.filterAppTagses.addAll(filterAppTagses);
        }

        @SuppressWarnings("unchecked")
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            // Always called from background thread
            ArrayList<AppInfo> apps = new ArrayList<>(originalData);
            FilterResults results = new FilterResults();
            String filter = "";
            if (prefix != null) filter = prefix.toString().toLowerCase(Locale.getDefault());

            for (int i = apps.size() - 1; i >= 0; --i) {
                AppInfo item = apps.get(i);
                String appName = item.getAppName().toLowerCase(Locale.getDefault());
                String description = item.getDescription().toLowerCase(Locale.getDefault());

                if (filter.length() > 0 && !appName.contains(filter) && !description.contains(filter)) {
                    apps.remove(i);
                } else if (!item.getTags().containsAll(filterAppTagses)) {
                    apps.remove(i);
                }
            }
            results.count = apps.size();
            results.values = apps;
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence prefix, FilterResults results) {
            if (results.values == null) return;
            filterString = prefix;
            data.clear();
            data.addAll((ArrayList<AppInfo>) results.values);
            notifyDataSetChanged();
        }

        public CharSequence getFilterString() {
            return filterString;
        }
    }
}
