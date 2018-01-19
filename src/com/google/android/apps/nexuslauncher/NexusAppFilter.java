package com.google.android.apps.nexuslauncher;

import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

import com.android.launcher3.AppFilter;

import java.util.HashSet;
import java.util.Set;

public class NexusAppFilter extends AppFilter {
    private final HashSet<ComponentName> mHideList = new HashSet<>();
    private final Context mContext;

    public NexusAppFilter(Context context) {
        mContext = context;

        //Voice Search
        mHideList.add(ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/.VoiceSearchActivity"));

        //Wallpapers
        mHideList.add(ComponentName.unflattenFromString("com.google.android.apps.wallpaper/.picker.CategoryPickerActivity"));

        //Google Now Launcher
        mHideList.add(ComponentName.unflattenFromString("com.google.android.launcher/com.google.android.launcher.StubApp"));
    }

    @Override
    public boolean shouldShowApp(ComponentName componentName) {
        String packageName = componentName.getPackageName();
        return !mHideList.contains(componentName) && !CustomIconUtils.isPackProvider(mContext, packageName);
    }
}
