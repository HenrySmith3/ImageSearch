package com.fieldwire.ImageSearch;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class ImagesLayout extends LinearLayout {
    int currentIndex = 0;
    public ImagesLayout(Context context) {
        super(context);
    }
    public void clearViews() {
        currentIndex = 0;
        for (int i = 0; i < getChildCount(); i++) {
            //removing the view and adding it later is more expensive than just not showing it for now.
            ((ImageView)getChildAt(i)).setImageDrawable(null);
        }
    }
    public void addImage(ImageView view) {
        //if we haven't had this many images before, we need to just make a new child.
        if (getChildCount() <= currentIndex) {
            addView(view);
        }
        //otherwise, just reuse a previous child.
        Drawable drawable = view.getDrawable();
        ((ImageView)getChildAt(currentIndex)).setImageBitmap(((BitmapDrawable)drawable).getBitmap());
        currentIndex++;
    }
}
