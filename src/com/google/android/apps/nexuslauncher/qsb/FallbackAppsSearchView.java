// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.apps.nexuslauncher.qsb;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.android.launcher3.ExtendedEditText;
import com.android.launcher3.allapps.AllAppsGridAdapter;
import com.android.launcher3.allapps.AllAppsRecyclerView;
import com.android.launcher3.allapps.AllAppsSearchBarController;
import com.android.launcher3.allapps.AlphabeticalAppsList;
import com.android.launcher3.discovery.AppDiscoveryItem;
import com.android.launcher3.discovery.AppDiscoveryUpdateState;

import java.util.ArrayList;

public class FallbackAppsSearchView extends ExtendedEditText implements AllAppsSearchBarController.Callbacks
{
    //private AllAppsQsbLayout bT;
    private AllAppsGridAdapter mAdapter;
    private AlphabeticalAppsList mApps;
    private AllAppsRecyclerView mAppsRecyclerView;
    //private final AllAppsSearchBarController mSearchBarController;
    
    public FallbackAppsSearchView(final Context context) {
        this(context, null);
    }
    
    public FallbackAppsSearchView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public FallbackAppsSearchView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        //this.mSearchBarController = new AllAppsSearchBarController();
    }
    
    private void notifyResultChanged() {
        //this.bT.bv(0);
        this.mAppsRecyclerView.onSearchResultsChanged();
    }
    
    /*public void bu(final AllAppsQsbLayout bt, final AlphabeticalAppsList mApps, final AllAppsRecyclerView mAppsRecyclerView) {
        this.bT = bt;
        this.mApps = mApps;
        this.mAppsRecyclerView = mAppsRecyclerView;
        this.mAdapter = (AllAppsGridAdapter)this.mAppsRecyclerView.getAdapter();
        this.mSearchBarController.initialize(new b(this.getContext()), this, Launcher.getLauncher(this.getContext()), this);
    }*/

    @Override
    public void onBoundsChanged(Rect newBounds) {

    }

    public void clearSearchResult() {
        if (this.getParent() != null && this.mApps.setOrderedFilter(null)) {
            this.notifyResultChanged();
        }
    }

    @Override
    public void onAppDiscoverySearchUpdate(@Nullable AppDiscoveryItem app, @NonNull AppDiscoveryUpdateState state) {

    }

    public void onSearchResult(final String lastSearchQuery, final ArrayList orderedFilter) {
        if (orderedFilter != null && this.getParent() != null) {
            this.mApps.setOrderedFilter(orderedFilter);
            this.notifyResultChanged();
            this.mAdapter.setLastSearchQuery(lastSearchQuery);
        }
    }
    
    /*public void refreshSearchResult() {
        this.mSearchBarController.refreshSearchResult();
    }*/
}
