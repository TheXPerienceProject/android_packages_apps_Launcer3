// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.libraries.a.a;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.libraries.launcherclient.ILauncherOverlay;
import com.google.android.libraries.launcherclient.ILauncherOverlayCallback$Stub;

public class d
{
    private static int LY;
    private int LN;
    private final Activity LO;
    private Bundle LP;
    private WindowManager.LayoutParams LQ;
    private int LR;
    private final b LS;
    private final g LT;
    private e LU;
    private final h LV;
    protected ILauncherOverlay LW;
    private int LX;
    private final BroadcastReceiver LZ;
    private boolean mDestroyed;
    
    static {
        d.LY = -1;
    }
    
    public d(final Activity lo, final b ls, final f f) {
        final int n = 19;
        this.LZ = new j(this);
        this.LN = 0;
        this.mDestroyed = false;
        this.LX = 0;
        this.LO = lo;
        this.LS = ls;
        this.LV = new h((Context)lo, 65);
        this.LR = f.Mf;
        this.LT = g.get((Context)lo);
        this.LW = this.LT.RN(this);
        final IntentFilter intentFilter = new IntentFilter("android.intent.action.PACKAGE_ADDED");
        intentFilter.addDataScheme("package");
        if (Build.VERSION.SDK_INT >= n) {
            intentFilter.addDataSchemeSpecificPart("com.google.android.googlequicksearchbox", 0);
        }
        this.LO.registerReceiver(this.LZ, intentFilter);
        if (d.LY < 1) {
            Ru((Context)lo);
        }
        this.Ry();
        if (Build.VERSION.SDK_INT >= n && this.LO.getWindow() != null && this.LO.getWindow().peekDecorView() != null && this.LO.getWindow().peekDecorView().isAttachedToWindow()) {
            this.onAttachedToWindow();
        }
    }
    
    static Intent RC(final Context context) {
        final String packageName = context.getPackageName();
        return new Intent("com.android.launcher3.WINDOW_OVERLAY").setPackage("com.google.android.googlequicksearchbox").setData(Uri.parse(new StringBuilder(String.valueOf(packageName).length() + 18).append("app://").append(packageName).append(":").append(Process.myUid()).toString()).buildUpon().appendQueryParameter("v", Integer.toString(7)).build());
    }
    
    private void RE(final int lx) {
        final boolean b = true;
        boolean b2 = false;
        if (this.LX != lx) {
            this.LX = lx;
            final b ls = this.LS;
            final boolean b3 = (lx & 0x1) != 0x0 && b;
            if ((lx & 0x2) != 0x0) {
                b2 = b;
            }
            ls.et(b3, b2);
        }
    }
    
    private void RF(final boolean b) {
        if (!this.mDestroyed) {
            this.LO.unregisterReceiver(this.LZ);
        }
        this.mDestroyed = true;
        this.LV.RS();
        if (this.LU != null) {
            this.LU.clear();
            this.LU = null;
        }
        this.LT.RL(this, b);
    }
    
    private void Rm() {
        /*if (this.LW != null) {
            ILauncherOverlay lw5 = null;
            Label_0184: {
                if (this.LU == null) {
                    break Label_0184;
                }
                Label_0019:
                while (true) {
                    this.LU.RJ(this);
                    Label_0200: {
                        if (d.LY < 3) {
                            break Label_0200;
                        }
                        final Bundle bundle = new Bundle();
                        bundle.putParcelable("layout_params", (Parcelable)this.LQ);
                        final Activity lo = this.LO;
                        final Resources resources = lo.getResources();
                        bundle.putParcelable("configuration", (Parcelable)resources.getConfiguration());
                        bundle.putInt("client_options", this.LR);
                        Label_0238: {
                            if (this.LP != null) {
                                break Label_0238;
                            }
                            Label_0127:
                            while (true) {
                                final ILauncherOverlay lw = this.LW;
                                try {
                                    lw.windowAttached2(bundle, this.LU);
                                    while (true) {
                                        Label_0253: {
                                            if (d.LY < 4) {
                                                break Label_0253;
                                            }
                                            final ILauncherOverlay lw2 = this.LW;
                                            lw2.setActivityState(this.LN);
                                            final e lu = new e();
                                            this.LU = lu;
                                            //continue Label_0019;
                                            final ILauncherOverlay lw3 = this.LW;
                                            final WindowManager.LayoutParams lq = this.LQ;
                                            final e lu2 = this.LU;
                                            try {
                                                lw3.windowAttached(lq, lu2, this.LR);
                                                //continue;
                                                final ILauncherOverlay lw4 = this.LW;
                                                lw4.onPause();
                                                lw5 = this.LW;
                                                bundle.putAll(this.LP);
                                                break;
                                                //continue Label_0127;
                                            }
                                            // iftrue(Label_0278:, this.LN & 0x2 != 0x0)
                                            catch (RemoteException ex2) {}
                                        }
                                    }
                                }
                                catch (RemoteException ex8) {}
                                break;
                            }
                        }
                        break;
                    }
                    break;
                }
            }
            lw5.onResume();
        }*/
    }
    
