// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.smartspace;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.util.Log;

import com.google.android.apps.nexuslauncher.smartspace.b2.b;

public class SmartspaceBroadcastReceiver extends BroadcastReceiver
{
    private void cg(final b b, final Context context, final Intent intent, final boolean b2) {
        PackageInfo packageInfo = null;
        if (b.cy) {
            f.get(context).cV(null);
            return;
        }
        final long uptimeMillis = SystemClock.uptimeMillis();
        while (true) {
            try {
                packageInfo = context.getPackageManager().getPackageInfo("com.google.android.googlequicksearchbox", 0);
                f.get(context).cV(new a(b, intent, b2, uptimeMillis, packageInfo));
            }
            catch (PackageManager.NameNotFoundException ex) {
                continue;
            }
            break;
        }
    }
    
    public void onReceive(final Context context, final Intent intent) {
        final boolean b = true;
        final byte[] byteArrayExtra = intent.getByteArrayExtra("com.google.android.apps.nexuslauncher.extra.SMARTSPACE_CARD");
        if (byteArrayExtra != null) {
            final com.google.android.apps.nexuslauncher.smartspace.b2.a a = new com.google.android.apps.nexuslauncher.smartspace.b2.a();
            com.google.protobuf.nano.a.mergeFrom(a, byteArrayExtra);
            final b[] cw = a.cw;
            final int length = cw.length;
            int i = 0;
            while (i < length) {
                final b b2 = cw[i];
                final boolean b3 = b2.cz == (b ? 1 : 0) && b;
                final boolean b4 = b2.cz == 2 && b;
                if (b3 || b4) {
                    this.cg(b2, context, intent, b3);
                }
                else {
                    Log.w("SmartspaceReceiver", "unrecognized card priority");
                }
                ++i;
            }
        }
        else {
            Log.e("SmartspaceReceiver", "receiving update with no proto: " + intent.getExtras());
        }
    }
}
