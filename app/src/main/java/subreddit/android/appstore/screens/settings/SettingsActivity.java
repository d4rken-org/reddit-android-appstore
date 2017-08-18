package subreddit.android.appstore.screens.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.BaseActivity;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    public static final String PREF_KEY_LOAD_MEDIA = "core.data.loadmedia";
    protected static final String SUBMIT_APP_URL = "https://androidflair.github.io/wikiapps/";
    @BindView(R.id.settings_toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_48px);
        mToolbar.setNavigationOnClickListener(this);

        if (savedInstanceState == null) {
            SettingsFragment fragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.settings_frame, fragment).commit();
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    public static class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.preferences);
            findPreference("about").setOnPreferenceClickListener(this);
            findPreference("submitapp").setOnPreferenceClickListener(this);
            findPreference("theme").setOnPreferenceChangeListener(this);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.restart)
                    .setNegativeButton(R.string.later, null)
                    .setPositiveButton(
                            android.R.string.ok,
                            (dialogInterface, i) -> ((AppStoreApp) getActivity().getApplication()).restart())
                    .show();

            return true;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (preference.getKey().equals("about"))
                startActivity(new Intent(getActivity(), AboutActivity.class));
            else if (preference.getKey().equals("submitapp")) {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                builder.setSecondaryToolbarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(getActivity(), Uri.parse(SUBMIT_APP_URL));
            }
            return true;
        }


    }
}