    private boolean Rq() {
        return this.LW != null;
    }
    
    private static void Ru(final Context context) {
        final int ly = 1;
        final ResolveInfo resolveService = context.getPackageManager().resolveService(RC(context), PackageManager.GET_META_DATA);
        if (resolveService != null && resolveService.serviceInfo.metaData != null) {
            d.LY = resolveService.serviceInfo.metaData.getInt("service.api.version", ly);
        }
        else {
            d.LY = ly;
        }
    }
    
    private void Rw(final WindowManager.LayoutParams lq) {
        if (this.LQ != lq) {
            this.LQ = lq;
            if (this.LQ == null) {
                if (this.LW != null) {
                    final ILauncherOverlay lw = this.LW;
                    final Activity lo = this.LO;
                    try {
                        lw.windowDetached(lo.isChangingConfigurations());
                        this.LW = null;
                    }
                    catch (RemoteException ex) {}
                }
            }
            else {
                this.Rm();
            }
        }
    }
    
    public void RB(final f f) {
        if (f.Mf != this.LR) {
            this.LR = f.Mf;
            if (this.LQ != null) {
                this.Rm();
            }
        }
    }
    
    public void RD(final boolean b) {
        int n = 0;
        if (this.LW != null) {
            while (true) {
                Label_0035: {
                    try {
                        final ILauncherOverlay lw = this.LW;
                        if (b) {
                            break Label_0035;
                        }
                        lw.openOverlay(n);
                    }
                    catch (RemoteException ex) {}
                    break;
                }
                n = 1;
                continue;
            }
        }
    }
    
    public void Rl() {
        if (this.Rq()) {
            final ILauncherOverlay lw = this.LW;
            try {
                lw.endScroll();
            }
            catch (RemoteException ex) {}
        }
    }
    
    void Rn(final ILauncherOverlay lw) {
        this.LW = lw;
        if (this.LW != null) {
            if (this.LQ != null) {
                this.Rm();
            }
        }
        else {
            this.RE(0);
        }
    }
    
    public void Rp() {
        if (this.Rq()) {
            final ILauncherOverlay lw = this.LW;
            try {
                lw.startScroll();
            }
            catch (RemoteException ex) {}
        }
    }
    
    public void Rr(final boolean b) {
        int n = 0;
        if (this.LW != null) {
            while (true) {
                Label_0035: {
                    try {
                        final ILauncherOverlay lw = this.LW;
                        if (b) {
                            break Label_0035;
                        }
                        lw.closeOverlay(n);
                    }
                    catch (RemoteException ex) {}
                    break;
                }
                n = 1;
                continue;
            }
        }
    }
    
    public void Rx(final Bundle lp) {
        this.LP = lp;
        if (this.LQ != null && d.LY >= 7) {
            this.Rm();
        }
    }
    
    public void Ry() {
        if (!this.mDestroyed) {
            if (!this.LT.RR() || !this.LV.RR()) {
                this.LO.runOnUiThread((Runnable)new i(this));
            }
        }
    }
    
    public void Rz(final float n) {
        if (this.Rq()) {
            try {
                this.LW.onScroll(n);
            }
            catch (RemoteException ex) {}
        }
    }
    
    public final void onAttachedToWindow() {
        if (!this.mDestroyed) {
            this.Rw(this.LO.getWindow().getAttributes());
        }
    }
    
    public void onDestroy() {
        boolean b = false;
        if (!this.LO.isChangingConfigurations()) {
            b = true;
        }
        this.RF(b);
    }
    
    public final void onDetachedFromWindow() {
        if (!this.mDestroyed) {
            this.Rw(null);
        }
    }
    
    public void onPause() {
        if (!this.mDestroyed) {
            this.LN &= 0xFFFFFFFD;
            if (this.LW != null && this.LQ != null) {
                ILauncherOverlay lw2 = null;
                Label_0077: {
                    if (d.LY < 4) {
                        break Label_0077;
                    }
                    final ILauncherOverlay lw = this.LW;
                    try {
                        lw.setActivityState(this.LN);
                        lw2 = this.LW;
                    }
                    catch (RemoteException ex) {
                        return;
                    }
                }
                lw2.onPause();
            }
        }
    }
    
