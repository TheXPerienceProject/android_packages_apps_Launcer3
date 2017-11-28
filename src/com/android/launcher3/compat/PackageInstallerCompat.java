/*
 * Copyright (C) 2014 The Android Open Source Project
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

package com.android.launcher3.compat;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInstaller;

import java.util.HashMap;

public abstract class PackageInstallerCompat {

    public static final int STATUS_INSTALLED = 0;
    public static final int STATUS_INSTALLING = 1;
    public static final int STATUS_FAILED = 2;

    private static final Object sInstanceLock = new Object();
    private static PackageInstallerCompat sInstance;

    public static PackageInstallerCompat getInstance(Context context) {
        synchronized (sInstanceLock) {
            if (sInstance == null) {
                sInstance = new PackageInstallerCompatVL(context);
            }
            return sInstance;
        }
    }

    /**
     * @return a map of active installs to their progress
     */
    public abstract HashMap<String, Integer> updateAndGetActiveSessionCache();

    public abstract void onStop();

    public static final class PackageInstallInfo
    {
        public final ComponentName componentName;
        public final String packageName;
        public final int progress;
        public final int state;

        private PackageInstallInfo(final PackageInstaller.SessionInfo packageInstaller$SessionInfo) {
            this.state = 1;
            this.packageName = packageInstaller$SessionInfo.getAppPackageName();
            this.componentName = new ComponentName(this.packageName, "");
            this.progress = (int)(packageInstaller$SessionInfo.getProgress() * 100.0f);
        }

        public PackageInstallInfo(final String packageName, final int state, final int progress) {
            this.state = state;
            this.packageName = packageName;
            this.componentName = new ComponentName(packageName, "");
            this.progress = progress;
        }

        public static PackageInstallInfo fromInstallingState(final PackageInstaller.SessionInfo packageInstaller$SessionInfo) {
            return new PackageInstallInfo(packageInstaller$SessionInfo);
        }

        public static PackageInstallInfo fromState(final int n, final String s) {
            return new PackageInstallInfo(s, n, 0);
        }
    }

}
