package com.example.ecobeauty.mydeparture;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.IdRes;
import androidx.annotation.RequiresApi;

import com.example.ecobeauty.R;
import com.example.ecobeauty.main.Constants;

import java.util.HashMap;

public class PresetRadioGroup extends LinearLayout {

    private int mCheckedId = View.NO_ID, id;
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private HashMap<Integer, View> mChildViewsMap = new HashMap<>();
    private PassThroughHierarchyChangeListener mPassThroughListener;
    private RadioCheckable.OnCheckedChangeListener mChildOnCheckedChangeListener;
    private  TypedArray obtainStyledAttributes;
    private View checkedView;;

    public PresetRadioGroup(Context context) {
        super(context);
        setupView();
    }

    public PresetRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    public PresetRadioGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(attrs);
        setupView();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PresetRadioGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        parseAttributes(attrs);
        setupView();
    }

    private void parseAttributes(AttributeSet attrs) {
        obtainStyledAttributes = getContext().obtainStyledAttributes(attrs,
                R.styleable.PresetRadioGroup, 0, 0);
        try {
            mCheckedId = obtainStyledAttributes.getResourceId(R.styleable.PresetRadioGroup_presetRadioCheckedId, View.NO_ID);

        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    private void setupView() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof RadioCheckable) {
            final RadioCheckable button = (RadioCheckable) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != View.NO_ID) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(child.getId(), true);
            }
        }

        super.addView(child, index, params);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (mCheckedId != View.NO_ID) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId, true);
        }
    }

    private void setCheckedId(@IdRes int id, boolean isChecked) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mChildViewsMap.get(id), isChecked, mCheckedId);
        }
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    public void clearCheck() {
        check(View.NO_ID);
    }

    public void check(@IdRes int id) {
        if (id != View.NO_ID && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != View.NO_ID) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != View.NO_ID) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id, true);
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        checkedView = mChildViewsMap.get(viewId);
        if (checkedView == null) {
            checkedView = findViewById(viewId);
            if (checkedView != null) {
                mChildViewsMap.put(viewId, checkedView);
            }
        }
        if (checkedView != null && checkedView instanceof RadioCheckable) {
            ((RadioCheckable) checkedView).setChecked(checked);
        }
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        mOnCheckedChangeListener = onCheckedChangeListener;
    }

    public OnCheckedChangeListener getOnCheckedChangeListener() {
        return mOnCheckedChangeListener;
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(View radioGroup, View radioButton, boolean isChecked, int checkedId);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int w, int h) {
            super(w, h);
        }

        public LayoutParams(int w, int h, float initWeight) {
            super(w, h, initWeight);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        @Override
        protected void setBaseAttributes(TypedArray a,
                                         int widthAttr, int heightAttr) {

            if (a.hasValue(widthAttr)) {
                width = a.getLayoutDimension(widthAttr, Constants.LAYOUT_WIDTH);
            } else {
                width = WRAP_CONTENT;
            }

            if (a.hasValue(heightAttr)) {
                height = a.getLayoutDimension(heightAttr, Constants.LAYOUT_HEIGHT);
            } else {
                height = WRAP_CONTENT;
            }
        }
    }

    private class CheckedStateTracker implements RadioCheckable.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(PresetValueButton radioGroup, boolean isChecked) {
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != View.NO_ID) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            id = radioGroup.getId();
            setCheckedId(id, true);
        }
    }

    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        public void onChildViewAdded(View parent, View child) {
            if (parent == PresetRadioGroup.this && child instanceof RadioCheckable) {
                id = child.getId();
                if (id == View.NO_ID) {
                    id = ViewUtils.generateViewId();
                    child.setId(id);
                }
                ((RadioCheckable) child).addOnCheckChangeListener(
                        mChildOnCheckedChangeListener);
                mChildViewsMap.put(id, child);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

         public void onChildViewRemoved(View parent, View child) {
            if (parent == PresetRadioGroup.this && child instanceof RadioCheckable) {
                ((RadioCheckable) child).removeOnCheckChangeListener(mChildOnCheckedChangeListener);
            }
            mChildViewsMap.remove(child.getId());
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
