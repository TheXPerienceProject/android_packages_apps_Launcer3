/*
 * Copyright (C) 2015 The Android Open Source Project
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

package com.android.launcher3.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.LauncherActivityInfo;
import android.os.Handler;
import android.os.Process;
import android.os.UserHandle;

import com.android.launcher3.FolderInfo;
import com.android.launcher3.InstallShortcutReceiver;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.LauncherFiles;
import com.android.launcher3.LauncherModel;
import com.android.launcher3.MainThreadExecutor;
import com.android.launcher3.SessionCommitReceiver;
import com.android.launcher3.ShortcutInfo;
import com.android.launcher3.Utilities;
import com.android.launcher3.compat.UserManagerCompat;
import com.android.launcher3.model.BgDataModel;
import com.android.launcher3.model.ModelWriter;
import com.google.android.apps.nexuslauncher.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Handles addition of app shortcuts for managed profiles.
 * Methods of class should only be called on {@link LauncherModel#sWorkerThread}.
 */
public class ManagedProfileHeuristic {

    /**
     * Maintain a set of packages installed per user.
     */
    private static final String INSTALLED_PACKAGES_PREFIX = "installed_packages_for_user_";

    private static final String USER_FOLDER_ID_PREFIX = "user_folder_";

    /**
     * Duration (in milliseconds) for which app shortcuts will be added to work folder.
     */
    private static final long AUTO_ADD_TO_FOLDER_DURATION = 8 * 60 * 60 * 1000;

    /*public static ManagedProfileHeuristic get(Context context, UserHandle user) {
        if (!Process.myUserHandle().equals(user)) {
            return new ManagedProfileHeuristic(context, user);
        }
        return null;
    }

    private final Context mContext;
    private final LauncherModel mModel;
    private final UserHandle mUser;
    private final IconCache mIconCache;
    private final boolean mAddIconsToHomescreen;

    private ManagedProfileHeuristic(Context context, UserHandle user) {
        mContext = context;
        mUser = user;
        mModel = LauncherAppState.getInstance(context).getModel();
        mIconCache = LauncherAppState.getInstance(context).getIconCache();
        mAddIconsToHomescreen =
                !BuildCompat.isAtLeastO() || SessionCommitReceiver.isEnabled(context);
    }

    public void processPackageRemoved(String[] packages) {
        Preconditions.assertWorkerThread();
        ManagedProfilePackageHandler handler = new ManagedProfilePackageHandler();
        for (String pkg : packages) {
            handler.onPackageRemoved(pkg, mUser);
        }
    }

    public void processPackageAdd(String[] packages) {
        Preconditions.assertWorkerThread();
        ManagedProfilePackageHandler handler = new ManagedProfilePackageHandler();
        for (String pkg : packages) {
            handler.onPackageAdded(pkg, mUser);
        }
    }

    public void processUserApps(List<LauncherActivityInfo> apps) {
        Preconditions.assertWorkerThread();
        new ManagedProfilePackageHandler().processUserApps(apps, mUser);
    }*/

    /**
     * Add work folder shortcuts to the DB.

    private void saveWorkFolderShortcuts(
            long workFolderId, int startingRank, ArrayList<ShortcutInfo> workFolderApps) {
        for (ItemInfo info : workFolderApps) {
            info.rank = startingRank++;
            mModel.getWriter(false).addItemToDatabase(info, workFolderId, 0, 0, 0);
        }
    }*/

    /**
     * Verifies that entries corresponding to {@param users} exist and removes all invalid entries.

    public static void processAllUsers(List<UserHandle> users, Context context) {
        UserManagerCompat userManager = UserManagerCompat.getInstance(context);
        HashSet<String> validKeys = new HashSet<String>();
        for (UserHandle user : users) {
            addAllUserKeys(userManager.getSerialNumberForUser(user), validKeys);
        }

        SharedPreferences prefs = context.getSharedPreferences(
                LauncherFiles.MANAGED_USER_PREFERENCES_KEY,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        for (String key : prefs.getAll().keySet()) {
            if (!validKeys.contains(key)) {
                editor.remove(key);
            }
        }
        editor.apply();
    }*/

    private static void addAllUserKeys(long userSerial, HashSet<String> keysOut) {
        keysOut.add(INSTALLED_PACKAGES_PREFIX + userSerial);
        keysOut.add(USER_FOLDER_ID_PREFIX + userSerial);
    }

    /**
     * For each user, if a work folder has not been created, mark it such that the folder will
     * never get created.
     */
    public static void markExistingUsersForNoFolderCreation(Context context) {
        UserManagerCompat userManager = UserManagerCompat.getInstance(context);
        UserHandle myUser = Process.myUserHandle();

        SharedPreferences prefs = null;
        for (UserHandle user : userManager.getUserProfiles()) {
            if (myUser.equals(user)) {
                continue;
            }

            if (prefs == null) {
                prefs = context.getSharedPreferences(
                        LauncherFiles.MANAGED_USER_PREFERENCES_KEY,
                        Context.MODE_PRIVATE);
            }
            String folderIdKey = USER_FOLDER_ID_PREFIX + userManager.getSerialNumberForUser(user);
            if (!prefs.contains(folderIdKey)) {
                prefs.edit().putLong(folderIdKey, ItemInfo.NO_ID).apply();
            }
        }
    }

