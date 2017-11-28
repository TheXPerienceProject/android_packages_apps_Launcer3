// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.smartspace;

enum SmartspaceController$Store
{
    dV("CURRENT", 1, "smartspace_current"),
    dW("WEATHER", 0, "smartspace_weather");
    //dX(new SmartspaceController$Store[] { SmartspaceController$Store.dW, SmartspaceController$Store.dV });
    
    final String filename;
    
    SmartspaceController$Store(final String s, final int n, final String filename) {
        this.filename = filename;
    }
}
