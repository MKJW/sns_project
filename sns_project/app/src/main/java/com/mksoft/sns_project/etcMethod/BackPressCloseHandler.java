package com.mksoft.sns_project.etcMethod;

import android.app.Activity;
import android.widget.Toast;

public class BackPressCloseHandler {

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        activity.finish();

    }

}
