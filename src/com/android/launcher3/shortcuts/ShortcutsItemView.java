/*
 * Copyright (C) 2017 The Android Open Source Project
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

package com.android.launcher3.shortcuts;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.android.launcher3.AbstractFloatingView;
import com.android.launcher3.BubbleTextView;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.LauncherAnimUtils;
import com.android.launcher3.anim.PropertyListBuilder;
import com.android.launcher3.anim.RoundedRectRevealOutlineProvider;
import com.android.launcher3.dragndrop.DragOptions;
import com.android.launcher3.dragndrop.DragView;
import com.android.launcher3.logging.UserEventDispatcher.LogContainerProvider;
import com.android.launcher3.popup.PopupContainerWithArrow;
import com.android.launcher3.popup.PopupItemView;
import com.android.launcher3.popup.PopupPopulator;
import com.android.launcher3.popup.SystemShortcut;
import com.android.launcher3.userevent.nano.LauncherLogProto;
import com.google.android.apps.nexuslauncher.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link PopupItemView} that contains all of the {@link DeepShortcutView}s for an app,
 * as well as the system shortcuts such as Widgets and App Info.
 */
public class ShortcutsItemView extends PopupItemView implements View.OnLongClickListener,
        View.OnTouchListener, LogContainerProvider {

    private Launcher mLauncher;
    private LinearLayout mShortcutsLayout;
    private LinearLayout mSystemShortcutIcons;
    private final Point mIconShift = new Point();
    private final Point mIconLastTouchPos = new Point();
    private final List<DeepShortcutView> mDeepShortcutViews = new ArrayList<>();
    private final List<View> mSystemShortcutViews = new ArrayList<>();
    private LinearLayout mContent;
    private int mHiddenShortcutsHeight;

    public ShortcutsItemView(Context context) {
        this(context, null, 0);
    }

    public ShortcutsItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShortcutsItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mLauncher = Launcher.getLauncher(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContent = findViewById(R.id.content);
        mShortcutsLayout = findViewById(R.id.shortcuts);
    }

    @Override
    public boolean onTouch(View v, MotionEvent ev) {
        // Touched a shortcut, update where it was touched so we can drag from there on long click.
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mIconLastTouchPos.set((int) ev.getX(), (int) ev.getY());
                break;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View v) {
        // Return early if this is not initiated from a touch or not the correct view
        if (!(v.getParent() instanceof DeepShortcutView)) return false;
        // Return early if global dragging is not enabled
        if (!mLauncher.isDraggingEnabled()) return false;
        // Return early if an item is already being dragged (e.g. when long-pressing two shortcuts)
        if (mLauncher.getDragController().isDragging()) return false;

        // Long clicked on a shortcut.
        DeepShortcutView sv = (DeepShortcutView) v.getParent();
        sv.setWillDrawIcon(false);

        // Move the icon to align with the center-top of the touch point
        mIconShift.x = mIconLastTouchPos.x - sv.getIconCenter().x;
        mIconShift.y = mIconLastTouchPos.y - mLauncher.getDeviceProfile().iconSizePx;

        DragView dv = mLauncher.getWorkspace().beginDragShared(sv.getIconView(),
                (PopupContainerWithArrow) getParent(), sv.getFinalInfo(),
                new ShortcutDragPreviewProvider(sv.getIconView(), mIconShift), new DragOptions());
        dv.animateShift(-mIconShift.x, -mIconShift.y);

        // TODO: support dragging from within folder without having to close it
        AbstractFloatingView.closeOpenContainer(mLauncher, AbstractFloatingView.TYPE_FOLDER);
        return false;
    }

    private void addShortcutView(View shortcutView, PopupPopulator.Item shortcutType, int index) {
        if (shortcutType == PopupPopulator.Item.SHORTCUT) {
            mDeepShortcutViews.add((DeepShortcutView) shortcutView);
        } else {
            mSystemShortcutViews.add(shortcutView);
        }
        if (shortcutType == PopupPopulator.Item.SYSTEM_SHORTCUT_ICON) {
            // System shortcut icons are added to a header that is separate from the full shortcuts.
            if (mSystemShortcutIcons == null) {
                mSystemShortcutIcons = (LinearLayout) mLauncher.getLayoutInflater().inflate(
                        R.layout.system_shortcut_icons, mShortcutsLayout, false);
                mShortcutsLayout.addView(mSystemShortcutIcons, mShortcutsLayout.getChildCount() > 0 ? -1 : 0);
            }
            mSystemShortcutIcons.addView(shortcutView, index);
        } else {
            if (mShortcutsLayout.getChildCount() > 0) {
                View prevChild = mShortcutsLayout.getChildAt(mShortcutsLayout.getChildCount() - 1);
                if (prevChild instanceof DeepShortcutView) {
                    prevChild.findViewById(R.id.divider).setVisibility(VISIBLE);
                }
            }
            mShortcutsLayout.addView(shortcutView, index);
        }
    }

    public void addShortcutView(View view, PopupPopulator.Item item) {
        addShortcutView(view, item, -1);
    }

    private Animator translateYFrom(final View view, final int n) {
        float translationY = view.getTranslationY();
        return ObjectAnimator.ofFloat(view, ShortcutsItemView.TRANSLATION_Y, new float[] { n + translationY, translationY });
    }

    public List<DeepShortcutView> getDeepShortcutViews(boolean reverseOrder) {
        if (reverseOrder) {
            Collections.reverse(mDeepShortcutViews);
        }
        return mDeepShortcutViews;
    }

    public List<View> getSystemShortcutViews(boolean reverseOrder) {
        // Always reverse system shortcut icons (in the header)
        // so they are in priority order from right to left.
        if (reverseOrder || mSystemShortcutIcons != null) {
            Collections.reverse(mSystemShortcutViews);
        }
        return mSystemShortcutViews;
    }

    /**
     * Adds a {@link SystemShortcut.Widgets} item if there are widgets for the given ItemInfo.
     */
    public void enableWidgetsIfExist(final BubbleTextView originalIcon) {
        ItemInfo itemInfo = (ItemInfo) originalIcon.getTag();
        SystemShortcut widgetInfo = new SystemShortcut.Widgets();
        View.OnClickListener onClickListener = widgetInfo.getOnClickListener(mLauncher, itemInfo);
        View widgetsView = null;
        for (View systemShortcutView : mSystemShortcutViews) {
            if (systemShortcutView.getTag() instanceof SystemShortcut.Widgets) {
                widgetsView = systemShortcutView;
                break;
            }
        }
        final PopupPopulator.Item widgetsItem = mSystemShortcutIcons == null
                ? PopupPopulator.Item.SYSTEM_SHORTCUT
                : PopupPopulator.Item.SYSTEM_SHORTCUT_ICON;
        if (onClickListener != null && widgetsView == null) {
            // We didn't have any widgets cached but now there are some, so enable the shortcut.
            widgetsView = mLauncher.getLayoutInflater().inflate(widgetsItem.layoutId, this, false);
            PopupPopulator.initializeSystemShortcut(getContext(), widgetsView, widgetInfo);
            widgetsView.setOnClickListener(onClickListener);
            if (widgetsItem == PopupPopulator.Item.SYSTEM_SHORTCUT_ICON) {
                addShortcutView(widgetsView, widgetsItem, 0);
            } else {
                // If using the expanded system shortcut (as opposed to just the icon), we need to
                // reopen the container to ensure measurements etc. all work out. While this could
                // be quite janky, in practice the user would typically see a small flicker as the
                // animation restarts partway through, and this is a very rare edge case anyway.
                ((PopupContainerWithArrow) getParent()).close(false);
                PopupContainerWithArrow.showForIcon(originalIcon);
            }
        } else if (onClickListener == null && widgetsView != null) {
            // No widgets exist, but we previously added the shortcut so remove it.
            if (widgetsItem == PopupPopulator.Item.SYSTEM_SHORTCUT_ICON) {
                mSystemShortcutViews.remove(widgetsView);
                mSystemShortcutIcons.removeView(widgetsView);
            } else {
                ((PopupContainerWithArrow) getParent()).close(false);
                PopupContainerWithArrow.showForIcon(originalIcon);
            }
        }
    }

    /*@Override
    public Animator createOpenAnimation(boolean isContainerAboveIcon, boolean pivotLeft) {
        AnimatorSet openAnimation = LauncherAnimUtils.createAnimatorSet();
        openAnimation.play(super.createOpenAnimation(isContainerAboveIcon, pivotLeft));
        for (int i = 0; i < mShortcutsLayout.getChildCount(); i++) {
            if (!(mShortcutsLayout.getChildAt(i) instanceof DeepShortcutView)) {
                continue;
            }
            DeepShortcutView shortcutView = ((DeepShortcutView) mShortcutsLayout.getChildAt(i));
            View deepShortcutIcon = shortcutView.getIconView();
            deepShortcutIcon.setScaleX(0);
            deepShortcutIcon.setScaleY(0);
            openAnimation.play(LauncherAnimUtils.ofPropertyValuesHolder(
                    deepShortcutIcon, new PropertyListBuilder().scale(1).build()));
        }
        return openAnimation;
    }

    @Override
    public Animator createCloseAnimation(boolean isContainerAboveIcon, boolean pivotLeft,
            long duration) {
        AnimatorSet closeAnimation = LauncherAnimUtils.createAnimatorSet();
        closeAnimation.play(super.createCloseAnimation(isContainerAboveIcon, pivotLeft, duration));
        for (int i = 0; i < mShortcutsLayout.getChildCount(); i++) {
            if (!(mShortcutsLayout.getChildAt(i) instanceof DeepShortcutView)) {
                continue;
            }
            DeepShortcutView shortcutView = ((DeepShortcutView) mShortcutsLayout.getChildAt(i));
            View deepShortcutIcon = shortcutView.getIconView();
            deepShortcutIcon.setScaleX(1);
            deepShortcutIcon.setScaleY(1);
            closeAnimation.play(LauncherAnimUtils.ofPropertyValuesHolder(
                    deepShortcutIcon, new PropertyListBuilder().scale(0).build()));
        }
        return closeAnimation;
    }

    @Override
    public int getArrowColor(boolean isArrowAttachedToBottom) {
        return 0;
    }*/

    @Override
    public void fillInLogContainerData(View v, ItemInfo info, LauncherLogProto.Target target,
            LauncherLogProto.Target targetParent) {
        target.itemType = LauncherLogProto.ItemType.DEEPSHORTCUT;
        target.rank = info.rank;
        targetParent.containerType = LauncherLogProto.ContainerType.DEEPSHORTCUTS;
    }

    public int getHiddenShortcutsHeight() {
        return mHiddenShortcutsHeight;
    }

    public void hideShortcuts(boolean b, int n) {
        this.mHiddenShortcutsHeight = (getResources().getDimensionPixelSize(R.dimen.bg_popup_item_height) - this.mShortcutsLayout.getChildAt(0).getLayoutParams().height) * this.mShortcutsLayout.getChildCount();
        final int n3 = this.mShortcutsLayout.getChildCount() - n;
        if (n3 <= 0) {
            return;
        }
        final int childCount = this.mShortcutsLayout.getChildCount();
        int n4;
        if (b) {
            n4 = 1;
        }
        else {
            n4 = -1;
        }
        int n5;
        int n6;
        if (b) {
            n5 = n3;
            n6 = 0;
        }
        else {
            final int n7 = childCount - 1;
            n5 = n3;
            n6 = n7;
        }
        while (n6 >= 0 && n6 < childCount) {
            final View child = this.mShortcutsLayout.getChildAt(n6);
            if (child instanceof DeepShortcutView) {
                this.mHiddenShortcutsHeight += child.getLayoutParams().height;
                child.setVisibility(View.GONE);
                final int n8 = n6 + n4;
                if (!b && n8 >= 0 && n8 < childCount) {
                    this.mShortcutsLayout.getChildAt(n8).findViewById(R.id.divider).setVisibility(View.GONE);
                }
                --n5;
                if (n5 == 0) {
                    break;
                }
            }
            n6 += n4;
        }
    }

    public Animator showAllShortcuts(final boolean b) {
        final int childCount = this.mShortcutsLayout.getChildCount();
        if (childCount == 0) {
            Log.w("ShortcutsItem", "Tried to show all shortcuts but there were no shortcuts to show");
            return null;
        }
        final int height = this.mShortcutsLayout.getChildAt(0).getLayoutParams().height;
        final int dimensionPixelSize = this.getResources().getDimensionPixelSize(R.dimen.bg_popup_item_height);
        for (int i = 0; i < childCount; ++i) {
            final DeepShortcutView deepShortcutView = (DeepShortcutView)this.mShortcutsLayout.getChildAt(i);
            deepShortcutView.getLayoutParams().height = dimensionPixelSize;
            deepShortcutView.requestLayout();
            deepShortcutView.setVisibility(View.VISIBLE);
            if (i < childCount - 1) {
                deepShortcutView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            }
        }
        final AnimatorSet animatorSet = LauncherAnimUtils.createAnimatorSet();
        if (b) {
            animatorSet.play(this.translateYFrom(this.mShortcutsLayout, -this.mHiddenShortcutsHeight));
        }
        else if (this.mSystemShortcutIcons != null) {
            animatorSet.play(this.translateYFrom(this.mSystemShortcutIcons, -this.mHiddenShortcutsHeight));
            final Rect rect = new Rect(this.mPillRect);
            final Rect rect2 = new Rect(this.mPillRect);
            rect2.bottom += this.mHiddenShortcutsHeight;
            animatorSet.play(new RoundedRectRevealOutlineProvider(this.getBackgroundRadius(), this.getBackgroundRadius(), rect, rect2, this.mRoundedCorners).createRevealAnimator((View)this, false));
        }
        for (int j = 0; j < childCount; ++j) {
            final DeepShortcutView deepShortcutView2 = (DeepShortcutView)this.mShortcutsLayout.getChildAt(j);
            final int n = dimensionPixelSize - height;
            int n2;
            if (b) {
                n2 = childCount - j - 1;
            }
            else {
                n2 = j;
            }
            int n3;
            if (b) {
                n3 = 1;
            }
            else {
                n3 = -1;
            }
            animatorSet.play(translateYFrom(deepShortcutView2, n2 * n * n3));
            animatorSet.play(translateYFrom(deepShortcutView2.getBubbleText(), n / 2 * n3));
            animatorSet.play(translateYFrom(deepShortcutView2.getIconView(), n3 * (n / 2)));
            animatorSet.play(LauncherAnimUtils.ofPropertyValuesHolder(deepShortcutView2.getIconView(), new PropertyListBuilder().scale(1.0f).build()));
        }
        return animatorSet;
    }
}
