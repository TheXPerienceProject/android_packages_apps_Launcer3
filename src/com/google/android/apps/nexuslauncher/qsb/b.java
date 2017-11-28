// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.qsb;

import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.graphics.Rect;
import android.view.View;

import com.android.launcher3.InstallShortcutReceiver;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.ShortcutInfo;
import com.android.launcher3.compat.ShortcutConfigActivityInfo;
import com.android.launcher3.dragndrop.BaseItemDragListener;
import com.android.launcher3.userevent.nano.LauncherLogProto;
import com.android.launcher3.widget.PendingAddShortcutInfo;
import com.android.launcher3.widget.PendingItemDragHelper;

public class b extends BaseItemDragListener
{
    private final LauncherActivityInfo bS;
    
    public b(final LauncherActivityInfo bs, final Rect rect) {
        super(rect, rect.width(), rect.width());
        this.bS = bs;
    }
    
    protected PendingItemDragHelper createDragHelper() {
        final PendingAddShortcutInfo tag = new PendingAddShortcutInfo(new g(this, this.bS));
        final View view = new View((Context)this.mLauncher);
        view.setTag((Object)tag);
        return new PendingItemDragHelper(view);
    }
    
    public void fillInLogContainerData(final View view, final ItemInfo itemInfo, final LauncherLogProto.Target launcherLogProto$Target, final LauncherLogProto.Target launcherLogProto$Target2) {
    }

    final class g extends ShortcutConfigActivityInfo.ShortcutConfigActivityInfoVO
    {
        final /* synthetic */ b cs;

        g(final b cs, final LauncherActivityInfo launcherActivityInfo) {
            super(launcherActivityInfo);
            this.cs = cs;
        }

        public ShortcutInfo createShortcutInfo() {
            return InstallShortcutReceiver.fromActivityInfo(this.cs.bS, this.cs.mLauncher);
        }
    }
}
