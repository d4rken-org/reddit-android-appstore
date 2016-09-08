package subreddit.android.appstore.backend.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import subreddit.android.appstore.AppStoreApp;
import timber.log.Timber;


public class AppInfo implements Comparable<AppInfo> {
    final static String TAG = AppStoreApp.LOGPREFIX + "AppInfo";

    final Collection<AppTags> appTagsCollection = new LinkedHashSet<>();
    final Collection<Contact> contacts = new ArrayList<>();
    final Collection<Download> downloads = new ArrayList<>();
    String description;
    String appName;
    String primaryCategory;
    String secondaryCategory;

    public AppInfo() {
    }

    @NonNull
    public String getPrimaryCategory() {
        return primaryCategory;
    }

    public void setPrimaryCategory(@NonNull String primaryCategory) {
        this.primaryCategory = primaryCategory;
    }

    @NonNull
    public String getSecondaryCategory() {
        return secondaryCategory;
    }

    public void setSecondaryCategory(@NonNull String secondaryCategory) {
        this.secondaryCategory = secondaryCategory;
    }

    public String getAppName() {
        return appName;
    }

    public String getDescription() {
        return description;
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

    public void addContact(Contact contact) {
        this.contacts.add(contact);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void addDownload(Download download) {
        this.downloads.add(download);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this);
    }

    @Nullable
    public static AppInfo fromJson(String jsonString) {
        if (jsonString == null) return null;
        Gson gson = new GsonBuilder().create();
        try {
            return gson.fromJson(jsonString, AppInfo.class);
        } catch (JsonSyntaxException e) {
            Timber.tag(TAG).e(e, "Failed to create AppInfo from: %s", jsonString);
            return null;
        }
    }

    @Override
    public int compareTo(@NonNull AppInfo appInfo) {
        return appName.compareTo(appInfo.appName);
    }
}

