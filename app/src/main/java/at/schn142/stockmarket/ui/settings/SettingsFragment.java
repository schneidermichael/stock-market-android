package at.schn142.stockmarket.ui.settings;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;

import at.schn142.stockmarket.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Indicate here the XML resource you created above that holds the preferences
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }

}