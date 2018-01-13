package com.google.android.apps.nexuslauncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Process;

import com.android.launcher3.FastBitmapDrawable;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.graphics.DrawableFactory;
import com.google.android.apps.nexuslauncher.clock.DynamicClock;

public class DynamicDrawableFactory extends DrawableFactory {
    private final DynamicClock mDynamicClockDrawer;

    public DynamicDrawableFactory(Context context) {
        mDynamicClockDrawer = new DynamicClock(context);
    }

    @Override
    public FastBitmapDrawable newIcon(Bitmap icon, final ItemInfo info) {
        if (info != null &&
                info.itemType == 0 &&
                DynamicClock.DESK_CLOCK.equals(info.getTargetComponent()) &&
                info.user.equals(Process.myUserHandle())) {
            return mDynamicClockDrawer.drawIcon(icon);
        }
        return super.newIcon(icon, info);
    }
}
