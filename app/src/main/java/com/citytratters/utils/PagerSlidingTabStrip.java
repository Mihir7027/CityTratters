package com.citytratters.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;


import com.citytratters.R;

import java.util.Locale;


public class PagerSlidingTabStrip extends HorizontalScrollView {
    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    private final PageListener pageListener = new PageListener();
    public ViewPager.OnPageChangeListener delegatePageListener;
    // @formatter:on
    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;
    private LinearLayout tabsContainer;
    private ViewPager pager;
    private int tabCount;
    private int currentPosition = 0;
    private float currentPositionOffset = 0f;
    private Paint rectPaint;
    private Paint dividerPaint;
    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;
    private boolean shouldExpand = false;
    private boolean textAllCaps = false;
    private int scrollOffset = 52;
    private int indicatorHeight = 4;
    private float arrowHeight = (float) 18.00;
    private int underlineHeight = 4;
    private int dividerPadding = 12;
    private int tabPadding = 18;
    private int dividerWidth = 1;
    private int tabTextSize = 13;
    private int tabTextColor = 0xFF666666;
    private int tabActiveTextColor = 0xFF3A2C94;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;
    private int lastScrollX = 0;
    private int tabBackgroundResId = R.drawable.background_tab;
    private Locale locale;
    private int indicatorShadowHeight = 1;
    private int indicatorShadowColor = 0x1A000000;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFillViewport(true);
        setWillNotDraw(false);
        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        indicatorShadowHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorShadowHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
// get system attrs (android:textSize and android:textColor)
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);
        a.recycle();
// get custom attrs
        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        indicatorShadowHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorShadowHeight, indicatorShadowHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);
        a.recycle();
        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);
        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);
        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setViewPager(ViewPager pager, int currentPosition) {
        this.currentPosition = currentPosition;
        this.pager = pager;
        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }
        pager.addOnPageChangeListener(pageListener);
        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {
        tabsContainer.removeAllViews();
        tabCount = pager.getAdapter().getCount();
        for (int i = 0; i < tabCount; i++) {
            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            } else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }
        }
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                currentPosition = pager.getCurrentItem();
                currentPositionOffset = 0;
                scrollToChild(currentPosition, 0);
            }
        });
        updateTabStyles();
    }

    public void addTextTab(final int position, String title) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        addTab(position, tab);

    }

    public void addIconTab(final int position, int resId) {
        ImageButton tab = new ImageButton(getContext());
        tab.setImageResource(resId);
        addTab(position, tab);
    }

    public void addTab(final int position, View tab) {
        tab.setFocusable(true);
        tab.setOnClickListener(v -> pager.setCurrentItem(position));
        tab.setPadding(tabPadding, 0, tabPadding, 0);
        tabsContainer.addView(tab, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    public void updateTabStyles() {
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            v.setBackgroundResource(tabBackgroundResId);
            if (v instanceof TextView) {
                TextView tab = (TextView) v;
                tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
                tab.setTypeface(tabTypeface, tabTypefaceStyle);
                //tab.setTextColor(tabTextColor);

                if (i == currentPosition) {
                    tab.setTextColor(tabActiveTextColor);
                    tab.setTypeface(null, Typeface.BOLD);
                } else {
                    tab.setTextColor(tabTextColor);
                    tab.setTypeface(null, Typeface.BOLD);
                }

// setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
// pre-ICS-build
                if (textAllCaps) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                        tab.setAllCaps(true);
                    } else {
                        tab.setText(tab.getText().toString());
                    }
                }
            }
        }
    }

    private void scrollToChild(int position, int offset) {
        if (tabCount == 0) {
            return;
        }
        int newScrollX = tabsContainer.getChildAt(position).getLeft() + offset;
        if (position > 0 || offset > 0) {
            newScrollX -= scrollOffset;
        }
        if (newScrollX != lastScrollX) {
            lastScrollX = newScrollX;
            scrollTo(newScrollX, 0);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0) {
            return;
        }
        final int height = getHeight();
// draw indicator line
        rectPaint.setColor(indicatorColor);
        rectPaint.setColor(indicatorShadowColor);
// default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);


        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
// if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();
            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }
        // draw underline
