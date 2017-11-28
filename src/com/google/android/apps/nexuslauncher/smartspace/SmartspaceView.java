// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.smartspace;

import android.animation.ValueAnimator;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Handler;
import android.os.Process;
import android.provider.CalendarContract;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.launcher3.BubbleTextView;
import com.android.launcher3.Launcher;
import com.android.launcher3.compat.LauncherAppsCompat;
import com.android.launcher3.dynamicui.WallpaperColorInfo;
import com.android.launcher3.popup.PopupContainerWithArrow;
import com.android.launcher3.util.Themes;
import com.google.android.apps.nexuslauncher.R;
import com.google.android.apps.nexuslauncher.graphics.IcuDateTextView;

import java.util.ArrayList;
import java.util.Collections;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED;

public class SmartspaceView extends FrameLayout implements c, ValueAnimator.AnimatorUpdateListener, View.OnClickListener, View.OnLongClickListener, Runnable
{
    private TextView dA;
    private final TextPaint dB;
    private View dC;
    private TextView dD;
    private ViewGroup dE;
    private ImageView dF;
    private TextView dG;
    private final ColorStateList dH;
    private final int dm;
    private IcuDateTextView dn;
    private ViewGroup d0;
    private final f dp;
    private e dq;
    private BubbleTextView dr;
    private boolean ds;
    private boolean dt;
    private final View.OnClickListener du;
    private final View.OnClickListener dv;
    private ImageView dw;
    private TextView dx;
    private ViewGroup dy;
    private ImageView dz;
    private final Handler mHandler;
    
    public SmartspaceView(final Context context, final AttributeSet set) {
        super(context, set);
        this.du = new h(this);
        this.dv = new i(this);
        this.dp = f.get(context);
        this.mHandler = new Handler();
        this.dH = ColorStateList.valueOf(Themes.getAttrColor(this.getContext(), R.attr.workspaceTextColor));
        this.ds = this.dp.cY();
        this.dm = 2130837511;
        (this.dB = new TextPaint()).setTextSize((float)this.getResources().getDimensionPixelSize(R.dimen.smartspace_title_size));
    }
    
    private void cj(final e e) {
        final boolean cs = e.cS();
        if (this.dt != cs) {
            this.dt = cs;
            this.cs();
        }
        this.setOnClickListener((View.OnClickListener)this);
        this.setOnLongClickListener(this.co());
        if (this.dt) {
            this.ck(e);
        }
        else {
            this.cl(e);
        }
        this.mHandler.removeCallbacks((Runnable)this);
        if (e.cS() && e.dP.cv()) {
            final long cw = e.dP.cw();
            long min = 61000L - System.currentTimeMillis() % 60000L;
            if (cw > 0L) {
                min = Math.min(min, cw);
            }
            this.mHandler.postDelayed((Runnable)this, min);
        }
    }
    
    private void ck(final e e) {
        ColorStateList dh = null;
        this.setBackgroundResource(this.dm);
        final d dp = e.dP;
        if (!TextUtils.isEmpty((CharSequence)dp.getTitle())) {
            if (dp.cv()) {
                this.dD.setText((CharSequence)this.cn());
            }
            else {
                this.dD.setText((CharSequence)dp.getTitle());
            }
            this.dD.setEllipsize(dp.cx(true));
        }
        if (!TextUtils.isEmpty((CharSequence)dp.cy()) || dp.getIcon() != null) {
            this.dx.setText((CharSequence)dp.cy());
            this.dx.setEllipsize(dp.cx(false));
            if (dp.getIcon() != null) {
                final ImageView dw = this.dw;
                if (dp.cz() && WallpaperColorInfo.getInstance(this.getContext()).supportsDarkText()) {
                    dh = this.dH;
                }
                dw.setImageTintList(dh);
                this.dw.setImageBitmap(dp.getIcon());
            }
        }
        if (e.cR()) {
            this.dy.setVisibility(View.VISIBLE);
            this.dy.setOnClickListener(this.dv);
            this.dy.setOnLongClickListener(this.co());
            this.dA.setText(e.dO.getTitle());
            this.dz.setImageBitmap(e.dO.getIcon());
        }
        else {
            this.dy.setVisibility(View.GONE);
        }
    }

    private void cl(final e e) {
        this.setBackgroundResource(0);
        this.dn.setOnClickListener(this.du);
        this.dn.setOnLongClickListener(this.co());
        if (e.cR()) {
            this.dC.setVisibility(View.VISIBLE);
            this.dE.setVisibility(View.VISIBLE);
            this.dE.setOnClickListener(this.dv);
            this.dE.setOnLongClickListener(this.co());
            this.dG.setText(e.dO.getTitle());
            this.dF.setImageBitmap(e.dO.getIcon());
        }
        else {
            this.dE.setVisibility(View.GONE);
            this.dC.setVisibility(View.GONE);
        }
    }
    
    private void cm() {
        this.dD = (TextView)this.findViewById(R.id.title_text);
        this.dx = (TextView)this.findViewById(R.id.subtitle_text);
        this.dw = (ImageView)this.findViewById(R.id.subtitle_icon);
        this.dF = (ImageView)this.findViewById(R.id.title_weather_icon);
        this.dz = (ImageView)this.findViewById(R.id.subtitle_weather_icon);
        this.d0 = (ViewGroup)this.findViewById(R.id.smartspace_content);
        this.dE = (ViewGroup)this.findViewById(R.id.title_weather_content);
        this.dy = (ViewGroup)this.findViewById(R.id.subtitle_weather_content);
        this.dG = (TextView)this.findViewById(R.id.title_weather_text);
        this.dA = (TextView)this.findViewById(R.id.subtitle_weather_text);
        this.dn = (IcuDateTextView)this.findViewById(R.id.clock);
        this.dC = this.findViewById(R.id.title_sep);
    }
    
