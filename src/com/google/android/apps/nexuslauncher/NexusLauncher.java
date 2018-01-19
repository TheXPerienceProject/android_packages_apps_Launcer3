package com.google.android.apps.nexuslauncher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.Menu;
import android.view.View;

import com.android.launcher3.AppInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.LauncherCallbacks;
import com.android.launcher3.LauncherExterns;
import com.android.launcher3.R;
import com.android.launcher3.Utilities;
import com.android.launcher3.graphics.DrawableFactory;
import com.android.launcher3.util.ComponentKeyMapper;
import com.google.android.apps.nexuslauncher.smartspace.SmartspaceView;
import com.google.android.apps.nexuslauncher.smartspace.f;
import com.google.android.libraries.launcherclient.GoogleNow;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class NexusLauncher {
    private final Launcher fB;
    public final LauncherCallbacks fA;
    private boolean fC;
    private final LauncherExterns fD;
    private boolean mRunning;
    com.google.android.libraries.launcherclient.GoogleNow fy;
    com.google.android.apps.nexuslauncher.NexusLauncherOverlay fz;
    private boolean mStarted;

    public NexusLauncher(NexusLauncherActivity activity) {
        fB = activity;
        fD = activity;
        fA = new NexusLauncherCallbacks();
        fD.setLauncherCallbacks(fA);
    }

    private static GoogleNow.IntegerReference dZ(SharedPreferences sharedPreferences) {
        return new GoogleNow.IntegerReference(
                (sharedPreferences.getBoolean("pref_enable_minus_one", true) ? 1 : 0) | 0x2 | 0x4 | 0x8);
    }

    class NexusLauncherCallbacks implements LauncherCallbacks {
        private SmartspaceView mSmartspace;

        public void bindAllApplications(final ArrayList<AppInfo> list) {
        }

        public void dump(final String s, final FileDescriptor fileDescriptor, final PrintWriter printWriter, final String[] array) {
            f.get(fB).cX(s, printWriter);
        }

        public void finishBindingItems(final boolean b) {
        }

        public List<ComponentKeyMapper<AppInfo>> getPredictedApps() {
            return new ArrayList<>();
        }

        @Override
        public int getSearchBarHeight() {
            return LauncherCallbacks.SEARCH_BAR_HEIGHT_NORMAL;
        }

        public boolean handleBackPressed() {
            return false;
        }

        public boolean hasCustomContentToLeft() {
            return false;
        }

        public boolean hasSettings() {
            return true;
        }

        public void onActivityResult(final int n, final int n2, final Intent intent) {
        }

        public void onAttachedToWindow() {
            fy.onAttachedToWindow();
        }

        public void onCreate(final Bundle bundle) {
            SharedPreferences prefs = Utilities.getPrefs(fB);
            fz = new com.google.android.apps.nexuslauncher.NexusLauncherOverlay(fB);
            fy = new com.google.android.libraries.launcherclient.GoogleNow(fB, fz, dZ(prefs));
            fz.setNowConnection(fy);

            f.get(fB).cW();
            mSmartspace = fB.findViewById(R.id.search_container_workspace);

            Bundle bundle2 = new Bundle();
            bundle2.putInt("system_ui_visibility", fB.getWindow().getDecorView().getSystemUiVisibility());
            fy.redraw(bundle2);
        }

        public void onDestroy() {
            fy.onDestroy();
        }

        public void onDetachedFromWindow() {
            fy.onDetachedFromWindow();
        }

        public void onHomeIntent() {
            fy.closeOverlay(fC);
        }

        public void onInteractionBegin() {
        }

        public void onInteractionEnd() {
        }

        public void onLauncherProviderChange() {
        }

        public void onNewIntent(final Intent intent) {
        }

        public void onPause() {
            mRunning = false;
            fy.onPause();

            if (mSmartspace != null) {
                mSmartspace.onPause();
            }
        }

        public void onPostCreate(final Bundle bundle) {
        }

        public boolean onPrepareOptionsMenu(final Menu menu) {
            return false;
        }

        public void onRequestPermissionsResult(final int n, final String[] array, final int[] array2) {
        }

        public void onResume() {
            mRunning = true;
            if (mStarted) {
                fC = true;
            }

            try {
                fy.onResume();
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            if (mSmartspace != null) {
                mSmartspace.onResume();
            }
        }

        public void onSaveInstanceState(final Bundle bundle) {
        }

        public void onStart() {
            mStarted = true;
            fy.onStart();
        }

        public void onStop() {
            mStarted = false;
            fy.onStop();
            if (!mRunning) {
                fC = false;
            }
            fz.stop();
        }

        public void onTrimMemory(int n) {
        }

        public void onWindowFocusChanged(boolean hasFocus) {
        }

        public void onWorkspaceLockedChanged() {
        }

        public void populateCustomContentContainer() {
        }

        @Override
        public View getQsbBar() {
            return null;
        }

        @Override
        public Bundle getAdditionalSearchWidgetOptions() {
            return null;
        }

        public void preOnCreate() {
            DrawableFactory.get(fB);
        }

        public void preOnResume() {
        }

        public boolean shouldMoveToDefaultScreenOnHomeIntent() {
            return true;
        }

        public boolean startSearch(String s, boolean b, Bundle bundle) {
                /*View viewById = fB.findViewById(R.id.g_icon);
                while (viewById != null && !viewById.isClickable()) {
                    if (viewById.getParent() instanceof View) {
                        viewById = (View)viewById.getParent();
                    } else {
                        viewById = null;
                    }
                }
                if (viewById != null && viewById.performClick()) {
                    fD.clearTypedText();
                    return true;
                }*/
            return false;
        }
    }
}
