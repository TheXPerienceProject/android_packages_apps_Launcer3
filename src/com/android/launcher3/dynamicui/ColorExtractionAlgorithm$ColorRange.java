package com.android.launcher3.dynamicui;

import android.util.Range;

class ColorExtractionAlgorithm$ColorRange
{
    private Range mHue;
    private Range mLightness;
    private Range mSaturation;
    
    ColorExtractionAlgorithm$ColorRange(final Range mHue, final Range mSaturation, final Range mLightness) {
        this.mHue = mHue;
        this.mSaturation = mSaturation;
        this.mLightness = mLightness;
    }
    
    boolean containsColor(final float n, final float n2, final float n3) {
        return this.mHue.contains(n) && this.mSaturation.contains(n2) && this.mLightness.contains(n3);
    }
    
    public String toString() {
        return String.format("H: %s, S: %s, L %s", this.mHue, this.mSaturation, this.mLightness);
    }
}
