package subreddit.android.appstore.backend;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;
import java.util.Collection;
import java.util.LinkedHashSet;


public class AppInfo implements Parcelable, Comparable<AppInfo> {


    public enum Tag {
        // Price
        FREE, PAID, ADS, IAP,
        // Format
        PHONE, TABLET, WEAR,
        // Categories
        APP, GAME
        // Additional

    }

    String contact;
    String description;
    String appName;
    URL targetLink;

    public AppInfo() {
    }

    public String getAppName() {
        return appName;
    }

    public String getDescription() {
        return description;
    }

    public String getContact() {
        return contact;
    }

    public URL getTargetLink() {
        return targetLink;
    }

    final Collection<Tag> tagCollection = new LinkedHashSet<>();

    public Collection<Tag> getTags() {
        return tagCollection;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(appName);
        dest.writeString(description);
        dest.writeString(contact);
    }

    protected AppInfo(Parcel in) {
        appName = in.readString();
        description = in.readString();
        contact = in.readString();
    }

    public static final Creator<AppInfo> CREATOR = new Creator<AppInfo>() {
        @Override
        public AppInfo createFromParcel(Parcel in) {
            return new AppInfo(in);
        }

        @Override
        public AppInfo[] newArray(int size) {
            return new AppInfo[size];
        }
    };

    @Override
    public int compareTo(AppInfo appInfo) {
        return appName.compareTo(appInfo.appName);
    }
}

