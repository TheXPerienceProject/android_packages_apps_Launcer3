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

package com.android.launcher3.popup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.CornerPathEffect;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Property;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.android.launcher3.AbstractFloatingView;
import com.android.launcher3.BubbleTextView;
import com.android.launcher3.DragSource;
import com.android.launcher3.DropTarget;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.Launcher;
import com.android.launcher3.LauncherAnimUtils;
import com.android.launcher3.LauncherModel;
import com.android.launcher3.Utilities;
import com.android.launcher3.accessibility.LauncherAccessibilityDelegate;
import com.android.launcher3.accessibility.ShortcutMenuAccessibilityDelegate;
import com.android.launcher3.anim.PropertyListBuilder;
import com.android.launcher3.anim.PropertyResetListener;
import com.android.launcher3.anim.RoundedRectRevealOutlineProvider;
import com.android.launcher3.badge.BadgeInfo;
import com.android.launcher3.dragndrop.DragController;
import com.android.launcher3.dragndrop.DragLayer;
import com.android.launcher3.dragndrop.DragOptions;
import com.android.launcher3.graphics.IconPalette;
import com.android.launcher3.graphics.TriangleShape;
import com.android.launcher3.notification.NotificationItemView;
import com.android.launcher3.notification.NotificationKeyData;
import com.android.launcher3.shortcuts.DeepShortcutManager;
import com.android.launcher3.shortcuts.DeepShortcutView;
import com.android.launcher3.shortcuts.ShortcutsItemView;
import com.android.launcher3.util.PackageUserKey;
import com.android.launcher3.util.Themes;
import com.google.android.apps.nexuslauncher.R;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.android.launcher3.userevent.nano.LauncherLogProto.ContainerType;
import static com.android.launcher3.userevent.nano.LauncherLogProto.ItemType;
import static com.android.launcher3.userevent.nano.LauncherLogProto.Target;

/**
 * A container for shortcuts to deep links within apps.
 */
