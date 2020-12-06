package at.schn142.stockmarket.ui.settings;

import android.content.DialogInterface;
import android.os.Bundle;


import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import at.schn142.stockmarket.R;
import at.schn142.stockmarket.ViewModel;

/**
 * This class represents SettingsFragment
 * Show the Settings
 *
 * @author michaelschneider
 * @version 1.0
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    private ViewModel mViewModel;

    AlertDialog.Builder builder;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }

    @Override
    public void onActivityCreated(Bundle _savedInstanceState) {
        super.onActivityCreated(_savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ViewModel.class);
        builder = new AlertDialog.Builder(getContext());

        Preference preference = findPreference(getString(R.string.delete_all));
        preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preferenceClick) {

                builder.setMessage(R.string.dialog_delete_all_message)
                        .setCancelable(false)
                        .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                mViewModel.deleteAll();
                            }
                        })
                        .setNegativeButton(R.string.dialog_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.setTitle(R.string.preference_delete_all);
                alert.show();

                return true;
            }
        });
    }



}










