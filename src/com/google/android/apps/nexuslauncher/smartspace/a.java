package com.google.android.apps.nexuslauncher.smartspace;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.android.launcher3.LauncherAppState;
import com.android.launcher3.graphics.LauncherIcons;
import com.google.android.apps.nexuslauncher.smartspace.b_package.b;

public class a {
    public final b di;
    public final boolean dj;
    public final PackageInfo dk;
    public final long dl;
    public final Intent intent;

    public a(b di, Intent intent, boolean dj, long dl, PackageInfo dk) {
        this.di = di;
        this.dj = dj;
        this.intent = intent;
        this.dl = dl;
        this.dk = dk;
    }

    private static Object ch(String s, Intent intent) {
        if (!TextUtils.isEmpty(s)) {
            return intent.getParcelableExtra(s);
        }
        return null;
    }

    public Bitmap ci(final Context context) {
        com.google.android.apps.nexuslauncher.smartspace.b_package.f fVar = this.di.cx;
        if (fVar == null) {
            return null;
        }
        Bitmap bitmap = (Bitmap) ch(fVar.cV, this.intent);
        if (bitmap != null) {
            return bitmap;
        }
        try {
            if (TextUtils.isEmpty(fVar.cW)) {
                if (!TextUtils.isEmpty(fVar.cX)) {
                    Resources resourcesForApplication = context.getPackageManager().getResourcesForApplication("com.google.android.googlequicksearchbox");
                    return LauncherIcons.createIconBitmap(resourcesForApplication.getDrawableForDensity(resourcesForApplication.getIdentifier(fVar.cX, null, null), LauncherAppState.getIDP(context).fillResIconDpi), context);
                }
                return null;
            }
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(fVar.cW));
        } catch (Exception e) {
            Log.e("NewCardInfo", "retrieving bitmap uri=" + fVar.cW + " gsaRes=" + fVar.cX);
            return null;
        }
    }
}