    public void onResume() {
        if (!this.mDestroyed) {
            this.LN |= 0x2;
            if (this.LW != null && this.LQ != null) {
                ILauncherOverlay lw2 = null;
                Label_0076: {
                    if (d.LY < 4) {
                        break Label_0076;
                    }
                    try {
                        this.LW.setActivityState(this.LN);
                        lw2 = this.LW;
                    }
                    catch (RemoteException ex) {
                        return;
                    }
                }
                lw2.onResume();
            }
        }
    }
    
    public void onStart() {
        if (!this.mDestroyed) {
            this.LT.RM(false);
            this.Ry();
            this.LN |= 0x1;
            if (this.LW != null && this.LQ != null) {
                final ILauncherOverlay lw = this.LW;
                try {
                    lw.setActivityState(this.LN);
                }
                catch (RemoteException ex) {}
            }
        }
    }
    
    public void onStop() {
        if (!this.mDestroyed) {
            this.LT.RM(true);
            this.LV.RS();
            this.LN &= 0xFFFFFFFE;
            if (this.LW != null && this.LQ != null) {
                final ILauncherOverlay lw = this.LW;
                try {
                    lw.setActivityState(this.LN);
                }
                catch (RemoteException ex) {}
            }
        }
    }
    
    public boolean startSearch(final byte[] array, final Bundle bundle) {
        if (d.LY >= 6) {
            if (this.LW != null) {
                try {
                    return this.LW.startSearch(array, bundle);
                }
                catch (RemoteException ex) {
                    Log.e("DrawerOverlayClient", "Error starting session for search", (Throwable)ex);
                }
            }
            return false;
        }
        return false;
    }

    class e extends ILauncherOverlayCallback$Stub implements Handler.Callback
    {
        private boolean Ma;
        private final Handler Mb;
        private int Mc;
        private d Md;
        private WindowManager Me;
        private Window mWindow;

        e() {
            this.Ma = false;
            this.Mb = new Handler(Looper.getMainLooper(), this);
        }

        private void RI(final boolean ma) {
            if (this.Ma != ma) {
                this.Ma = ma;
            }
        }

        public void RJ(final d md) {
            this.Md = md;
            this.Me = md.LO.getWindowManager();
            final Point point = new Point();
            this.Me.getDefaultDisplay().getRealSize(point);
            this.Mc = -Math.max(point.x, point.y);
            this.mWindow = md.LO.getWindow();
        }

        public void clear() {
            this.Md = null;
            this.Me = null;
            this.mWindow = null;
        }

        public boolean handleMessage(final Message message) {
            final boolean b = true;
            if (this.Md == null) {
                return b;
            }
            switch (message.what) {
                default: {
                    return false;
                }
                case 2: {
                    if ((this.Md.LX & 0x1) != 0x0) {
                        this.Md.LS.onOverlayScrollChanged((float)message.obj);
                    }
                    return b;
                }
                case 3: {
                    final WindowManager.LayoutParams attributes = this.mWindow.getAttributes();
                    if (!(boolean)message.obj) {
                        attributes.x = 0;
                        attributes.flags &= 0xFFFFFDFF;
                    }
                    else {
                        attributes.x = this.Mc;
                        attributes.flags |= 0x200;
                    }
                    this.Me.updateViewLayout(this.mWindow.getDecorView(), attributes);
                    return b;
                }
                case 4: {
                    this.Md.RE(message.arg1);
                    if (this.Md.LS instanceof a) {
                        ((a)this.Md.LS).es(message.arg1);
                    }
                    return b;
                }
            }
        }

        public void overlayScrollChanged(final float n) {
            final int n2 = 2;
            this.Mb.removeMessages(n2);
            Message.obtain(this.Mb, n2, (Object)n).sendToTarget();
            if (n > 0.0f) {
                this.RI(false);
            }
        }

        public void overlayStatusChanged(final int n) {
            Message.obtain(this.Mb, 4, n, 0).sendToTarget();
        }
    }

    public static class f
    {
        private final int Mf;

        public f(final int mf) {
            this.Mf = mf;
        }
    }

    class i implements Runnable
    {
        final /* synthetic */ d Ml;

        i(final d ml) {
            this.Ml = ml;
        }

        public void run() {
            this.Ml.RE(0);
        }
    }

    class j extends BroadcastReceiver
    {
        final /* synthetic */ d Mm;

        j(final d mm) {
            this.Mm = mm;
        }

        public void onReceive(final Context context, final Intent intent) {
            final Uri data = intent.getData();
            if (Build.VERSION.SDK_INT < 19) {
                if (data == null) {
                    return;
                }
                if (!"com.google.android.googlequicksearchbox".equals(data.getSchemeSpecificPart())) {
                    return;
                }
            }
            this.Mm.LV.RS();
            this.Mm.LT.RS();
            Ru(context);
            if ((this.Mm.LN & 0x2) != 0x0) {
                this.Mm.Ry();
            }
        }
    }
}
