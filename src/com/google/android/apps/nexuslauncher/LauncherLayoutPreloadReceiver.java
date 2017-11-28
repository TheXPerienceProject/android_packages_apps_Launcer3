// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.shortcuts.DeepShortcutManager;

public class LauncherLayoutPreloadReceiver extends BroadcastReceiver
{
    public void onReceive(final Context context, final Intent intent) {
        Log.d("SUWFinishReceiver", "Received preload request");
        if (!this.isOrderedBroadcast() || (DeepShortcutManager.getInstance(context).hasHostPermission() ^ true)) {
            Log.d("SUWFinishReceiver", "Skipping preload");
            return;
        }
        Log.d("SUWFinishReceiver", "Trying to preload launcher");
        LauncherAppState.getInstance(context).getModel().enqueueModelUpdateTask(new c(this.goAsync()));
    }
}
