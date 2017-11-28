// 
// Decompiled by Procyon v0.5.30
// 

package com.android.launcher3.allapps.search;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.android.launcher3.ExtendedEditText;
import com.android.launcher3.Launcher;
import com.android.launcher3.Utilities;

import java.util.ArrayList;

public class AllAppsSearchBarController implements TextWatcher, TextView.OnEditorActionListener, ExtendedEditText.OnBackKeyListener
{
    protected Callbacks mCb;
    protected ExtendedEditText mInput;
    protected InputMethodManager mInputMethodManager;
    protected Launcher mLauncher;
    protected String mQuery;
    protected SearchAlgorithm mSearchAlgorithm;
    
    public void afterTextChanged(final Editable editable) {
        this.mQuery = editable.toString();
        if (this.mQuery.isEmpty()) {
            this.mSearchAlgorithm.cancel(true);
            this.mCb.clearSearchResult();
        }
        else {
            this.mSearchAlgorithm.cancel(false);
            this.mSearchAlgorithm.doSearch(this.mQuery, this.mCb);
        }
    }
    
    public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
    }
    
    public void focusSearchField() {
        this.mInput.showKeyboard();
    }
    
    protected void hideKeyboard() {
        this.mInputMethodManager.hideSoftInputFromWindow(this.mInput.getWindowToken(), 0);
    }
    
    public final void initialize(final SearchAlgorithm mSearchAlgorithm, final ExtendedEditText mInput, final Launcher mLauncher, final Callbacks mCb) {
        this.mCb = mCb;
        this.mLauncher = mLauncher;
        (this.mInput = mInput).addTextChangedListener((TextWatcher)this);
        this.mInput.setOnEditorActionListener((TextView.OnEditorActionListener)this);
        this.mInput.setOnBackKeyListener(this);
        this.mInputMethodManager = (InputMethodManager)this.mInput.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        this.mSearchAlgorithm = mSearchAlgorithm;
    }
    
    public boolean isSearchFieldFocused() {
        return this.mInput.isFocused();
    }
    
    public boolean onBackKey() {
        if (Utilities.trim(this.mInput.getEditableText().toString()).isEmpty()) {
            this.reset();
            return true;
        }
        return false;
    }
    
    public boolean onEditorAction(final TextView textView, final int n, final KeyEvent keyEvent) {
        if (n != 3) {
            return false;
        }
        final String string = textView.getText().toString();
        //return !string.isEmpty() && this.mLauncher.startActivitySafely((View)textView, PackageManagerHelper.getMarketSearchIntent((Context)this.mLauncher, string), null);
        return false;
    }
    
    public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
    }
    
    public void refreshSearchResult() {
        if (TextUtils.isEmpty((CharSequence)this.mQuery)) {
            return;
        }
        this.mSearchAlgorithm.cancel(false);
        this.mSearchAlgorithm.doSearch(this.mQuery, this.mCb);
    }
    
    public void reset() {
        this.unfocusSearchField();
        this.mCb.clearSearchResult();
        this.mInput.setText((CharSequence)"");
        this.mQuery = null;
        this.hideKeyboard();
    }
    
    protected void unfocusSearchField() {
        final View focusSearch = this.mInput.focusSearch(View.FOCUS_DOWN);
        if (focusSearch != null) {
            focusSearch.requestFocus();
        }
    }

    public interface Callbacks
    {
        void clearSearchResult();

        void onSearchResult(final String p0, final ArrayList p1);
    }
}
