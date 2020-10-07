package com.example.ecobeauty;

import android.content.Context;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import android.util.AttributeSet;
import android.view.View;

public class ScrollingFABAnimation extends
		CoordinatorLayout.Behavior<FloatingActionButton> {

	public ScrollingFABAnimation(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	//For SnackBar to push up the Floating Button when it pops up
	@Override
	public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
		return dependency instanceof Snackbar.SnackbarLayout;
	}

	@Override
	public boolean onDependentViewChanged(CoordinatorLayout parent,	FloatingActionButton child, View dependency) {
		float translationY = Math.min(0, dependency.getTranslationY() - dependency.getHeight());
		child.setTranslationY(translationY);
		return true;
	}

	//For FAB to hide or show on scroll
	@Override
	public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,	FloatingActionButton child, View directTargetChild, View target,
			int nestedScrollAxes) {
		return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL	|| super.onStartNestedScroll(coordinatorLayout, child,
						directTargetChild, target, nestedScrollAxes);
	}

	@Override
	public void onNestedScroll(CoordinatorLayout coordinatorLayout,	FloatingActionButton child, View target, int dxConsumed,
			int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
		super.onNestedScroll(coordinatorLayout, child, target, dxConsumed,	dyConsumed, dxUnconsumed, dyUnconsumed);

		if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
			child.hide();
		} else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
			child.show();
		}
	}
}