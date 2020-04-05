package com.example.mukit.homebird_v1.model;

import com.example.mukit.homebird_v1.R;

/**
 * Created by Boss on 12/17/2016.
 */

public enum ModelObject {

    PINK(R.color.pink_color, R.layout.left),
    DEEP_ORANGE(R.color.deep_orange_color, R.layout.mid),
    ORANGE(R.color.orange_color, R.layout.right);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}
