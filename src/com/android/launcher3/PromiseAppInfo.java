package com.android.launcher3;

import android.content.Intent;

import com.android.launcher3.compat.PackageInstallerCompat;
import com.android.launcher3.util.PackageManagerHelper;

public class PromiseAppInfo extends AppInfo
{
    public int level;
    
    public PromiseAppInfo(final PackageInstallerCompat.PackageInstallInfo packageInstallerCompat$PackageInstallInfo) {
        this.level = 0;
        this.componentName = packageInstallerCompat$PackageInstallInfo.componentName;
        this.intent = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.LAUNCHER").setComponent(this.componentName).setFlags(270532608);
    }
    
    public Intent getMarketIntent() {
        return PackageManagerHelper.getMarketIntent(this.componentName.getPackageName());
    }
    
    public ShortcutInfo makeShortcut() {
        final ShortcutInfo shortcutInfo = new ShortcutInfo(this);
        shortcutInfo.setInstallProgress(this.level);
        shortcutInfo.status |= 0x2;
        shortcutInfo.status |= 0x8;
        return shortcutInfo;
    }
}
