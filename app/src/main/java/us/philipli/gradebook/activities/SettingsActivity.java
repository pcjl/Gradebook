package us.philipli.gradebook.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import us.philipli.gradebook.R;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(R.id.settings_content,
                new PrefsFragment()).commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

            EditTextPreference nameEditText = (EditTextPreference) findPreference("name");
            nameEditText.setSummary(preferences.getString("name", "DEFAULT"));
            EditTextPreference schoolEditText = (EditTextPreference) findPreference("school");
            schoolEditText.setSummary(preferences.getString("school", "DEFAULT"));

            Preference.OnPreferenceChangeListener prefListener = new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    preference.setSummary((String) newValue);
                    return true;
                }
            };

            nameEditText.setOnPreferenceChangeListener(prefListener);
            schoolEditText.setOnPreferenceChangeListener(prefListener);
        }
    }
}

