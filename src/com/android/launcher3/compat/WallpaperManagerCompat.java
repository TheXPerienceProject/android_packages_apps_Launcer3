// 
// Decompiled by Procyon v0.5.30
// 

package com.android.launcher3.compat;

import android.content.Context;

import com.android.launcher3.Utilities;

public abstract class WallpaperManagerCompat
{
    private static WallpaperManagerCompat sInstance;
    private static final Object sInstanceLock;
    
    static {
        sInstanceLock = new Object();
    }
    
    public static WallpaperManagerCompat getInstance(final Context p0) {
        synchronized (WallpaperManagerCompat.sInstanceLock) {
            if (sInstance == null) {
                if (Utilities.isAtLeastO())
                    sInstance = new WallpaperManagerCompatVOMR1(p0);
                else
                    sInstance = new WallpaperManagerCompatVL(p0);
            }

            return sInstance;
        }
    }
    
    public abstract void addOnColorsChangedListener(final OnColorsChangedListenerCompat p0);
    
    public abstract WallpaperColorsCompat getWallpaperColors(final int p0);

    public interface OnColorsChangedListenerCompat
    {
        void onColorsChanged(final WallpaperColorsCompat p0, final int p1);
    }
}
