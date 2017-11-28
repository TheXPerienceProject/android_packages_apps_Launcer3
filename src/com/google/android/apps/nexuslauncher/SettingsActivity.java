// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.preference.TwoStatePreference;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.google.android.apps.nexuslauncher.reflection.c;
import com.google.android.apps.nexuslauncher.smartspace.f;
import com.google.android.apps.nexuslauncher.a2.b;

public class SettingsActivity extends com.android.launcher3.SettingsActivity implements PreferenceFragment.OnPreferenceStartFragmentCallback
{
    @SuppressLint("ResourceType")
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            this.getFragmentManager().beginTransaction().replace(16908290, (Fragment)new SettingsActivity.MySettingsFragment()).commit();
        }
    }
    
    @SuppressLint("ResourceType")
    public boolean onPreferenceStartFragment(final PreferenceFragment preferenceFragment, final Preference preference) {
        final Fragment instantiate = Fragment.instantiate((Context)this, preference.getFragment(), preference.getExtras());
        if (instantiate instanceof DialogFragment) {
            ((DialogFragment)instantiate).show(this.getFragmentManager(), preference.getKey());
        }
        else {
            this.getFragmentManager().beginTransaction().replace(16908290, instantiate).addToBackStack(preference.getKey()).commit();
        }
        return true;
    }

    public static class MySettingsFragment extends SettingsActivity.LauncherSettingsFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener
    {
        public void onCreate(final Bundle bundle) {
            while (true) {
                super.onCreate(bundle);
                ((SwitchPreference)this.findPreference((CharSequence)"pref_show_predictions")).setOnPreferenceChangeListener((Preference.OnPreferenceChangeListener)this);
                this.findPreference((CharSequence)"pref_enable_minus_one").setTitle((CharSequence) com.google.android.apps.nexuslauncher.qsb.a.br((Context)this.getActivity()));
                Object o = "";
                final Context context = this.getContext();
                final PackageManager packageManager = context.getPackageManager();
                final Context context2 = this.getContext();
                try {
                    final PackageInfo packageInfo = packageManager.getPackageInfo(context2.getPackageName(), 0);
                    final Object versionName = packageInfo.versionName;
                    o = this.findPreference((CharSequence)"about_app_version");
                    ((Preference)o).setSummary((CharSequence)versionName);
                    if (f.get((Context)this.getActivity()).cY() ^ true) {
                        final PreferenceScreen preferenceScreen = this.getPreferenceScreen();
                        o = this.findPreference((CharSequence)"pref_smartspace");
                        preferenceScreen.removePreference((Preference)o);
                        return;
                    }
                }
                catch (PackageManager.NameNotFoundException ex) {
                    Log.e("SettingsActivity", "Unable to load my own package info", (Throwable)ex);
                    final Object versionName = o;
                }
                this.findPreference((CharSequence)"pref_smartspace").setOnPreferenceClickListener((Preference.OnPreferenceClickListener)this);
            }
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            final boolean b = true;
            if (!"pref_show_predictions".equals(preference.getKey())) {
                return false;
            }
            if (o == Boolean.TRUE) {
                c.getInstance(this.getContext()).aD(b);
                return b;
            }
            final SettingsActivity.SuggestionConfirmationFragment settingsActivity$SuggestionConfirmationFragment = new SettingsActivity.SuggestionConfirmationFragment();
            settingsActivity$SuggestionConfirmationFragment.setTargetFragment((Fragment)this, 0);
            settingsActivity$SuggestionConfirmationFragment.show(this.getFragmentManager(), preference.getKey());
            return false;
        }

        public boolean onPreferenceClick(final Preference preference) {
            if ("pref_smartspace".equals(preference.getKey())) {
                f.get(this.getContext()).cZ();
                return true;
            }
            return false;
        }

        public void onResume() {
            super.onResume();
            this.findPreference((CharSequence)"pref_enable_minus_one").setEnabled(b.dz(this.getContext()));
        }
    }

    public static class OpenSourceLicensesFragment extends DialogFragment
    {
        @SuppressLint("ResourceType")
        public Dialog onCreateDialog(final Bundle bundle) {
            final WebView view = new WebView((Context)this.getActivity());
            view.setWebViewClient(new WebViewClient());
            view.getSettings().setBuiltInZoomControls(true);
            view.loadUrl("file:///android_res/raw/license.html");
            return (Dialog)new AlertDialog.Builder((Context)this.getActivity()).setTitle(2131493024).setView((View)view).create();
        }
    }

    public static class SuggestionConfirmationFragment extends DialogFragment implements DialogInterface.OnClickListener
    {
        public void onClick(final DialogInterface dialogInterface, final int n) {
            if (this.getTargetFragment() instanceof PreferenceFragment) {
                final Preference preference = ((PreferenceFragment)this.getTargetFragment()).findPreference("pref_show_predictions");
                if (preference instanceof TwoStatePreference) {
                    ((TwoStatePreference)preference).setChecked(false);
                }
            }
            c.getInstance(this.getContext()).aD(false);
        }

        @SuppressLint("ResourceType")
        public Dialog onCreateDialog(final Bundle bundle) {
            return (Dialog)new AlertDialog.Builder(this.getContext()).setTitle(2131493008).setMessage(2131493009).setNegativeButton(17039360, (DialogInterface.OnClickListener)null).setPositiveButton(2131493010, (DialogInterface.OnClickListener)this).create();
        }
    }
}
