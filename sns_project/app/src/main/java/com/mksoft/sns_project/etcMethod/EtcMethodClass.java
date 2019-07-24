package com.mksoft.sns_project.etcMethod;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class EtcMethodClass {

    public HideKeyboard hideKeyboard;
    public BottomLineButton bottomLineButton;
    public PermissionMethod permissionMethod;
    public EtcMethodClass(Activity pageActivity, RelativeLayout pageLayout){
        this.permissionMethod = new PermissionMethod(pageActivity);
        this.hideKeyboard = new HideKeyboard(pageActivity, pageLayout);
        this.bottomLineButton = new BottomLineButton();
    }





}
