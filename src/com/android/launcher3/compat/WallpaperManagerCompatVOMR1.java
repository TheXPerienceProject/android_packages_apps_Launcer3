// 
// Decompiled by Procyon v0.5.30
// 

package com.android.launcher3.compat;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

@RequiresApi(26)
public class WallpaperManagerCompatVOMR1 extends WallpaperManagerCompat
{
    private static final String TAG = "WMCompatVOMR1";
    private final Method mAddOCLMethod;
    private final Class mOCLClass;
    private final Method mWCColorHintsMethod;
    private final Method mWCGetMethod;
    private final Method mWCGetPrimaryColorMethod;
    private final Method mWCGetSecondaryColorMethod;
    private final Method mWCGetTertiaryColorMethod;
    private final WallpaperManager mWm;
    
    WallpaperManagerCompatVOMR1(final Context context) {
        try {
            final int n = 1;
            this.mWm = (WallpaperManager) context.getSystemService((Class) WallpaperManager.class);
            this.mOCLClass = Class.forName("android.app.WallpaperManager.OnColorsChangedListener");
            final Class[] array = new Class[n];
            array[0] = this.mOCLClass;
            this.mAddOCLMethod = WallpaperManager.class.getDeclaredMethod("addOnColorsChangedListener", (Class<?>[]) array);
            final Class[] array2 = new Class[n];
            array2[0] = Integer.TYPE;
            this.mWCGetMethod = WallpaperManager.class.getDeclaredMethod("getWallpaperColors", (Class<?>[]) array2);
            final Class<?> returnType = this.mWCGetMethod.getReturnType();
            this.mWCGetPrimaryColorMethod = returnType.getDeclaredMethod("getPrimaryColor", (Class<?>[]) new Class[0]);
            this.mWCGetSecondaryColorMethod = returnType.getDeclaredMethod("getSecondaryColor", (Class<?>[]) new Class[0]);
            this.mWCGetTertiaryColorMethod = returnType.getDeclaredMethod("getTertiaryColor", (Class<?>[]) new Class[0]);
            this.mWCColorHintsMethod = returnType.getDeclaredMethod("getColorHints", (Class<?>[]) new Class[0]);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private WallpaperColorsCompat convertColorsObject(final Object o) {
        try {
            if (o == null) {
                return null;
            }
            final Color color = (Color) this.mWCGetPrimaryColorMethod.invoke(o, new Object[0]);
            final Color color2 = (Color) this.mWCGetSecondaryColorMethod.invoke(o, new Object[0]);
            final Color color3 = (Color) this.mWCGetTertiaryColorMethod.invoke(o, new Object[0]);
            int argb;
            if (color != null) {
                argb = color.toArgb();
            } else {
                argb = 0;
            }
            int argb2;
            if (color2 != null) {
                argb2 = color2.toArgb();
            } else {
                argb2 = 0;
            }
            int argb3;
            if (color3 != null) {
                argb3 = color3.toArgb();
            } else {
                argb3 = 0;
            }
            return new WallpaperColorsCompat(argb, argb2, argb3, (int) this.mWCColorHintsMethod.invoke(o, new Object[0]));
        }
        catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    
    public void addOnColorsChangedListener(final OnColorsChangedListenerCompat wallpaperManagerCompat$OnColorsChangedListenerCompat) {
        final int n = 1;
        final ClassLoader classLoader = WallpaperManager.class.getClassLoader();
        final Class[] array = new Class[n];
        array[0] = this.mOCLClass;
        final Object proxyInstance = Proxy.newProxyInstance(classLoader, array, new WallpaperManagerCompatVOMR1$1(this, wallpaperManagerCompat$OnColorsChangedListenerCompat));
        try {
            final Method mAddOCLMethod = this.mAddOCLMethod;
            try {
                mAddOCLMethod.invoke(this.mWm, proxyInstance);
            }
            catch (Exception ex) {
                Log.e("WMCompatVOMR1", "Error calling wallpaper API", (Throwable)ex);
            }
        }
        catch (Exception ex2) {}
    }
    
    public WallpaperColorsCompat getWallpaperColors(final int n) {
        final Object[] array = { null };
        try {
            array[0] = n;
            return this.convertColorsObject(this.mWCGetMethod.invoke(this.mWm, array));
        }
        catch (Exception ex) {
            Log.e("WMCompatVOMR1", "Error calling wallpaper API", ex);
            return null;
        }
    }

    final class WallpaperManagerCompatVOMR1$1 implements InvocationHandler
    {
        final /* synthetic */ WallpaperManagerCompatVOMR1 this$0;
        final /* synthetic */ OnColorsChangedListenerCompat val$listener;

        WallpaperManagerCompatVOMR1$1(final WallpaperManagerCompatVOMR1 this$0, final OnColorsChangedListenerCompat val$listener) {
            this.this$0 = this$0;
            this.val$listener = val$listener;
        }

        public Object invoke(final Object o, final Method method, final Object[] array) {
            final String name = method.getName();
            if ("onColorsChanged".equals(name)) {
                this.val$listener.onColorsChanged(this.this$0.convertColorsObject(array[0]), (int)array[1]);
            }
            else if ("toString".equals(name)) {
                return this.val$listener.toString();
            }
            return null;
        }
    }
}
