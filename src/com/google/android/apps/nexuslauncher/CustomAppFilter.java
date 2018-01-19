package com.google.android.apps.nexuslauncher;

import android.content.ComponentName;
import android.content.Context;

public class CustomAppFilter extends NexusAppFilter {
    private final Context mContext;

    public CustomAppFilter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public boolean shouldShowApp(ComponentName componentName) {
        return super.shouldShowApp(componentName) &&
                !CustomIconUtils.isPackProvider(mContext, componentName.getPackageName());
    }
}
