package com.mksoft.sns_project.etcMethod;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

public class HideKeyboard {
    Activity activity;
    InputMethodManager imm;
    RelativeLayout pageLayout;

    public HideKeyboard(Activity activity, RelativeLayout pageLayout) {
        this.activity = activity;
        this.pageLayout = pageLayout;
        imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    }

    public void hideKeyboard() {
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    //키보드 숨기기
    public void clickHideKeyboard(){
        pageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
            }
        });
    }


}