@TargetApi(Build.VERSION_CODES.N)
public class PopupContainerWithArrow extends AbstractFloatingView implements DragSource,
        DragController.DragListener {

    protected final Launcher mLauncher;
    private final int mStartDragThreshold;
    private LauncherAccessibilityDelegate mAccessibilityDelegate;
    private final boolean mIsRtl;

    public ShortcutsItemView mShortcutsItemView;
    private NotificationItemView mNotificationItemView;

    protected BubbleTextView mOriginalIcon;
    private final Rect mTempRect = new Rect();
    private PointF mInterceptTouchDown = new PointF();
    private boolean mIsLeftAligned;
    protected boolean mIsAboveIcon;
    private View mArrow;

    protected Animator mOpenCloseAnimator;
    private boolean mDeferContainerRemoval;
    private AnimatorSet mReduceHeightAnimatorSet;

    private int mGravity;
    private Rect mStartRect = new Rect();
    private Rect mEndRect = new Rect();

    public PopupContainerWithArrow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mLauncher = Launcher.getLauncher(context);

        mStartDragThreshold = getResources().getDimensionPixelSize(
                R.dimen.deep_shortcuts_start_drag_threshold);
        mAccessibilityDelegate = new ShortcutMenuAccessibilityDelegate(mLauncher);
        mIsRtl = Utilities.isRtl(getResources());
    }

    public PopupContainerWithArrow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupContainerWithArrow(Context context) {
        this(context, null, 0);
    }

    public LauncherAccessibilityDelegate getAccessibilityDelegate() {
        return mAccessibilityDelegate;
    }

    /**
     * Shows the notifications and deep shortcuts associated with {@param icon}.
     * @return the container if shown or null.
     */
    public static PopupContainerWithArrow showForIcon(BubbleTextView icon) {
        Launcher launcher = Launcher.getLauncher(icon.getContext());
        if (getOpen(launcher) != null) {
            // There is already an items container open, so don't open this one.
            icon.clearFocus();
            return null;
        }
        ItemInfo itemInfo = (ItemInfo) icon.getTag();
        if (!DeepShortcutManager.supportsShortcuts(itemInfo)) {
            return null;
        }

        PopupDataProvider popupDataProvider = launcher.getPopupDataProvider();
        List<String> shortcutIds = popupDataProvider.getShortcutIdsForItem(itemInfo);
        List<NotificationKeyData> notificationKeys = popupDataProvider
                .getNotificationKeysForItem(itemInfo);
        List<SystemShortcut> systemShortcuts = popupDataProvider
                .getEnabledSystemShortcutsForItem(itemInfo);

        final PopupContainerWithArrow container =
                (PopupContainerWithArrow) launcher.getLayoutInflater().inflate(
                        R.layout.popup_container, launcher.getDragLayer(), false);
        container.setVisibility(View.INVISIBLE);
        launcher.getDragLayer().addView(container);
        container.populateAndShow(icon, shortcutIds, notificationKeys, systemShortcuts);
        return container;
    }

    public void populateAndShow(final BubbleTextView originalIcon, final List<String> shortcutIds,
            final List<NotificationKeyData> notificationKeys, List<SystemShortcut> systemShortcuts) {
        final Resources resources = getResources();
        final int arrowWidth = resources.getDimensionPixelSize(R.dimen.popup_arrow_width);
        final int arrowHeight = resources.getDimensionPixelSize(R.dimen.popup_arrow_height);
        final int arrowVerticalOffset = resources.getDimensionPixelSize(
                R.dimen.popup_arrow_vertical_offset);

        mOriginalIcon = originalIcon;

        // Add dummy views first, and populate with real info when ready.
        PopupPopulator.Item[] itemsToPopulate = PopupPopulator
                .getItemsToPopulate(shortcutIds, notificationKeys, systemShortcuts);
        addDummyViews(itemsToPopulate, notificationKeys.size());

        measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        orientAboutIcon(originalIcon, arrowHeight + arrowVerticalOffset);

        boolean reverseOrder = mIsAboveIcon;
        if (reverseOrder) {
            removeAllViews();
            mNotificationItemView = null;
            mShortcutsItemView = null;
            itemsToPopulate = PopupPopulator.reverseItems(itemsToPopulate);
            addDummyViews(itemsToPopulate, notificationKeys.size());

            measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
            orientAboutIcon(originalIcon, arrowHeight + arrowVerticalOffset);
        }

        ItemInfo originalItemInfo = (ItemInfo) originalIcon.getTag();
        List<DeepShortcutView> shortcutViews = mShortcutsItemView == null
                ? Collections.EMPTY_LIST
                : mShortcutsItemView.getDeepShortcutViews(reverseOrder);
        List<View> systemShortcutViews = mShortcutsItemView == null
                ? Collections.EMPTY_LIST
                : mShortcutsItemView.getSystemShortcutViews(reverseOrder);
        if (mNotificationItemView != null) {
            updateNotificationHeader();
        }

        int numShortcuts = shortcutViews.size() + systemShortcutViews.size();
        int numNotifications = notificationKeys.size();
        if (numNotifications == 0) {
            setContentDescription(getContext().getString(R.string.shortcuts_menu_description,
                    numShortcuts, originalIcon.getContentDescription().toString()));
        } else {
            setContentDescription(getContext().getString(
                    R.string.shortcuts_menu_with_notifications_description, numShortcuts,
                    numNotifications, originalIcon.getContentDescription().toString()));
        }

        // Add the arrow.
        final int arrowHorizontalOffset = resources.getDimensionPixelSize(isAlignedWithStart() ?
                R.dimen.popup_arrow_horizontal_offset_start :
                R.dimen.popup_arrow_horizontal_offset_end);
        mArrow = addArrowView(arrowHorizontalOffset, arrowVerticalOffset, arrowWidth, arrowHeight);
        mArrow.setPivotX(arrowWidth / 2);
        mArrow.setPivotY(mIsAboveIcon ? 0 : arrowHeight);
        measure(0, 0);

        animateOpen();

        mLauncher.getDragController().addDragListener(this);
        mOriginalIcon.forceHideBadge(true);

        // Load the shortcuts on a background thread and update the container as it animates.
        final Looper workerLooper = LauncherModel.getWorkerLooper();
        new Handler(workerLooper).postAtFrontOfQueue(PopupPopulator.createUpdateRunnable(
                mLauncher, originalItemInfo, new Handler(Looper.getMainLooper()),
                this, shortcutIds, shortcutViews, notificationKeys, mNotificationItemView,
                systemShortcuts, systemShortcutViews));
    }

    private void addDummyViews(PopupPopulator.Item[] itemTypesToPopulate,
            int count) {
        final Resources res = getResources();
        final LayoutInflater inflater = mLauncher.getLayoutInflater();

        int numItems = itemTypesToPopulate.length;
        int n2 = 3;
        for (int i = 0; i < numItems; i++) {
            PopupPopulator.Item itemTypeToPopulate = itemTypesToPopulate[i];
            PopupPopulator.Item previousItem =
                    i > 0 ? itemTypesToPopulate[i - 1] : null;
            PopupPopulator.Item nextItemTypeToPopulate =
                    i < numItems - 1 ? itemTypesToPopulate[i + 1] : null;
            final View item = inflater.inflate(itemTypeToPopulate.layoutId, this, false);

            boolean shouldAddTopMargin = previousItem != null
                    && itemTypeToPopulate.isShortcut ^ previousItem.isShortcut;

            boolean shouldAddBottomMargin = nextItemTypeToPopulate != null
                    && itemTypeToPopulate.isShortcut ^ nextItemTypeToPopulate.isShortcut;

            if (itemTypeToPopulate == PopupPopulator.Item.NOTIFICATION) {
                mNotificationItemView = (NotificationItemView) item;
                int footerHeight = count > 1 ?
                        res.getDimensionPixelSize(R.dimen.notification_footer_height) : 0;
                item.findViewById(R.id.footer).getLayoutParams().height = footerHeight;
                if (count > 1) {
                    mNotificationItemView.findViewById(R.id.divider).setVisibility(View.VISIBLE);
                }
                int n3 = 2 | 1;
                if (shouldAddTopMargin) {
                    n3 = 2;
                    mNotificationItemView.findViewById(R.id.gutter_top).setVisibility(VISIBLE);
                }
                if (shouldAddBottomMargin) {
                    n3 &= 0xFFFFFFFD;
                    mNotificationItemView.findViewById(R.id.gutter_bottom).setVisibility(VISIBLE);
                }
                mNotificationItemView.setBackgroundWithCorners(Themes.getAttrColor(mLauncher, R.attr.popupColorTertiary), n3);
                mNotificationItemView.getMainView().setAccessibilityDelegate(mAccessibilityDelegate);
            } else if (itemTypeToPopulate == PopupPopulator.Item.SHORTCUT) {
                item.setAccessibilityDelegate(mAccessibilityDelegate);
            }

            if (itemTypeToPopulate.isShortcut) {
                if (mShortcutsItemView == null) {
                    mShortcutsItemView = (ShortcutsItemView) inflater.inflate(
                            R.layout.shortcuts_item, this, false);
                    addView(mShortcutsItemView);
                    if (shouldAddTopMargin) {
                        n2 &= 0xFFFFFFFE;
                    }
                }
                if (itemTypeToPopulate != PopupPopulator.Item.SYSTEM_SHORTCUT_ICON && count > 0) {
                    final int height = item.getLayoutParams().height;
                    item.getLayoutParams().height = res.getDimensionPixelSize(R.dimen.bg_popup_item_condensed_height);
                    if (item instanceof DeepShortcutView) {
                        float n4 = item.getLayoutParams().height / height;
                        ((DeepShortcutView)item).getIconView().setScaleX(n4);
                        ((DeepShortcutView)item).getIconView().setScaleY(n4);
                    }
                }
                mShortcutsItemView.addShortcutView(item, itemTypeToPopulate);
                if (shouldAddBottomMargin) {
                    n2 &= 0xFFFFFFFD;
                }
            } else {
                addView(item);
            }
        }

        mShortcutsItemView.setBackgroundWithCorners(Themes.getAttrColor(mLauncher, R.attr.popupColorPrimary), n2);
        if (count > 0) {
            mShortcutsItemView.hideShortcuts(mIsAboveIcon, 2);
        }
    }

    protected PopupItemView getItemViewAt(int index) {
        if (!mIsAboveIcon) {
            // Opening down, so arrow is the first view.
            index++;
        }
        return (PopupItemView) getChildAt(index);
    }

    protected int getItemCount() {
        // All children except the arrow are items.
        return getChildCount() - 1;
    }

    /*private void animateOpen() {
        setVisibility(View.VISIBLE);
        mIsOpen = true;

        final AnimatorSet shortcutAnims = LauncherAnimUtils.createAnimatorSet();
        final int itemCount = getItemCount();

        final long duration = getResources().getInteger(
                R.integer.config_deepShortcutOpenDuration);
        final long arrowScaleDuration = getResources().getInteger(
                R.integer.config_deepShortcutArrowOpenDuration);
        final long arrowScaleDelay = duration - arrowScaleDuration;
        final long stagger = getResources().getInteger(
                R.integer.config_deepShortcutOpenStagger);
        final TimeInterpolator fadeInterpolator = new LogAccelerateInterpolator(100, 0);

        // Animate shortcuts
        DecelerateInterpolator interpolator = new DecelerateInterpolator();
        for (int i = 0; i < itemCount; i++) {
            final PopupItemView popupItemView = getItemViewAt(i);
            popupItemView.setVisibility(INVISIBLE);
            popupItemView.setAlpha(0);

            Animator anim = popupItemView.createOpenAnimation(mIsAboveIcon, mIsLeftAligned);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    popupItemView.setVisibility(VISIBLE);
                }
            });
            anim.setDuration(duration);
            int animationIndex = mIsAboveIcon ? itemCount - i - 1 : i;
            anim.setStartDelay(stagger * animationIndex);
            anim.setInterpolator(interpolator);
            shortcutAnims.play(anim);

            Animator fadeAnim = ObjectAnimator.ofFloat(popupItemView, View.ALPHA, 1);
            fadeAnim.setInterpolator(fadeInterpolator);
            // We want the shortcut to be fully opaque before the arrow starts animating.
            fadeAnim.setDuration(arrowScaleDelay);
            shortcutAnims.play(fadeAnim);
        }
        shortcutAnims.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mOpenCloseAnimator = null;
                Utilities.sendCustomAccessibilityEvent(
                        PopupContainerWithArrow.this,
                        AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                        getContext().getString(R.string.action_deep_shortcut));
            }
        });

        // Animate the arrow
        mArrow.setScaleX(0);
        mArrow.setScaleY(0);
        Animator arrowScale = createArrowScaleAnim(1).setDuration(arrowScaleDuration);
        arrowScale.setStartDelay(arrowScaleDelay);
        shortcutAnims.play(arrowScale);

        mOpenCloseAnimator = shortcutAnims;
        shortcutAnims.start();
    }*/

    private void animateOpen() {
        setVisibility(View.VISIBLE);
        mIsOpen = true;
        AnimatorSet animatorSet = LauncherAnimUtils.createAnimatorSet();
        Resources resources = this.getResources();
        long n = resources.getInteger(R.integer.config_popupOpenCloseDuration);
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        int n2 = 0;
        for (int i = 0; i < this.getItemCount(); ++i) {
            n2 += this.getItemViewAt(i).getMeasuredHeight();
        }
        Point computeAnimStartPoint = this.computeAnimStartPoint(n2);
        int n3 = mIsAboveIcon ? getPaddingTop() : computeAnimStartPoint.y;
        float backgroundRadius = this.getItemViewAt(0).getBackgroundRadius();
        mStartRect.set(computeAnimStartPoint.x, computeAnimStartPoint.y, computeAnimStartPoint.x, computeAnimStartPoint.y);
        mEndRect.set(0, n3, this.getMeasuredWidth(), n2 + n3);
        ValueAnimator revealAnimator = new RoundedRectRevealOutlineProvider(backgroundRadius, backgroundRadius, this.mStartRect, this.mEndRect).createRevealAnimator((View)this, false);
        revealAnimator.setDuration(n);
        revealAnimator.setInterpolator(accelerateDecelerateInterpolator);
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, PopupContainerWithArrow.ALPHA, new float[] { 0.0f, 1.0f });
        ((Animator)ofFloat).setDuration(n);
        ofFloat.setInterpolator(accelerateDecelerateInterpolator);
        animatorSet.play(ofFloat);
        mArrow.setScaleX(0.0f);
        mArrow.setScaleY(0.0f);
        ObjectAnimator setDuration = this.createArrowScaleAnim(1.0f).setDuration((long)resources.getInteger(R.integer.config_popupArrowOpenDuration));
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mOpenCloseAnimator = null;
                Utilities.sendCustomAccessibilityEvent(
                        PopupContainerWithArrow.this,
                        AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED,
                        getContext().getString(R.string.action_deep_shortcut));
            }
        });
        ((AnimatorSet)(mOpenCloseAnimator = animatorSet)).playSequentially(new Animator[] { revealAnimator, setDuration });
        animatorSet.start();
    }

    private Point computeAnimStartPoint(int n) {
        final Resources resources = this.getResources();
        int n2;
        if (this.mIsLeftAligned ^ this.mIsRtl) {
            n2 = 2131427457;
        }
        else {
            n2 = 2131427458;
        }
        int dimensionPixelSize = resources.getDimensionPixelSize(n2);
        if (!this.mIsLeftAligned) {
            dimensionPixelSize = this.getMeasuredWidth() - dimensionPixelSize;
        }
        final int n3 = this.getMeasuredHeight() - this.getPaddingTop() - this.getPaddingBottom() - n;
        final int paddingTop = this.getPaddingTop();
        if (!this.mIsAboveIcon) {
            n = n3;
        }
        return new Point(dimensionPixelSize, paddingTop + n);
    }

    private void enforceContainedWithinScreen(final int n, final int n2) {
        final DragLayer dragLayer = this.mLauncher.getDragLayer();
        if (this.getTranslationX() + n < 0.0f || this.getTranslationX() + n2 > dragLayer.getWidth()) {
            this.mGravity |= 0x1;
        }
        if (Gravity.isHorizontal(this.mGravity)) {
            this.setX((float)(dragLayer.getWidth() / 2 - this.getMeasuredWidth() / 2));
        }
        if (Gravity.isVertical(this.mGravity)) {
            this.setY((float)(dragLayer.getHeight() / 2 - this.getMeasuredHeight() / 2));
        }
    }

    /**
     * Orients this container above or below the given icon, aligning with the left or right.
     *
     * These are the preferred orientations, in order (RTL prefers right-aligned over left):
     * - Above and left-aligned
     * - Above and right-aligned
     * - Below and left-aligned
     * - Below and right-aligned
     *
     * So we always align left if there is enough horizontal space
     * and align above if there is enough vertical space.
     */
    private void orientAboutIcon(final BubbleTextView bubbleTextView, final int n) {
        final int measuredWidth = this.getMeasuredWidth();
        final int n2 = this.getMeasuredHeight() + n;
        final DragLayer dragLayer = this.mLauncher.getDragLayer();
        dragLayer.getDescendantRectRelativeToSelf((View)bubbleTextView, this.mTempRect);
        final Rect insets = dragLayer.getInsets();
        final int n3 = this.mTempRect.left + bubbleTextView.getPaddingLeft();
        final int n4 = this.mTempRect.right - measuredWidth - bubbleTextView.getPaddingRight();
        int n5;
        if (n3 + measuredWidth + insets.left < dragLayer.getRight() - insets.right) {
            n5 = 1;
        }
        else {
            n5 = 0;
        }
        int n6;
        if (n4 > dragLayer.getLeft() + insets.left) {
            n6 = 1;
        }
        else {
            n6 = 0;
        }
        int n7 = 0;
        Label_0209: {
            if (n5 != 0) {
                if (!this.mIsRtl) {
                    n7 = n3;
                    break Label_0209;
                }
                if (n6 == 0) {
                    n7 = n3;
                    break Label_0209;
                }
            }
            n7 = n4;
        }
        this.mIsLeftAligned = (n7 == n3);
        int n8;
        if (this.mIsRtl) {
            n8 = n7 - (dragLayer.getWidth() - measuredWidth);
        }
        else {
            n8 = n7;
        }
        final int n9 = (int)((bubbleTextView.getWidth() - bubbleTextView.getTotalPaddingLeft() - bubbleTextView.getTotalPaddingRight()) * bubbleTextView.getScaleX());
        final Resources resources = this.getResources();
        int n10;
        if (this.isAlignedWithStart()) {
            n10 = n9 / 2 - resources.getDimensionPixelSize(R.dimen.deep_shortcut_icon_size) / 2 - resources.getDimensionPixelSize(R.dimen.popup_padding_start);
        }
        else {
            n10 = n9 / 2 - resources.getDimensionPixelSize(R.dimen.deep_shortcut_drag_handle_size) / 2 - resources.getDimensionPixelSize(R.dimen.popup_padding_end);
        }
        if (!this.mIsLeftAligned) {
            n10 = -n10;
        }
        final int n11 = n8 + n10;
        int n12;
        if (bubbleTextView.getIcon() != null) {
            n12 = bubbleTextView.getIcon().getBounds().height();
        }
        else {
            n12 = bubbleTextView.getHeight();
        }
        final int n13 = this.mTempRect.top + bubbleTextView.getPaddingTop() - n2;
        int n14;
        if (!(this.mIsAboveIcon = (n13 > dragLayer.getTop() + insets.top))) {
            n14 = n12 + (this.mTempRect.top + bubbleTextView.getPaddingTop());
        }
        else {
            n14 = n13;
        }
        int n15;
        if (this.mIsRtl) {
            n15 = insets.right + n11;
        }
        else {
            n15 = n11 - insets.left;
        }
        final int n16 = n14 - insets.top;
        this.mGravity = 0;
        int n17;
        if (n16 + n2 > dragLayer.getBottom() - insets.bottom) {
            this.mGravity = 16;
            n17 = n3 + n9 - insets.left;
            final int n18 = n4 - n9 - insets.left;
            if (!this.mIsRtl) {
                if (n17 + measuredWidth < dragLayer.getRight()) {
                    this.mIsLeftAligned = true;
                }
                else {
                    this.mIsLeftAligned = false;
                    n17 = n18;
                }
            }
            else if (n18 > dragLayer.getLeft()) {
                this.mIsLeftAligned = false;
                n17 = n18;
            }
            else {
                this.mIsLeftAligned = true;
            }
            this.mIsAboveIcon = true;
        }
        else {
            n17 = n15;
        }
        this.setX((float)n17);
        this.setY((float)n16);
    }

    private boolean isAlignedWithStart() {
        return mIsLeftAligned && !mIsRtl || !mIsLeftAligned && mIsRtl;
    }

    /**
     * Adds an arrow view pointing at the original icon.
     * @param horizontalOffset the horizontal offset of the arrow, so that it
     *                              points at the center of the original icon
     */
    private View addArrowView(int horizontalOffset, int verticalOffset, int width, int height) {
        LayoutParams layoutParams = new LayoutParams(width, height);
        if (mIsLeftAligned) {
            layoutParams.gravity = Gravity.LEFT;
            layoutParams.leftMargin = horizontalOffset;
        } else {
            layoutParams.gravity = Gravity.RIGHT;
            layoutParams.rightMargin = horizontalOffset;
        }
        if (mIsAboveIcon) {
            layoutParams.topMargin = verticalOffset;
        } else {
            layoutParams.bottomMargin = verticalOffset;
        }

        View arrowView = new View(getContext());
        if (Gravity.isVertical(mGravity)) {
            // This is only true if there wasn't room for the container next to the icon,
            // so we centered it instead. In that case we don't want to show the arrow.
            arrowView.setVisibility(INVISIBLE);
        } else {
            ShapeDrawable arrowDrawable = new ShapeDrawable(TriangleShape.create(
                    width, height, !mIsAboveIcon));
            Paint arrowPaint = arrowDrawable.getPaint();
            // Note that we have to use getChildAt() instead of getItemViewAt(),
            // since the latter expects the arrow which hasn't been added yet.
            PopupItemView itemAttachedToArrow = (PopupItemView)
                    (getChildAt(mIsAboveIcon ? getChildCount() - 1 : 0));
            arrowPaint.setColor(Themes.getAttrColor(mLauncher, R.attr.popupColorPrimary));
            // The corner path effect won't be reflected in the shadow, but shouldn't be noticeable.
            int radius = getResources().getDimensionPixelSize(R.dimen.popup_arrow_corner_radius);
            arrowPaint.setPathEffect(new CornerPathEffect(radius));
            arrowView.setBackground(arrowDrawable);
            arrowView.setElevation(getElevation());
        }
        addView(arrowView, mIsAboveIcon ? getChildCount() : 0, layoutParams);
        return arrowView;
    }

    @Override
    public View getExtendedTouchView() {
        return mOriginalIcon;
    }

    /**
     * Determines when the deferred drag should be started.
     *
     * Current behavior:
     * - Start the drag if the touch passes a certain distance from the original touch down.
     */
    public DragOptions.PreDragCondition createPreDragCondition() {
        return new DragOptions.PreDragCondition() {
            @Override
            public boolean shouldStartDrag(double distanceDragged) {
                return distanceDragged > mStartDragThreshold;
            }

            @Override
            public void onPreDragStart(DropTarget.DragObject dragObject) {
                if (mIsAboveIcon) {
                    mOriginalIcon.setIconVisible(false);
                    mOriginalIcon.setVisibility(View.VISIBLE);
                }
                else {
                    mOriginalIcon.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPreDragEnd(DropTarget.DragObject dragObject, boolean dragStarted) {
                mOriginalIcon.setIconVisible(true);
                if (dragStarted) {
                    mOriginalIcon.setVisibility(View.INVISIBLE);
                } else {
                    mLauncher.getUserEventDispatcher().logDeepShortcutsOpen(mOriginalIcon);
                    if (!mIsAboveIcon) {
                        mOriginalIcon.setVisibility(VISIBLE);
                        mOriginalIcon.setTextVisibility(false);
                    }
                }
            }
        };
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            mInterceptTouchDown.set(ev.getX(), ev.getY());
            return false;
        }
        // Stop sending touch events to deep shortcut views if user moved beyond touch slop.
        return Math.hypot(mInterceptTouchDown.x - ev.getX(), mInterceptTouchDown.y - ev.getY())
                > ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    /**
     * Updates the notification header if the original icon's badge updated.
     */
    public void updateNotificationHeader(Set<PackageUserKey> updatedBadges) {
        ItemInfo itemInfo = (ItemInfo) mOriginalIcon.getTag();
        PackageUserKey packageUser = PackageUserKey.fromItemInfo(itemInfo);
        if (updatedBadges.contains(packageUser)) {
            updateNotificationHeader();
        }
    }

    private void updateNotificationHeader() {
        ItemInfo itemInfo = (ItemInfo) mOriginalIcon.getTag();
        BadgeInfo badgeInfo = mLauncher.getPopupDataProvider().getBadgeInfoForItem(itemInfo);
        if (mNotificationItemView != null && badgeInfo != null) {
            IconPalette palette = mOriginalIcon.getBadgePalette();
            mNotificationItemView.updateHeader(badgeInfo.getNotificationCount(), palette);
        }
    }

    public Animator adjustItemHeights(final int n, final int n2, final int n3) {
        final int n4 = 1;
        if (this.mReduceHeightAnimatorSet != null) {
            this.mReduceHeightAnimatorSet.cancel();
        }
        final int n5 = mIsAboveIcon ? n - n2 : -n;
        this.mReduceHeightAnimatorSet = LauncherAnimUtils.createAnimatorSet();
        int n6;
        if (n == this.mNotificationItemView.getHeightMinusFooter()) {
            n6 = n4;
        }
        else {
            n6 = 0;
        }
        int n7;
        if (this.mIsAboveIcon) {
            n7 = n6;
        }
        else {
            n7 = 0;
        }
        this.mReduceHeightAnimatorSet.play(this.mNotificationItemView.animateHeightRemoval(n, n7 != 0));
        final PropertyResetListener propertyResetListener = new PropertyResetListener(PopupContainerWithArrow.TRANSLATION_Y, 0.0f);
        int i = 0;
        int n8 = 0;
        while (i < this.getItemCount()) {
            final PopupItemView itemView = this.getItemViewAt(i);
            if (n8 != 0) {
                itemView.setTranslationY(itemView.getTranslationY() - n2);
            }
            if (itemView != this.mNotificationItemView || (this.mIsAboveIcon && n6 == 0)) {
                final Property translation_Y = PopupContainerWithArrow.TRANSLATION_Y;
                final float[] array = new float[n4];
                array[0] = itemView.getTranslationY() + n5;
                final ObjectAnimator setDuration = ObjectAnimator.ofFloat(itemView, translation_Y, array).setDuration((long)n3);
                setDuration.addListener(propertyResetListener);
                this.mReduceHeightAnimatorSet.play(setDuration);
                if (itemView == this.mShortcutsItemView) {
                    n8 = n4;
                }
            }
            ++i;
        }
        if (this.mIsAboveIcon) {
            this.mArrow.setTranslationY(this.mArrow.getTranslationY() - n2);
        }
        mReduceHeightAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(final Animator animator) {
                if (mIsAboveIcon) {
                    setTranslationY(getTranslationY() + n5);
                    mArrow.setTranslationY(0.0f);
                }
                mReduceHeightAnimatorSet = null;
            }
        });
        return mReduceHeightAnimatorSet;
    }

    public void trimNotifications(Map<PackageUserKey, BadgeInfo> map) {
        final int n = 1;
        if (this.mNotificationItemView == null) {
            return;
        }
        BadgeInfo badgeInfo = map.get(PackageUserKey.fromItemInfo((ItemInfo)this.mOriginalIcon.getTag()));
        if (badgeInfo == null || badgeInfo.getNotificationKeys().size() == 0) {
            final AnimatorSet animatorSet = LauncherAnimUtils.createAnimatorSet();
            int hiddenShortcutsHeight;
            if (this.mShortcutsItemView != null) {
                hiddenShortcutsHeight = this.mShortcutsItemView.getHiddenShortcutsHeight();
                this.mShortcutsItemView.setBackgroundWithCorners(Themes.getAttrColor((Context)this.mLauncher, 2130772007), 3);
                animatorSet.play(this.mShortcutsItemView.showAllShortcuts(this.mIsAboveIcon));
            }
            else {
                hiddenShortcutsHeight = 0;
            }
            final int integer = this.getResources().getInteger(R.integer.config_removeNotificationViewDuration);
            animatorSet.play(this.adjustItemHeights(this.mNotificationItemView.getHeightMinusFooter(), hiddenShortcutsHeight, integer));
            final Property alpha = PopupContainerWithArrow.ALPHA;
            final float[] array = new float[n];
            array[0] = 0.0f;
            final ObjectAnimator setDuration = ObjectAnimator.ofFloat((Object)mNotificationItemView, alpha, array).setDuration((long)integer);
            setDuration.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(final Animator animator) {
                    removeView(mNotificationItemView);
                    mNotificationItemView = null;
                    if (getItemCount() == 0) {
                        close(false);
                    }
                }
            });
            animatorSet.play(setDuration);
            final long n2 = this.getResources().getInteger(R.integer.config_popupArrowOpenDuration);
            final ObjectAnimator setDuration2 = this.createArrowScaleAnim(0.0f).setDuration(n2);
            setDuration2.setStartDelay(0L);
            final ObjectAnimator setDuration3 = this.createArrowScaleAnim(1.0f).setDuration(n2);
            setDuration3.setStartDelay((long)(integer - n2 * 1.5));
            final Animator[] array2 = { setDuration2, null };
            array2[n] = setDuration3;
            animatorSet.playSequentially(array2);
            animatorSet.start();
            return;
        }
        this.mNotificationItemView.trimNotifications(NotificationKeyData.extractKeysOnly(badgeInfo.getNotificationKeys()));
    }

    @Override
    protected void onWidgetsBound() {
        if (mShortcutsItemView != null) {
            mShortcutsItemView.enableWidgetsIfExist(mOriginalIcon);
        }
    }

    private ObjectAnimator createArrowScaleAnim(float scale) {
        return LauncherAnimUtils.ofPropertyValuesHolder(
                mArrow, new PropertyListBuilder().scale(scale).build());
    }

    /**
     * Animates the height of the notification item and the translationY of other items accordingly.
     */
    public Animator reduceNotificationViewHeight(int heightToRemove, int duration) {
        return adjustItemHeights(heightToRemove, 0, duration);
    }

    @Override
    public boolean supportsAppInfoDropTarget() {
        return true;
    }

    @Override
    public boolean supportsDeleteDropTarget() {
        return false;
    }

    @Override
    public float getIntrinsicIconScaleFactor() {
        return 1f;
    }

    @Override
    public void onDropCompleted(View target, DropTarget.DragObject d, boolean isFlingToDelete,
            boolean success) {
        if (!success) {
            d.dragView.remove();
            mLauncher.showWorkspace(true);
            mLauncher.getDropTargetBar().onDragEnd();
        }
    }

    @Override
    public void onDragStart(DropTarget.DragObject dragObject, DragOptions options) {
        // Either the original icon or one of the shortcuts was dragged.
        // Hide the container, but don't remove it yet because that interferes with touch events.
        mDeferContainerRemoval = true;
        animateClose();
    }

    @Override
    public void onDragEnd() {
        if (!mIsOpen) {
            if (mOpenCloseAnimator != null) {
                // Close animation is running.
                mDeferContainerRemoval = false;
            } else {
                // Close animation is not running.
                if (mDeferContainerRemoval) {
                    closeComplete();
                }
            }
        }
    }

    @Override
    public void fillInLogContainerData(View v, ItemInfo info, Target target, Target targetParent) {
        target.itemType = ItemType.DEEPSHORTCUT;
        targetParent.containerType = ContainerType.DEEPSHORTCUTS;
    }

    @Override
    protected void handleClose(boolean animate) {
        if (animate) {
            animateClose();
        } else {
            closeComplete();
        }
    }

    protected void animateClose() {
        if (!mIsOpen) {
            return;
        }
        final int n = 1;
        mEndRect.setEmpty();
        if (mOpenCloseAnimator != null) {
            Outline outline = new Outline();
            getOutlineProvider().getOutline(this, outline);
            outline.getRect(mEndRect);
            mOpenCloseAnimator.cancel();
        }
        mIsOpen = false;

        final AnimatorSet shortcutAnims = LauncherAnimUtils.createAnimatorSet();
        final long duration = getResources().getInteger(
                R.integer.config_deepShortcutCloseDuration);
        AccelerateDecelerateInterpolator accelerateDecelerateInterpolator = new AccelerateDecelerateInterpolator();
        int i = 0;
        int n2 = 0;
        while (i < getItemCount()) {
            n2 += getItemViewAt(i).getMeasuredHeight();
            ++i;
        }
        Point computeAnimStartPoint = computeAnimStartPoint(n2);
        int n3 = mIsAboveIcon ? getPaddingTop() : computeAnimStartPoint.y;
        float backgroundRadius = this.getItemViewAt(0).getBackgroundRadius();
        mStartRect.set(computeAnimStartPoint.x, computeAnimStartPoint.y, computeAnimStartPoint.x, computeAnimStartPoint.y);
        if (mEndRect.isEmpty()) {
            mEndRect.set(0, n3, this.getMeasuredWidth(), n2 + n3);
        }
        final ValueAnimator revealAnimator = new RoundedRectRevealOutlineProvider(backgroundRadius, backgroundRadius, this.mStartRect, this.mEndRect).createRevealAnimator((View)this, n != 0);
        revealAnimator.setDuration(duration);
        revealAnimator.setInterpolator(accelerateDecelerateInterpolator);
        shortcutAnims.play(revealAnimator);
        final Property alpha = PopupContainerWithArrow.ALPHA;
        final float[] array = new float[n];
        array[0] = 0.0f;
        final ObjectAnimator ofFloat = ObjectAnimator.ofFloat((Object)this, alpha, array);
        ((Animator)ofFloat).setDuration(duration);
        ofFloat.setInterpolator(accelerateDecelerateInterpolator);
        shortcutAnims.play(ofFloat);
        final ObjectAnimator textAlphaAnimator = this.mOriginalIcon.createTextAlphaAnimator(n != 0);
        ((Animator)textAlphaAnimator).setDuration(duration);
        shortcutAnims.play(textAlphaAnimator);
        shortcutAnims.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mOpenCloseAnimator = null;
                if (mDeferContainerRemoval) {
                    setVisibility(INVISIBLE);
                } else {
                    closeComplete();
                }
            }
        });
        ((mOpenCloseAnimator = shortcutAnims)).start();
        mOriginalIcon.forceHideBadge(false);
    }

    /**
     * Closes the folder without animation.
     */
    protected void closeComplete() {
        if (mOpenCloseAnimator != null) {
            mOpenCloseAnimator.cancel();
            mOpenCloseAnimator = null;
        }
        mIsOpen = false;
        mDeferContainerRemoval = false;
        mOriginalIcon.setTextVisibility(mOriginalIcon.shouldTextBeVisible());
        mOriginalIcon.forceHideBadge(false);
        mLauncher.getDragController().removeDragListener(this);
        mLauncher.getDragLayer().removeView(this);
    }

    @Override
    protected boolean isOfType(int type) {
        return (type & TYPE_POPUP_CONTAINER_WITH_ARROW) != 0;
    }

    /**
     * Returns a DeepShortcutsContainer which is already open or null
     */
    public static PopupContainerWithArrow getOpen(Launcher launcher) {
        return getOpenView(launcher, TYPE_POPUP_CONTAINER_WITH_ARROW);
    }

    @Override
    public int getLogContainerType() {
        return ContainerType.DEEPSHORTCUTS;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        enforceContainedWithinScreen(l, r);
    }

    public interface RoundedCornerFlags {
    }
}
