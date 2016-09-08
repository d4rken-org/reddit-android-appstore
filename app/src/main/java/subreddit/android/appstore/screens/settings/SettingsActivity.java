package subreddit.android.appstore.screens.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import subreddit.android.appstore.AppStoreApp;
import subreddit.android.appstore.R;
import subreddit.android.appstore.util.ui.BaseActivity;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
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
            getFragmentManager().beginTransaction().replace(R.id.settings_frame, fragment).commit();
        }
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    public static class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);
            findPreference("about").setOnPreferenceClickListener(this);
            findPreference("night_mode").setOnPreferenceClickListener(this);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            switch (preference.getKey()) {
                case "about": {
                    startActivity(new Intent(getActivity(), AboutActivity.class));
                    break;
                }
                case "night_mode": {
                    new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.restart)
                            .setNegativeButton(R.string.later, null)
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ((AppStoreApp) getActivity().getApplication()).restart();
                                }
                            })
                            .show();
                }
            }

            return true;
        }
    }
}
