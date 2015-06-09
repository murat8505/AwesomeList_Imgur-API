package com.frankegan.awesomelist;

import android.graphics.Color;

/**
 * @author frankegan created on 6/6/15.
 */
public interface OnAppBarChangeListener {
    int VISIBLE = 1;
    int GONE = 0;
    void onVisibilityChanged(int visibility);
    void onAppBarScrollOut();
    void onAppBarScrollIn();
}
