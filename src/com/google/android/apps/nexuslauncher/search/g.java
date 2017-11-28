// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.search;

import android.graphics.Bitmap;

import com.android.launcher3.util.ComponentKey;

import java.util.concurrent.Callable;

class g implements Callable
{
    final ComponentKey eO;
    final /* synthetic */ AppSearchProvider eP;
    
    public g(final AppSearchProvider ep, final ComponentKey eo) {
        this.eP = ep;
        this.eO = eo;
    }
    
    public Bitmap call() {
        final d d = new d(this.eO);
        this.eP.mApp.getIconCache().getTitleAndIcon(d, false);
        return d.iconBitmap;
    }
}
