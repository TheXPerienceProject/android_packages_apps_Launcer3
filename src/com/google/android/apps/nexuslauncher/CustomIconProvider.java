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
import java.util.HashMap;
import java.util.Map;

public class CustomIconProvider extends DynamicIconProvider {
    private final Context mContext;
    private final String mIconPack;
    private final Map<String, Integer> mIconPackComponents = new HashMap<>();

    public CustomIconProvider(Context context) {
        super(context);
        mContext = context;

        mIconPack = Utilities.getPrefs(mContext).getString(SettingsActivity.ICON_PACK_PREF, "");
        mIconPackComponents.clear();

        if (CustomIconUtils.isPackProvider(mContext, mIconPack)) {
            PackageManager pm = mContext.getPackageManager();
            try {
                Resources res = pm.getResourcesForApplication(mIconPack);
                int resId = res.getIdentifier("appfilter", "xml", mIconPack);
                if (resId != 0) {
                    XmlResourceParser parseXml = pm.getXml(mIconPack, resId, null);
                    while (parseXml.next() != XmlPullParser.END_DOCUMENT) {
                        if (parseXml.getEventType() == XmlPullParser.START_TAG && parseXml.getName().equals("item")) {
                            String componentName = parseXml.getAttributeValue(null, "component");
                            String drawableName = parseXml.getAttributeValue(null, "drawable");
                            if (componentName != null && drawableName != null) {
                                int drawableId = res.getIdentifier(drawableName, "drawable", mIconPack);
                                if (drawableId != 0) {
                                    mIconPackComponents.put(componentName, drawableId);
                                }
                            }
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException | XmlPullParserException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Drawable getIcon(LauncherActivityInfo launcherActivityInfo, int iconDpi, boolean flattenDrawable) {
        String component = launcherActivityInfo.getComponentName().toString();
        if (mIconPackComponents.containsKey(component)) {
            try {
                return mContext.getPackageManager().getDrawable(mIconPack, mIconPackComponents.get(component), null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        Drawable drawable = super.getIcon(launcherActivityInfo, iconDpi, flattenDrawable);
        if ((!Utilities.ATLEAST_OREO || !(drawable instanceof AdaptiveIconDrawable)) &&
                !"com.google.android.calendar".equals(launcherActivityInfo.getApplicationInfo().packageName)) {
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
            while (parseXml.next() != XmlPullParser.END_DOCUMENT)
                if (parseXml.getEventType() == XmlPullParser.START_TAG && parseXml.getName().equals("application"))
                    for (int i = 0; i < parseXml.getAttributeCount(); i++)
                        if (parseXml.getAttributeName(i).equals("roundIcon"))
                            return resourcesForApplication.getDrawableForDensity(Integer.parseInt(parseXml.getAttributeValue(i).substring(1)), iconDpi);
            parseXml.close();
        } catch (PackageManager.NameNotFoundException | IOException | XmlPullParserException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
