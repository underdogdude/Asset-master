package xyz.codecool.android.asset.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;

public class CardViewBehavior extends CoordinatorLayout.Behavior<CardView> {
    private int toolbarHeight;

    public CardViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.toolbarHeight = 150;
    }
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, CardView fab, View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, CardView fab, View dependency) {
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = (float)dependency.getY()/(float)toolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}
