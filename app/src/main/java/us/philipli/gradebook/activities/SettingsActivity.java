package us.philipli.gradebook.activities;

import android.app.Activity;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import us.philipli.gradebook.R;

public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction().replace(android.R.id.content,
                new PrefsFragment()).commit();
    }

    public static class PrefsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preferences);

            EditTextPreference nameEditText = (EditTextPreference) findPreference("name");
            nameEditText.setSummary(nameEditText.getText());
            EditTextPreference schoolEditText = (EditTextPreference) findPreference("school");
            schoolEditText.setSummary(schoolEditText.getText());

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

