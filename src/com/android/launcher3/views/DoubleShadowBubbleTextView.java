package com.android.launcher3.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Region;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.launcher3.BubbleTextView;
import com.google.android.apps.nexuslauncher.R;

public class DoubleShadowBubbleTextView extends BubbleTextView
{
    private final ShadowInfo mShadowInfo;
    
    public DoubleShadowBubbleTextView(final Context context) {
        this(context, null);
    }
    
    public DoubleShadowBubbleTextView(final Context context, final AttributeSet set) {
        this(context, set, 0);
    }
    
    public DoubleShadowBubbleTextView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.mShadowInfo = new ShadowInfo(context, set, n);
        this.setShadowLayer(this.mShadowInfo.ambientShadowBlur, 0.0f, 0.0f, this.mShadowInfo.ambientShadowColor);
    }
    
    @SuppressLint("WrongConstant")
    public void onDraw(final Canvas canvas) {
        if (this.mShadowInfo.skipDoubleShadow(this)) {
            super.onDraw(canvas);
            return;
        }
        final int alpha = Color.alpha(this.getCurrentTextColor());
        this.getPaint().setShadowLayer(this.mShadowInfo.ambientShadowBlur, 0.0f, 0.0f, ColorUtils.setAlphaComponent(this.mShadowInfo.ambientShadowColor, alpha));
        this.drawWithoutBadge(canvas);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect((float)this.getScrollX(), (float)(this.getScrollY() + this.getExtendedPaddingTop()), (float)(this.getScrollX() + this.getWidth()), (float)(this.getScrollY() + this.getHeight()), Region.Op.INTERSECT);
        this.getPaint().setShadowLayer(this.mShadowInfo.keyShadowBlur, 0.0f, this.mShadowInfo.keyShadowOffset, ColorUtils.setAlphaComponent(this.mShadowInfo.keyShadowColor, alpha));
        this.drawWithoutBadge(canvas);
        canvas.restore();
        this.drawBadgeIfNecessary(canvas);
    }


    public static class ShadowInfo
    {
        public final float ambientShadowBlur;
        public final int ambientShadowColor;
        public final float keyShadowBlur;
        public final int keyShadowColor;
        public final float keyShadowOffset;

        @SuppressLint("ResourceType")
        public ShadowInfo(final Context context, final AttributeSet set, final int n) {
            final TypedArray obtainStyledAttributes = context.obtainStyledAttributes(set, R.styleable.ShadowInfo, n, 0);
            this.ambientShadowBlur = obtainStyledAttributes.getDimension(1, 0.0f);
            this.ambientShadowColor = obtainStyledAttributes.getColor(0, 0);
            this.keyShadowBlur = obtainStyledAttributes.getDimension(3, 0.0f);
            this.keyShadowOffset = obtainStyledAttributes.getDimension(4, 0.0f);
            this.keyShadowColor = obtainStyledAttributes.getColor(2, 0);
            obtainStyledAttributes.recycle();
        }

        public boolean skipDoubleShadow(final TextView textView) {
            final boolean b = true;
            final int alpha = Color.alpha(textView.getCurrentTextColor());
            final int alpha2 = Color.alpha(this.keyShadowColor);
            final int alpha3 = Color.alpha(this.ambientShadowColor);
            if (alpha == 0 || (alpha2 == 0 && alpha3 == 0)) {
                textView.getPaint().clearShadowLayer();
                return b;
            }
            if (alpha3 > 0) {
                textView.getPaint().setShadowLayer(this.ambientShadowBlur, 0.0f, 0.0f, ColorUtils.setAlphaComponent(this.ambientShadowColor, alpha));
                return b;
            }
            if (alpha2 > 0) {
                textView.getPaint().setShadowLayer(this.keyShadowBlur, 0.0f, this.keyShadowOffset, ColorUtils.setAlphaComponent(this.keyShadowColor, alpha));
                return b;
            }
            return false;
        }
    }
}