    public static void onAllAppsLoaded(final Context context, List<LauncherActivityInfo> list, UserHandle userHandle) {
        if (!Process.myUserHandle().equals(userHandle)) {
            UserFolderInfo managedProfileHeuristic$UserFolderInfo = new UserFolderInfo(context, userHandle, null);
            if (managedProfileHeuristic$UserFolderInfo.folderAlreadyCreated) {
                return;
            }
            if (Utilities.isAtLeastO() && (SessionCommitReceiver.isEnabled(context) ^ true)) {
                managedProfileHeuristic$UserFolderInfo.prefs.edit().putLong(managedProfileHeuristic$UserFolderInfo.folderIdKey, (long)(-1)).apply();
                return;
            }
            InstallShortcutReceiver.enableInstallQueue(4);
            for (LauncherActivityInfo launcherActivityInfo : list) {
                if (launcherActivityInfo.getFirstInstallTime() < managedProfileHeuristic$UserFolderInfo.addIconToFolderTime) {
                    InstallShortcutReceiver.queueActivityInfo(launcherActivityInfo, context);
                }
            }
            new Handler(LauncherModel.getWorkerLooper()).post(new Runnable() {
                @Override
                public void run() {
                    InstallShortcutReceiver.disableAndFlushInstallQueue(4, context);
                }
            });
        }
    }

    public static SharedPreferences prefs(final Context context) {
        return context.getSharedPreferences("com.android.launcher3.managedusers.prefs", 0);
    }

    public static void processAllUsers(final List list, final Context context) {
        final UserManagerCompat instance = UserManagerCompat.getInstance(context);
        final HashSet<String> set = new HashSet<String>();
        final Iterator<UserHandle> iterator = list.iterator();
        while (iterator.hasNext()) {
            set.add("user_folder_" + instance.getSerialNumberForUser(iterator.next()));
        }
        SharedPreferences prefs = prefs(context);
        SharedPreferences.Editor edit = prefs.edit();
        for (final String s : prefs.getAll().keySet()) {
            if (!set.contains(s)) {
                edit.remove(s);
            }
        }
        edit.apply();
    }

    public static class UserFolderInfo
    {
        final long addIconToFolderTime;
        final boolean folderAlreadyCreated;
        final String folderIdKey;
        final FolderInfo folderInfo;
        boolean folderPendingAddition;
        final ArrayList<ShortcutInfo> pendingShortcuts;
        final SharedPreferences prefs;
        final UserHandle user;
        final long userSerial;

        public UserFolderInfo(final Context context, final UserHandle user, final BgDataModel bgDataModel) {
            final boolean folderPendingAddition = true;
            this.pendingShortcuts = new ArrayList<>();
            this.user = user;
            final UserManagerCompat instance = UserManagerCompat.getInstance(context);
            this.userSerial = instance.getSerialNumberForUser(user);
            this.addIconToFolderTime = instance.getUserCreationTime(user) + 28800000L;
            this.folderIdKey = "user_folder_" + this.userSerial;
            this.prefs = ManagedProfileHeuristic.prefs(context);
            this.folderAlreadyCreated = this.prefs.contains(this.folderIdKey);
            if (bgDataModel != null) {
                if (this.folderAlreadyCreated) {
                    this.folderInfo = (FolderInfo)bgDataModel.folders.get(this.prefs.getLong(this.folderIdKey, (long)(-1)));
                }
                else {
                    this.folderInfo = new FolderInfo();
                    this.folderInfo.title = context.getText(R.string.work_folder_name);
                    this.folderInfo.setOption(2, folderPendingAddition, null);
                    this.folderPendingAddition = folderPendingAddition;
                }
            }
            else {
                this.folderInfo = null;
            }
        }

        public void applyPendingState(final ModelWriter modelWriter) {
            if (this.folderInfo == null) {
                return;
            }
            int size;
            if (this.folderAlreadyCreated) {
                size = this.folderInfo.contents.size();
            }
            else {
                size = 0;
            }
            for (ShortcutInfo shortcutInfo : this.pendingShortcuts) {
                final int n = size + 1;
                shortcutInfo.rank = size;
                modelWriter.addItemToDatabase(shortcutInfo, this.folderInfo.id, 0L, 0, 0);
                size = n;
            }
            if (this.folderAlreadyCreated) {
                new MainThreadExecutor().execute(new Runnable()
                {
                    public void run() {
                        folderInfo.prepareAutoUpdate();
                        Iterator<ShortcutInfo> iterator = pendingShortcuts.iterator();
                        while (iterator.hasNext()) {
                            folderInfo.add(iterator.next(), false);
                        }
                    }
                });
            }
            else {
                this.prefs.edit().putLong(this.folderIdKey, this.folderInfo.id).apply();
            }
        }

        public ItemInfo convertToWorkspaceItem(final ShortcutInfo shortcutInfo, final LauncherActivityInfo launcherActivityInfo) {
            if (launcherActivityInfo.getFirstInstallTime() >= this.addIconToFolderTime) {
                return shortcutInfo;
            }
            if (this.folderAlreadyCreated) {
                if (this.folderInfo == null) {
                    return shortcutInfo;
                }
                this.pendingShortcuts.add(shortcutInfo);
                return null;
            }
            else {
                this.pendingShortcuts.add(shortcutInfo);
                this.folderInfo.add(shortcutInfo, false);
                if (this.folderPendingAddition) {
                    this.folderPendingAddition = false;
                    return this.folderInfo;
                }
                return null;
            }
        }
    }
}
