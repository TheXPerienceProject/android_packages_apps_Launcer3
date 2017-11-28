// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.smartspace;

import android.view.View;

import com.android.launcher3.AbstractFloatingView;
import com.android.launcher3.Launcher;

final class g implements View.OnClickListener
{
    final /* synthetic */ b dY;
    final /* synthetic */ Launcher val$launcher;
    
    g(final b dy, final Launcher val$launcher) {
        this.dY = dy;
        this.val$launcher = val$launcher;
    }
    
    public void onClick(final View view) {
        f.get(view.getContext()).cZ();
        AbstractFloatingView.closeAllOpenViews(this.val$launcher);
    }
}
