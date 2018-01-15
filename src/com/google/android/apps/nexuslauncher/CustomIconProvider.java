package com.google.android.apps.nexuslauncher;

import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;

import com.android.launcher3.IconProvider;
import com.android.launcher3.Utilities;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class CustomIconProvider extends IconProvider {
    private final Context mContext;

    public CustomIconProvider(Context context) {
        super();
        mContext = context;
    }

    @Override
    public Drawable getIcon(LauncherActivityInfo launcherActivityInfo, int iconDpi, boolean flattenDrawable) {
        Drawable drawable = super.getIcon(launcherActivityInfo, iconDpi, flattenDrawable);
        if (!Utilities.ATLEAST_OREO || !(drawable instanceof AdaptiveIconDrawable)) {
            Drawable roundIcon = getRoundIcon(launcherActivityInfo.getApplicationInfo().packageName, iconDpi);
            if (roundIcon != null) {
                drawable = roundIcon;
            }
        }
        return drawable;
    }

    private Drawable getRoundIcon(String packageName, int iconDpi) {
        try {
            Resources resourcesForApplication = mContext.getPackageManager().getResourcesForApplication(packageName);
            AssetManager assets = resourcesForApplication.getAssets();
            XmlResourceParser parseXml = assets.openXmlResourceParser("AndroidManifest.xml");
            int eventType;
            while ((eventType = parseXml.nextToken()) != XmlPullParser.END_DOCUMENT)
                if (eventType == XmlPullParser.START_TAG && parseXml.getName().equals("application"))
                    for (int i = 0; i < parseXml.getAttributeCount(); i++)
                        if (parseXml.getAttributeName(i).equals("roundIcon"))
                            return resourcesForApplication.getDrawableForDensity(Integer.parseInt(parseXml.getAttributeValue(i).substring(1)), iconDpi);
            parseXml.close();
        }
        catch (PackageManager.NameNotFoundException | IOException | XmlPullParserException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
