/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.launcher3;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * LauncherCallbacks is an interface used to extend the Launcher activity. It includes many hooks
 * in order to add additional functionality. Some of these are very general, and give extending
 * classes the ability to react to Activity life-cycle or specific user interactions. Others
 * are more specific and relate to replacing parts of the application, for example, the search
 * interface or the wallpaper picker.
 */
public interface LauncherCallbacks {

    void bindAllApplications(final ArrayList p0);

    void dump(final String p0, final FileDescriptor p1, final PrintWriter p2, final String[] p3);

    void finishBindingItems(final boolean p0);

    List getPredictedApps();

    boolean handleBackPressed();

    boolean hasCustomContentToLeft();

    boolean hasSettings();

    void onActivityResult(final int p0, final int p1, final Intent p2);

    void onAttachedToWindow();

    void onCreate(final Bundle p0);

    void onDestroy();

    void onDetachedFromWindow();

    void onHomeIntent();

    void onInteractionBegin();

    void onInteractionEnd();

    void onLauncherProviderChange();

    void onNewIntent(final Intent p0);

    void onPause();

    void onPostCreate(final Bundle p0);

    boolean onPrepareOptionsMenu(final Menu p0);

    void onRequestPermissionsResult(final int p0, final String[] p1, final int[] p2);

    void onResume();

    void onSaveInstanceState(final Bundle p0);

    void onStart();

    void onStop();

    void onTrimMemory(final int p0);

    void onWindowFocusChanged(final boolean p0);

    void onWorkspaceLockedChanged();

    void populateCustomContentContainer();

    void preOnCreate();

    void preOnResume();

    boolean shouldMoveToDefaultScreenOnHomeIntent();

    boolean shouldShowDiscoveryBounce();

    boolean startSearch(final String p0, final boolean p1, final Bundle p2);
}
