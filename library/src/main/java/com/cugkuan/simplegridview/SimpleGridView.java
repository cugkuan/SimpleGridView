package com.cugkuan.simplegridview;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 一个简单的GridView
 */
public class SimpleGridView extends ViewGroup {


    private int numColumns = 3;

    /**
     * 水平方向的单元格的间隔
     */
    private int mVerticalSpace;

    /**
     * 水平方向的单元格间隔
     */
    private int mHorizontalSpace;

    /**
     * 宽度 ： 高度的比率
     */
    private float mRatio;

    private int mCellWidth;

    private int mCellHeight;

    private GridViewAdapter mAdapter;


    public SimpleGridView(Context context) {
        this(context, null);
    }

    public SimpleGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        if (attrs == null) {
            return;
        }

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SimpleGridView);
        numColumns = array.getInt(R.styleable.SimpleGridView_android_numColumns, 3);
        if (numColumns <= 0) {
            numColumns = 1;
        }
        mVerticalSpace = array.getDimensionPixelSize(R.styleable.SimpleGridView_android_verticalSpacing, 0);
        mHorizontalSpace = array.getDimensionPixelSize(R.styleable.SimpleGridView_android_horizontalSpacing, 0);
        mRatio = array.getFloat(R.styleable.SimpleGridView_simpleGridView_whRatio, 1.0f);

        array.recycle();
    }

    private DataSetObserver mDataSetObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            removeAllViews();
            if (mAdapter != null) {
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    View view = mAdapter.getView(getContext(), SimpleGridView.this, i);
                    addView(view);
                }
            }
            invalidate();
        }

        @Override
        public void onInvalidated() {
            super.onInvalidated();
        }
    };

    public void setAdapter(GridViewAdapter adapter) {

        if (mAdapter != null) {
            mAdapter.unregisterDataSetObserver(mDataSetObserver);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.registerDataSetObserver(mDataSetObserver);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        mCellWidth = (width - (numColumns - 1) * mHorizontalSpace - getPaddingLeft() - getPaddingRight()) / numColumns;
        mCellHeight = (int) ((float) mCellWidth / mRatio);
        int count = getChildCount();
        int lineCount = count / numColumns + count % numColumns == 0 ? 0 : 1;
        int height = mCellHeight * count + mVerticalSpace * (lineCount - 1) + getPaddingTop() + getPaddingBottom();
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        //行
        int row = 0;
        //列
        int line = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            view.measure(MeasureSpec.makeMeasureSpec(mCellWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(mCellHeight, MeasureSpec.EXACTLY));
            row = i / numColumns;
            line = i % numColumns;
            int left = line * (mCellWidth + mVerticalSpace) + getPaddingLeft();
            int top = row * (mCellHeight + mVerticalSpace) + getPaddingTop();
            view.layout(left, top, left + mCellWidth, top + mCellHeight);
        }
    }

    public static abstract class GridViewAdapter {

        private final DataSetObservable mDataSetObservable = new DataSetObservable();

        abstract protected View getView(Context context, ViewGroup parent, int position);

        abstract public int getCount();

        public void notifyDataSetChanged() {
            mDataSetObservable.notifyChanged();
        }

        /**
         * Register an observer that is called when changes happen to the data used by this adapter.
         *
         * @param observer the object that gets notified when the data set changes.
         */
        void registerDataSetObserver(DataSetObserver observer) {
            mDataSetObservable.registerObserver(observer);
        }

        /**
         * Unregister an observer that has previously been registered with this
         * adapter via {@link #registerDataSetObserver}.
         *
         * @param observer the object to unregister.
         */
        void unregisterDataSetObserver(DataSetObserver observer) {
            mDataSetObservable.unregisterObserver(observer);
        }
    }
}
