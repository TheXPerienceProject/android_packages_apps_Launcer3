// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.search;

import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.android.launcher3.AllAppsList;
import com.android.launcher3.AppInfo;
import com.android.launcher3.LauncherAppState;
import com.android.launcher3.LauncherModel;
import com.android.launcher3.compat.UserManagerCompat;
import com.android.launcher3.model.BgDataModel;
import com.android.launcher3.util.ComponentKey;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AppSearchProvider extends ContentProvider
{
    private static final String[] eK;
    private final ContentProvider.PipeDataWriter eL;
    private LooperExecutor eM;
    public LauncherAppState mApp;
    
    static {
        eK = new String[] { "_id", "suggest_text_1", "suggest_icon_1", "suggest_intent_action", "suggest_intent_data" };
    }
    
    public AppSearchProvider() {
        this.eL = (ContentProvider.PipeDataWriter)new h(this);
    }
    
    public static ComponentKey dl(final Uri uri, final Context context) {
        return new ComponentKey(ComponentName.unflattenFromString(uri.getQueryParameter("component")), UserManagerCompat.getInstance(context).getUserForSerialNumber(Long.parseLong(uri.getQueryParameter("user"))));
    }
    
    public static Uri dm(final AppInfo appInfo, final UserManagerCompat userManagerCompat) {
        return new Uri.Builder().scheme("content").authority("com.google.android.apps.nexuslauncher.appssearch").appendQueryParameter("component", appInfo.componentName.flattenToShortString()).appendQueryParameter("user", Long.toString(userManagerCompat.getSerialNumberForUser(appInfo.user))).build();
    }
    
    private Cursor dn(final List list) {
        final MatrixCursor matrixCursor = new MatrixCursor(AppSearchProvider.eK, list.size());
        final UserManagerCompat instance = UserManagerCompat.getInstance(this.getContext());
        final Iterator<AppInfo> iterator = (Iterator<AppInfo>)list.iterator();
        int n = 0;
        while (iterator.hasNext()) {
            final AppInfo appInfo = iterator.next();
            final String string = dm(appInfo, instance).toString();
            final MatrixCursor.RowBuilder row = matrixCursor.newRow();
            final int n2 = n + 1;
            row.add((Object)n).add((Object)appInfo.title.toString()).add((Object)string).add((Object)"com.google.android.apps.nexuslauncher.search.APP_LAUNCH").add((Object)string);
            n = n2;
        }
        return (Cursor)matrixCursor;
    }
    
    public Bundle call(final String s, final String s2, final Bundle bundle) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.d("AppSearchProvider", "Content provider accessed on main thread");
            return null;
        }
        if ("loadIcon".equals(s)) {
            final Uri parse = Uri.parse(s2);
            final ComponentKey dl = dl(parse, this.getContext());
            final LooperExecutor em = this.eM;
            final g g = new g(this, dl);
            final LooperExecutor looperExecutor = em;
            final Future<Bitmap> submit = looperExecutor.submit((Callable<Bitmap>)g);
            try {
                final Bitmap value = submit.get();
                final Bitmap bitmap = value;
                final Bundle bundle2 = new Bundle();
                bundle2.putParcelable("suggest_icon_1", bitmap);
                return bundle2;
            }
            catch (Exception ex) {
                Log.e("AppSearchProvider", "Unable to load icon " + ex);
                return null;
            }
        }
        return super.call(s, s2, bundle);
    }
    
    public int delete(final Uri uri, final String s, final String[] array) {
        throw new UnsupportedOperationException();
    }
    
    public String getType(final Uri uri) {
        return "vnd.android.cursor.dir/vnd.android.search.suggest";
    }
    
    public Uri insert(final Uri uri, final ContentValues contentValues) {
        throw new UnsupportedOperationException();
    }
    
    public boolean onCreate() {
        this.eM = new LooperExecutor(LauncherModel.getWorkerLooper());
        this.mApp = LauncherAppState.getInstance(this.getContext());
        return true;
    }
    
    public ParcelFileDescriptor openFile(final Uri uri, final String s) throws FileNotFoundException {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.e("AppSearchProvider", "Content provider accessed on main thread");
            return null;
        }
        final ComponentKey dl = dl(uri, this.getContext());
        final String s2 = "image/png";
        final LooperExecutor em = this.eM;
        final g g = new g(this, dl);
        final LooperExecutor looperExecutor = em;
        final Future<Object> submit = looperExecutor.submit((Callable<Object>)g);
        try {
            return this.openPipeHelper(uri, s2, (Bundle)null, (Object)submit, this.eL);
        }
        catch (Exception ex) {
            throw new FileNotFoundException(ex.getMessage());
        }
    }
    
    public Cursor query(final Uri uri, final String[] array, final String s, final String[] array2, final String s2) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Log.e("AppSearchProvider", "Content provider accessed on main thread");
            return (Cursor)new MatrixCursor(AppSearchProvider.eK, 0);
        }
        final f f = new f(uri.getLastPathSegment());
        this.mApp.getModel().enqueueModelUpdateTask(f);
        try {
            final Object value = f.eN.get(5, TimeUnit.SECONDS);
            final List<?> list = (List<?>)value;
            return this.dn(list);
        }
        catch (InterruptedException | ExecutionException | TimeoutException ex) {
            Log.d("AppSearchProvider", "Error searching apps", ex);
            final List<?> list = new ArrayList<Object>();
            return this.dn(list);
        }
    }
    
    public int update(final Uri uri, final ContentValues contentValues, final String s, final String[] array) {
        throw new UnsupportedOperationException();
    }

    class f implements Callable, LauncherModel.ModelUpdateTask
    {
        private final FutureTask eN;
        private AllAppsList mAllAppsList;
        private LauncherAppState mApp;
        private BgDataModel mBgDataModel;
        private LauncherModel mModel;
        private final String mQuery;

        f(final String s) {
            this.mQuery = s.toLowerCase();
            this.eN = new FutureTask(this);
        }

        public List call() {
            /*if (!this.mModel.isModelLoaded()) {
                Log.d("AppSearchProvider", "Workspace not loaded, loading now");
                this.mModel.startLoaderForResults(new LoaderResults(this.mApp, this.mBgDataModel, this.mAllAppsList, 0, null));
            }
            if (!this.mModel.isModelLoaded()) {
                Log.d("AppSearchProvider", "Loading workspace failed");
                return Collections.emptyList();
            }*/
            final ArrayList<Object> list = new ArrayList<Object>();
            /*final ArrayList<AppInfo> data = this.mAllAppsList.data;
            final DefaultAppSearchAlgorithm$StringMatcher instance = DefaultAppSearchAlgorithm$StringMatcher.getInstance();
            for (AppInfo appInfo : data) {
                if (DefaultAppSearchAlgorithm.matches(appInfo, this.mQuery, instance)) {
                    list.add(appInfo);
                    if (!appInfo.usingLowResIcon) {
                        continue;
                    }
                    this.mApp.getIconCache().getTitleAndIcon(appInfo, false);
                }
            }
            Collections.sort(list, new AppInfoComparator(this.mApp.getContext()));*/
            return list;
        }

        public void init(final LauncherAppState mApp, final LauncherModel mModel, final BgDataModel mBgDataModel, final AllAppsList mAllAppsList, final Executor executor) {
            this.mApp = mApp;
            this.mModel = mModel;
            this.mBgDataModel = mBgDataModel;
            this.mAllAppsList = mAllAppsList;
        }

        public void run() {
            this.eN.run();
        }
    }
}
