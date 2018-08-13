package subreddit.android.appstore.backend.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import timber.log.Timber;

public class AppInfo implements Comparable<AppInfo> {
    private final Collection<AppTags> appTagsCollection = new LinkedHashSet<>();
    private final Collection<Contact> contacts = new ArrayList<>();
    private final Collection<Download> downloads = new ArrayList<>();
    private String description;
    private String appName;
    private List<String> categories;

    public AppInfo() {
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getPrimaryCategory() {
        return categories.get(0);
    }

    public String getSecondaryCategory() {
        return categories.get(1);
    }

    public String getTertiaryCategory() {
        return categories.get(2);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Collection<Contact> getContacts() {
        return contacts;
    }

    public Collection<Download> getDownloads() {
        return downloads;
    }

    public Collection<AppTags> getTags() {
        return appTagsCollection;
    }

    public void addTag(AppTags tag) {
        if (!appTagsCollection.contains(tag)) {
            appTagsCollection.add(tag);
        }
    }

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public void addDownload(Download download) {
        this.downloads.add(download);
    }

    public String toJson(Gson gson) {
        return gson.toJson(this);
    }

    @Nullable
    public static AppInfo fromJson(Gson gson, String jsonString) {
        if (jsonString == null) return null;
        try {
            return gson.fromJson(jsonString, AppInfo.class);
        } catch (JsonSyntaxException e) {
            Timber.e(e, "Failed to create AppInfo from: %s", jsonString);
            return null;
        }
    }

    @Override
    public int compareTo(@NonNull AppInfo appInfo) {
        return appName.compareToIgnoreCase(appInfo.appName);
    }

    @Override
    public String toString() {
        return getAppName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AppInfo appInfo = (AppInfo) o;

        return downloads.equals(appInfo.downloads) && appName.equals(appInfo.appName);
    }

    @Override
    public int hashCode() {
        int result = downloads.hashCode();
        result = 31 * result + appName.hashCode();
        return result;
    }
}