/*
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);
*/

        // draw indicator line
      /*  rectPaint.setColor(indicatorColor);
        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);
*/
/*
        rectPaint.setColor(indicatorShadowColor);
        canvas.drawRect(lineLeft, height - indicatorShadowHeight, lineRight, height, rectPaint);
*/

        // draw triangle down arrow


        Paint paint = new Paint();
        paint.setColor(indicatorColor);
        paint.setStrokeWidth(2);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);

        float leftPointX = lineLeft + (lineRight - lineLeft) / 2 - arrowHeight * 2;
        float rightPointX = leftPointX + arrowHeight * 2;
        float bottomPointX = leftPointX + arrowHeight;
        float leftPointY = height - arrowHeight;
        float bottomPointY = height;

        Point left = new Point((int) leftPointX, (int) leftPointY);
        Point right = new Point((int) rightPointX, (int) leftPointY);
        Point bottom = new Point((int) bottomPointX, (int) bottomPointY);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.setLastPoint(left.x, left.y);
        path.lineTo(right.x, right.y);
        path.lineTo(bottom.x, bottom.y);
        path.lineTo(left.x, left.y);
        path.close();
        canvas.drawPath(path, paint);


//draw triangle up arrow

/*

        float aPointLeft = (lineRight - lineLeft) / 2 + lineLeft;
        float aPointHeight = height - indicatorHeight - arrowHeight - 2;
        float bPointLeft = aPointLeft - arrowHeight * 2;
        float bPointHeight = height - arrowHeight;
        float cPointLeft = aPointLeft + arrowHeight * 2;
        float cPointHeight = height - arrowHeight;

        Paint paint = new Paint();
        paint.setColor(indicatorColor);
        paint.setStrokeWidth(2);
        paint.setStyle(Style.FILL_AND_STROKE);
        paint.setAntiAlias(true);


        Point a = new Point((int) aPointLeft, (int) aPointHeight);
        Point b = new Point((int) bPointLeft, (int) bPointHeight);
        Point c = new Point((int) cPointLeft, (int) cPointHeight);

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.lineTo(a.x, a.y);

        path.close();
*/


// draw divider
        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextSize(int textSizePx) {
        this.tabTextSize = textSizePx;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public void setTextActiveColor(int textColor) {
        this.tabActiveTextColor = textColor;
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        updateTabStyles();
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    public interface IconTabProvider {
        public int getPageIconResId(int position);
    }

    static class SavedState extends BaseSavedState {
        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }
    }

    private class PageListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;
            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));
            invalidate();
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
            //updateTabStyles();
            /*for (int i = 0; i < tabCount; i++) {
                View v = tabsContainer.getChildAt(i);
                v.setBackgroundResource(tabBackgroundResId);
                if (v instanceof TextView) {
                    TextView tv = (TextView) v;

                    if (i == position) {
                        tv.setTextColor(tabActiveTextColor);
                        tv.setTypeface(null, Typeface.BOLD);
                    } else {
                        tv.setTextColor(tabTextColor);
                        tv.setTypeface(null, Typeface.NORMAL);
                    }
                }
            }*/
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
            for (int i = 0; i < tabCount; i++) {
                View v = tabsContainer.getChildAt(i);
                v.setBackgroundResource(tabBackgroundResId);
                if (v instanceof TextView) {
                    TextView tv = (TextView) v;

                    if (i == position) {
                        tv.setTextColor(tabActiveTextColor);
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setPaintFlags(tv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                    } else {
                        tv.setTextColor(tabTextColor);
                        tv.setTypeface(null, Typeface.BOLD);
                        tv.setPaintFlags(0);
                    }
                }
            }
        }
    }
}