package subreddit.android.appstore.screens.navigation;


import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.util.Locale;

import subreddit.android.appstore.R;

public class CategoryFilter implements Parcelable {
    private final String primaryCategory;
    private final String secondaryCategory;
    private int name;

    public CategoryFilter() {
        primaryCategory = null;
        secondaryCategory = null;
    }

    public CategoryFilter(@Nullable String primaryCategory, @Nullable String secondaryCategory) {
        this.primaryCategory = primaryCategory;
        this.secondaryCategory = secondaryCategory;
    }

    @Nullable
    public String getPrimaryCategory() {
        return primaryCategory;
    }

    @Nullable
    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    protected CategoryFilter(Parcel in) {
        primaryCategory = in.readString();
        secondaryCategory = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(primaryCategory);
        dest.writeString(secondaryCategory);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CategoryFilter> CREATOR = new Creator<CategoryFilter>() {
        @Override
        public CategoryFilter createFromParcel(Parcel in) {
            return new CategoryFilter(in);
        }

        @Override
        public CategoryFilter[] newArray(int size) {
            return new CategoryFilter[size];
        }
    };

    public String getFragmentTag() {
        return String.format(Locale.US, "%s:%s", primaryCategory, secondaryCategory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryFilter that = (CategoryFilter) o;

        if (primaryCategory != null ? !primaryCategory.equals(that.primaryCategory) : that.primaryCategory != null)
            return false;
        return secondaryCategory != null ? secondaryCategory.equals(that.secondaryCategory) : that.secondaryCategory == null;

    }

    @Override
    public int hashCode() {
        int result = primaryCategory != null ? primaryCategory.hashCode() : 0;
        result = 31 * result + (secondaryCategory != null ? secondaryCategory.hashCode() : 0);
        return result;
    }

    public String getName(Context context) {
        if (secondaryCategory != null) return secondaryCategory;
        else if (primaryCategory != null) return primaryCategory;
        else return context.getString(R.string.app_category_everything);
    }
}
