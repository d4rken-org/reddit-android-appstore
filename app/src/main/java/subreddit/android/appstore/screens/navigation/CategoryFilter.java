package subreddit.android.appstore.screens.navigation;


import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class CategoryFilter implements Parcelable {
    private final String primaryCategory;
    private final String secondaryCategory;

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

}
