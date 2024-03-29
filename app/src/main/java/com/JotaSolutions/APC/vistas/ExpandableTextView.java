

package com.JotaSolutions.APC.vistas;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.JotaSolutions.APC.R;


public class ExpandableTextView extends LinearLayout implements View.OnClickListener {

    private static final String TAG = ExpandableTextView.class.getSimpleName();

    /* numero de lineas por default*/
    private static final int MAX_COLLAPSED_LINES = 8;

    /* la duracion de la animacion */
    private static final int DEFAULT_ANIM_DURATION = 300;

    /* valor alfa por default (se usa en animacion) */
    private static final float DEFAULT_ANIM_ALPHA_START = 0.7f;

    /* aqui seteamos el defecto en caso de mostrar menos */
    private static final String COLLAPS_BUTTON_TEXT = "show less";

    /* aqui seteamos el defecto en caso de mostrar mas  */
    private static final String EXPAND_BUTTON_TEXT = "show more";

    protected TextView mTv;

    protected TextView mButton;

    private boolean mRelayout;

    private boolean mCollapsed = true;

    private int mCollapsedHeight;

    private int mTextHeightWithMaxLines;

    private int mMaxCollapsedLines;

    private int mMarginBetweenTxtAndBottom;

    private String mExpandText;

    private String mCollapseText;

    private int mAnimationDuration;

    private float mAnimAlphaStart;

    private boolean mAnimating;


    private OnExpandStateChangeListener mListener;


    private SparseBooleanArray mCollapsedStatus;
    private int mPosition;

    public ExpandableTextView(Context context) {
        this(context, null);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs);
    }

    @Override
    public void setOrientation(int orientation) {
        if (LinearLayout.HORIZONTAL == orientation) {
            throw new IllegalArgumentException("ExpandableTextView only supports Vertical Orientation.");
        }
        super.setOrientation(orientation);
    }

    @Override
    public void onClick(View view) {
        if (mButton.getVisibility() != View.VISIBLE) {
            return;
        }

        mCollapsed = !mCollapsed;
        mButton.setText(mCollapsed ? mExpandText : mCollapseText);

        if (mCollapsedStatus != null) {
            mCollapsedStatus.put(mPosition, mCollapsed);
        }


        mAnimating = true;

        Animation animation;
        if (mCollapsed) {
            animation = new ExpandCollapseAnimation(this, getHeight(), mCollapsedHeight);
        } else {
            animation = new ExpandCollapseAnimation(this, getHeight(), getHeight() +
                    mTextHeightWithMaxLines - mTv.getHeight());
        }

        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                applyAlphaAnimation(mTv, mAnimAlphaStart);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                clearAnimation();

                mAnimating = false;

                // notificamos al listener
                if (mListener != null) {
                    mListener.onExpandStateChanged(mTv, !mCollapsed);
                }

                if (mCollapsed) {
                    mTv.setMaxLines(mMaxCollapsedLines);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        clearAnimation();
        startAnimation(animation);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        return mAnimating;
    }

    @Override
    protected void onFinishInflate() {
        findViews();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (!mRelayout || getVisibility() == View.GONE) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        mRelayout = false;


        mButton.setVisibility(View.GONE);
        mTv.setMaxLines(Integer.MAX_VALUE);


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        if (mTv.getLineCount() <= mMaxCollapsedLines) {
            return;
        }


        mTextHeightWithMaxLines = getRealTextViewHeight(mTv);


        if (mCollapsed) {
            mTv.setMaxLines(mMaxCollapsedLines);
        }
        mButton.setVisibility(View.VISIBLE);


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mCollapsed) {

            mTv.post(new Runnable() {
                @Override
                public void run() {
                    mMarginBetweenTxtAndBottom = getHeight() - mTv.getHeight();
                }
            });

            mCollapsedHeight = getMeasuredHeight();
        }
    }

    public void setOnExpandStateChangeListener(@Nullable OnExpandStateChangeListener listener) {
        mListener = listener;
    }

    public void setText(@Nullable CharSequence text) {
        mRelayout = true;
        mTv.setText(text);
        setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
    }

    public void setText(@Nullable CharSequence text, @NonNull SparseBooleanArray collapsedStatus, int position) {
        mCollapsedStatus = collapsedStatus;
        mPosition = position;
        boolean isCollapsed = collapsedStatus.get(position, true);
        clearAnimation();
        mCollapsed = isCollapsed;
        mButton.setText(mCollapsed ? mExpandText : mCollapseText);
        setText(text);
        getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        requestLayout();
    }

    @Nullable
    public CharSequence getText() {
        if (mTv == null) {
            return "";
        }
        return mTv.getText();
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ExpandableTextView);
        mMaxCollapsedLines = typedArray.getInt(R.styleable.ExpandableTextView_maxCollapsedLines, MAX_COLLAPSED_LINES);
        mAnimationDuration = typedArray.getInt(R.styleable.ExpandableTextView_animDuration, DEFAULT_ANIM_DURATION);
        mAnimAlphaStart = typedArray.getFloat(R.styleable.ExpandableTextView_animAlphaStart, DEFAULT_ANIM_ALPHA_START);
        mExpandText = typedArray.getString(R.styleable.ExpandableTextView_expandText);
        mCollapseText = typedArray.getString(R.styleable.ExpandableTextView_collapseText);

        if (mExpandText == null) {
            mExpandText = EXPAND_BUTTON_TEXT;
        }
        if (mCollapseText == null) {
            mCollapseText = COLLAPS_BUTTON_TEXT;
        }

        typedArray.recycle();


        setOrientation(LinearLayout.VERTICAL);


        setVisibility(GONE);
    }

    private void findViews() {
        mTv = (TextView) findViewById(R.id.expandable_text);

        mButton = (TextView) findViewById(R.id.expand_collapse);
        mButton.setText(mCollapsed ? mExpandText : mCollapseText);
        mButton.setOnClickListener(this);
    }

    private static boolean isPostHoneycomb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static void applyAlphaAnimation(View view, float alpha) {
        if (isPostHoneycomb()) {
            view.setAlpha(alpha);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(alpha, alpha);

            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            view.startAnimation(alphaAnimation);
        }
    }

    private static int getRealTextViewHeight(@NonNull TextView textView) {
        int textHeight = textView.getLayout().getLineTop(textView.getLineCount());
        int padding = textView.getCompoundPaddingTop() + textView.getCompoundPaddingBottom();
        return textHeight + padding;
    }

    class ExpandCollapseAnimation extends Animation {
        private final View mTargetView;
        private final int mStartHeight;
        private final int mEndHeight;

        public ExpandCollapseAnimation(View view, int startHeight, int endHeight) {
            mTargetView = view;
            mStartHeight = startHeight;
            mEndHeight = endHeight;
            setDuration(mAnimationDuration);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            final int newHeight = (int) ((mEndHeight - mStartHeight) * interpolatedTime + mStartHeight);
            mTv.setMaxHeight(newHeight - mMarginBetweenTxtAndBottom);

            if (Float.compare(mAnimAlphaStart, 1.0f) != 0) {
                applyAlphaAnimation(mTv, mAnimAlphaStart + interpolatedTime * (1.0f - mAnimAlphaStart));
            }

            mTargetView.getLayoutParams().height = newHeight;
            mTargetView.requestLayout();
        }

        @Override
        public void initialize(int width, int height, int parentWidth, int parentHeight) {
            super.initialize(width, height, parentWidth, parentHeight);
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    public interface OnExpandStateChangeListener {

        void onExpandStateChanged(TextView textView, boolean isExpanded);
    }
}