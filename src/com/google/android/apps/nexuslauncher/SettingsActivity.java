package com.google.android.apps.nexuslauncher;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.util.Log;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.LauncherModel;
import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.android.launcher3.graphics.DrawableFactory;
import com.android.launcher3.graphics.IconShapeOverride;
import com.android.launcher3.util.LooperExecutor;

import java.util.HashMap;
import java.util.Map;

import static com.android.launcher3.Utilities.getDevicePrefs;

public class SettingsActivity extends com.android.launcher3.SettingsActivity implements PreferenceFragment.OnPreferenceStartFragmentCallback {
    public final static String ICON_PACK_PREF = "pref_icon_pack";

    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            getFragmentManager().beginTransaction().replace(android.R.id.content, new MySettingsFragment()).commit();
        }
    }

    @Override
    public boolean onPreferenceStartFragment(PreferenceFragment preferenceFragment, Preference preference) {
        Fragment instantiate = Fragment.instantiate(this, preference.getFragment(), preference.getExtras());
        if (instantiate instanceof DialogFragment) {
            ((DialogFragment) instantiate).show(getFragmentManager(), preference.getKey());
        } else {
            getFragmentManager().beginTransaction().replace(android.R.id.content, instantiate).addToBackStack(preference.getKey()).commit();
        }
        return true;
    }

    public static class MySettingsFragment extends com.android.launcher3.SettingsActivity.LauncherSettingsFragment implements Preference.OnPreferenceChangeListener {
        private ListPreference mIconPackPref;

        @Override
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);

            mIconPackPref = (ListPreference) findPreference(ICON_PACK_PREF);
            mIconPackPref.setOnPreferenceChangeListener(this);
        }

        @Override
        public void onResume() {
            super.onResume();

            HashMap<String, CharSequence> packList = CustomIconUtils.getPackProviders(getContext());

            CharSequence[] keys = new String[packList.size() + 1];
            keys[0] = getContext().getResources().getString(R.string.icon_shape_system_default);

            CharSequence[] values = new String[keys.length];
            values[0] = "";

            int i = 1;
            for (Map.Entry<String, CharSequence> entry : packList.entrySet()) {
                keys[i] = entry.getValue();
                values[i++] = entry.getKey();
            }

            mIconPackPref.setEntries(keys);
            mIconPackPref.setEntryValues(values);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, final Object newValue) {
            SharedPreferences pref = getPreferenceManager().getSharedPreferences();
            if (!pref.getString(ICON_PACK_PREF, "").equals(newValue)) {
                ProgressDialog.show(getContext(),
                        null /* title */,
                        getContext().getString(R.string.state_loading),
                        true /* indeterminate */,
                        false /* cancelable */);

                new LooperExecutor(LauncherModel.getWorkerLooper()).execute(new Runnable() {
                    @SuppressLint("ApplySharedPref")
                    @Override
                    public void run() {
                        // Clear the icon cache.
                        LauncherAppState.getInstance(getContext()).getIconCache().clear();

                        // Wait for it
                        try {
                            Thread.sleep(1000);
                        } catch (Exception e) {
                            Log.e("SettingsActivity", "Error waiting", e);
                        }

                        if (Utilities.ATLEAST_MARSHMALLOW) {
                            // Schedule an alarm before we kill ourself.
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN)
                                    .addCategory(Intent.CATEGORY_HOME)
                                    .setPackage(getContext().getPackageName())
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            PendingIntent pi = PendingIntent.getActivity(getContext(), 0,
                                    homeIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                            getContext().getSystemService(AlarmManager.class).setExact(
                                    AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 50, pi);
                        }

                        // Kill process
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

                return true;
            }
            return false;
        }

        private static class OverrideApplyHandler implements Runnable {

            private final Context mContext;
            private final String mValue;

            private OverrideApplyHandler(Context context, String value) {
                mContext = context;
                mValue = value;
            }

            @Override
            public void run() {

            }
        }
    }
}
