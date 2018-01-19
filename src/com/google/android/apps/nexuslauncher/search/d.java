package com.google.android.apps.nexuslauncher.search;

import android.content.Intent;

import com.android.launcher3.ItemInfoWithIcon;
import com.android.launcher3.util.ComponentKey;

public class d extends ItemInfoWithIcon {
    private Intent mIntent;

    public d(final ComponentKey componentKey) {
        this.mIntent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER)
                .setComponent(componentKey.componentName)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        this.user = componentKey.user;
        this.itemType = 0;
    }

    public Intent getIntent() {
        return this.mIntent;
    }
}
