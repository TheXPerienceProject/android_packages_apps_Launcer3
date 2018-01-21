package com.google.android.apps.nexuslauncher;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.util.Log;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.LauncherModel;
import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.android.launcher3.util.LooperExecutor;
import com.google.android.apps.nexuslauncher.smartspace.f;

public class SettingsActivity extends com.android.launcher3.SettingsActivity implements PreferenceFragment.OnPreferenceStartFragmentCallback {
    public final static String ICON_PACK_PREF = "pref_icon_pack";
    public final static String SHOW_PREDICTIONS_PREF = "pref_show_predictions";
    public final static String ENABLE_MINUS_ONE_PREF = "pref_enable_minus_one";
    public final static String SMARTSPACE_PREF = "pref_smartspace";
    public final static String APP_VERSION_PREF = "about_app_version";

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

    public static class MySettingsFragment extends com.android.launcher3.SettingsActivity.LauncherSettingsFragment
            implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {
        private CustomIconsPreference mIconPackPref;
        private Context mContext;

        @Override
        public void onCreate(Bundle bundle) {
            super.onCreate(bundle);

            mContext = getActivity();

            findPreference(SHOW_PREDICTIONS_PREF).setOnPreferenceChangeListener(this);
            findPreference(ENABLE_MINUS_ONE_PREF).setTitle(getDisplayGoogleTitle());

            PackageManager packageManager = mContext.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
                findPreference(APP_VERSION_PREF).setSummary(packageInfo.versionName);
                if (com.google.android.apps.nexuslauncher.smartspace.f.get(mContext).cY()) {
                    findPreference(SMARTSPACE_PREF).setOnPreferenceClickListener(this);
                } else {
                    getPreferenceScreen().removePreference(findPreference("pref_smartspace"));
                }
            } catch (PackageManager.NameNotFoundException ex) {
                Log.e("SettingsActivity", "Unable to load my own package info", ex);
            }

            mIconPackPref = (CustomIconsPreference) findPreference(ICON_PACK_PREF);
            mIconPackPref.setOnPreferenceChangeListener(this);

            //ToDo: Add
            getPreferenceScreen().removePreference(findPreference(SHOW_PREDICTIONS_PREF));
        }

        private String getDisplayGoogleTitle() {
            CharSequence charSequence = null;
            try {
                Resources resourcesForApplication = mContext.getPackageManager().getResourcesForApplication("com.google.android.googlequicksearchbox");
                int identifier = resourcesForApplication.getIdentifier("title_google_home_screen", "string", "com.google.android.googlequicksearchbox");
                if (identifier != 0) {
                    charSequence = resourcesForApplication.getString(identifier);
                }
            }
            catch (PackageManager.NameNotFoundException ex) {
            }
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = mContext.getString(R.string.title_google_app);
            }
            return mContext.getString(R.string.title_show_google_app, charSequence);
        }

        @Override
        public void onResume() {
            super.onResume();
            mIconPackPref.reloadIconPacks();
        }

        @Override
        public boolean onPreferenceChange(Preference preference, final Object newValue) {
            switch (preference.getKey()) {
                case ICON_PACK_PREF:
                    ProgressDialog.show(mContext,
                            null /* title */,
                            mContext.getString(R.string.state_loading),
                            true /* indeterminate */,
                            false /* cancelable */);

                    new LooperExecutor(LauncherModel.getWorkerLooper()).execute(new Runnable() {
                        @SuppressLint("ApplySharedPref")
                        @Override
                        public void run() {
                            // Clear the icon cache.
                            LauncherAppState.getInstance(mContext).getIconCache().clear();

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
                                        .setPackage(mContext.getPackageName())
                                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                PendingIntent pi = PendingIntent.getActivity(mContext, 0,
                                        homeIntent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_ONE_SHOT);
                                getContext().getSystemService(AlarmManager.class).setExact(
                                        AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 50, pi);
                            }

                            // Kill process
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    });
                    return true;
                case SHOW_PREDICTIONS_PREF:
                    //ToDo: Toggle
                    break;
            }
            return false;
        }

        @Override
        public boolean onPreferenceClick(Preference preference) {
            if (SMARTSPACE_PREF.equals(preference.getKey())) {
                f.get(mContext).cZ();
                return true;
            }
            return false;
        }
    }
}
