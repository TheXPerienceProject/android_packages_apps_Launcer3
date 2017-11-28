// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.qsb;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import com.android.launcher3.Launcher;
import com.google.android.apps.nexuslauncher.NexusLauncherActivity;
import com.google.android.apps.nexuslauncher.R;

public class a extends View.AccessibilityDelegate
{
    public static final String br(final Context context) {
        CharSequence charSequence = null;
        try {
            final Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication("com.google.android.googlequicksearchbox");
            final int identifier = resourcesForApplication.getIdentifier("title_google_home_screen", "string", "com.google.android.googlequicksearchbox");
            if (identifier != 0) {
                charSequence = resourcesForApplication.getString(identifier);
            }
            if (TextUtils.isEmpty(charSequence)) {
                charSequence = context.getString(R.string.title_google_app);
            }
            return context.getString(R.string.title_show_google_app, new Object[] { charSequence });
        }
        catch (PackageManager.NameNotFoundException ex) {
        }
        return charSequence.toString();
    }
    
    public void onInitializeAccessibilityNodeInfo(final View view, final AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
        if (((NexusLauncherActivity)Launcher.getLauncher(view.getContext())).dW()) {
            accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(2131493012, (CharSequence)br(view.getContext())));
        }
    }
    
    public boolean performAccessibilityAction(final View view, final int n, final Bundle bundle) {
        if (n == R.string.title_show_google_app ) {
            ((NexusLauncherActivity)Launcher.getLauncher(view.getContext())).dX();
            return true;
        }
        return super.performAccessibilityAction(view, n, bundle);
    }
}