    private String cn() {
        final boolean b = true;
        final d dp = this.dq.dP;
        return dp.cC(TextUtils.ellipsize((CharSequence)dp.cB(b), this.dB, this.getWidth() - this.getPaddingLeft() - this.getPaddingRight() - this.getResources().getDimensionPixelSize(R.dimen.smartspace_horizontal_padding) - this.dB.measureText(dp.cA(b)), TextUtils.TruncateAt.END).toString());
    }
    
    private View.OnLongClickListener co() {
        View.OnLongClickListener viewOnLongClickListener = null;
        if (!this.ds) {
            viewOnLongClickListener = null;
        }
        return viewOnLongClickListener;
    }
    
    private void cs() {
        final int indexOfChild = this.indexOfChild((View)this.d0);
        this.removeView(this.d0);
        final LayoutInflater from = LayoutInflater.from(this.getContext());
        int n;
        if (this.dt) {
            n = 2130968621;
        }
        else {
            n = 2130968620;
        }
        this.addView(from.inflate(n, this, false), indexOfChild);
        this.cm();
    }
    
    protected final void cp(final int n) {

    }
    
    public void cq() {
        this.ds = this.dp.cY();
        if (this.dq != null) {
            this.cr(this.dq);
        }
        else {
            Log.d("SmartspaceView", "onGsaChanged but no data present");
        }
    }
    
    public void cr(final e dq) {
        this.dq = dq;
        int n;
        if (this.d0.getVisibility() == View.VISIBLE) {
            n = 1;
        }
        else {
            n = 0;
        }
        this.cj(dq);
        if (n == 0) {
            this.d0.setVisibility(View.VISIBLE);
            this.d0.setAlpha(0.0f);
            this.d0.animate().setDuration(200L).alpha(1.0f);
        }
    }
    
    public void onAnimationUpdate(final ValueAnimator valueAnimator) {
        this.invalidate();
    }
    
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.dp.da(this);
    }
    
    public void onClick(final View view) {
        if (this.dq != null && this.dq.cS()) {
            this.cp(10002);
            this.dq.dP.cu(view);
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        f.get(this.getContext()).da(null);
    }
    
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.cm();
        (this.dr = this.findViewById(R.id.dummyBubbleTextView)).setTag(new j(this));
        this.dr.setContentDescription("");
    }
    
    protected void onLayout(final boolean b, final int n, final int n2, final int n3, final int n4) {
        super.onLayout(b, n, n2, n3, n4);
        if (this.dq != null && this.dq.cS() && this.dq.dP.cv()) {
            final String cn = this.cn();
            if (!cn.equals(this.dD.getText())) {
                this.dD.setText(cn);
            }
        }
    }
    
    public boolean onLongClick(final View view) {
        final boolean b = true;
        final Launcher launcher = Launcher.getLauncher(this.getContext());
        final PopupContainerWithArrow popupContainerWithArrow = (PopupContainerWithArrow)launcher.getLayoutInflater().inflate(R.layout.popup_container, launcher.getDragLayer(), false);
        popupContainerWithArrow.setVisibility(View.INVISIBLE);
        launcher.getDragLayer().addView(popupContainerWithArrow);
        final ArrayList list = new ArrayList<b>(b ? 1 : 0);
        list.add(new b());
        popupContainerWithArrow.populateAndShow(this.dr, Collections.EMPTY_LIST, Collections.EMPTY_LIST, list);
        return b;
    }
    
    public void onPause() {
        this.mHandler.removeCallbacks(this);
    }
    
    public void onResume() {
        if (this.dq != null) {
            this.cj(this.dq);
        }
    }
    
    public void run() {
        if (this.dq != null) {
            this.cj(this.dq);
        }
    }
    
    public void setPadding(final int n, final int n2, final int n3, final int n4) {
        super.setPadding(0, 0, 0, 0);
    }

    final class h implements View.OnClickListener
    {
        final /* synthetic */ SmartspaceView dZ;

        h(final SmartspaceView dz) {
            this.dZ = dz;
        }

        public void onClick(final View view) {
            this.dZ.cp(10000);
            final Uri content_URI = CalendarContract.CONTENT_URI;
            final Uri.Builder appendPath = content_URI.buildUpon().appendPath("time");
            ContentUris.appendId(appendPath, System.currentTimeMillis());
            final Intent addFlags = new Intent("android.intent.action.VIEW").setData(appendPath.build()).addFlags(FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            final SmartspaceView dz = this.dZ;
            final Context context = dz.getContext();
            try {
                Launcher.getLauncher(context).startActivitySafely(view, addFlags, null);
            }
            catch (ActivityNotFoundException ex) {
                LauncherAppsCompat.getInstance(this.dZ.getContext()).showAppDetailsForProfile(new ComponentName("com.google.android.calendar", ""), Process.myUserHandle());
            }
        }
    }

    final class i implements View.OnClickListener
    {
        final /* synthetic */ SmartspaceView ea;

        i(final SmartspaceView ea) {
            this.ea = ea;
        }

        public void onClick(final View view) {
            if (this.ea.dq != null && this.ea.dq.cR()) {
                this.ea.cp(10001);
                this.ea.dq.dO.cu(view);
            }
        }
    }
}
