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
    private final String tertiaryCategory;

    public CategoryFilter() {
        primaryCategory = null;
        secondaryCategory = null;
        tertiaryCategory = null;
    }

    public CategoryFilter(@Nullable String primaryCategory, @Nullable String secondaryCategory, @Nullable String tertiaryCategory) {
        this.primaryCategory = primaryCategory;
        this.secondaryCategory = secondaryCategory;
        this.tertiaryCategory = tertiaryCategory;
    }

    @Nullable
    public String getPrimaryCategory() {
        return primaryCategory;
    }

    @Nullable
    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    @Nullable
    public String getTertiaryCategory() {
        return tertiaryCategory;
    }

    protected CategoryFilter(Parcel in) {
        primaryCategory = in.readString();
        secondaryCategory = in.readString();
        tertiaryCategory = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(primaryCategory);
        dest.writeString(secondaryCategory);
        dest.writeString(tertiaryCategory);
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
        return String.format(Locale.US, "%s:%s:%s", primaryCategory, secondaryCategory, tertiaryCategory);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryFilter that = (CategoryFilter) o;

        if (primaryCategory==null ? that.primaryCategory!=null : !primaryCategory.equals(that.primaryCategory))
            return false;
        if (secondaryCategory==null ? that.secondaryCategory!=null : !secondaryCategory.equals(that.secondaryCategory))
            return false;
        return tertiaryCategory==null ? that.tertiaryCategory==null : tertiaryCategory.equals(that.tertiaryCategory);

    }

    @Override
    public int hashCode() {
        int result = primaryCategory != null ? primaryCategory.hashCode() : 0;
        result = 31 * result + (secondaryCategory != null ? secondaryCategory.hashCode() : 0);
        return result;
    }

    public String getName(Context context) {
        if (tertiaryCategory != null) return tertiaryCategory;
        else if (secondaryCategory != null) return secondaryCategory;
        else if (primaryCategory != null) return primaryCategory;
        else return context.getString(R.string.app_category_everything);
    }
}
