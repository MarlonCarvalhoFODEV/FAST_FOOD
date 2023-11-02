package com.c.hotburguerdelivery.zutils;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class CustomItemDecor extends RecyclerView.ItemDecoration {
    private int halfSpace;

    public CustomItemDecor(int spanPadding) {
        this.halfSpace = spanPadding;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

        if (parent.getPaddingLeft() != halfSpace) {
            parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
            parent.setClipToPadding(false);
        }

        outRect.top = halfSpace;
        outRect.bottom = halfSpace;
        outRect.left = halfSpace;
        outRect.right = halfSpace;

    }
}
