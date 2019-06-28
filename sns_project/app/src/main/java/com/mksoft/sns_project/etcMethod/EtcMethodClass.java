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

    HideKeyboard hideKeyboard;
    RelativeLayout pageLayout;
    Activity pageActivity;
    private Boolean isPermission = true;

    public EtcMethodClass(Activity pageActivity, RelativeLayout pageLayout){
        this.pageActivity = pageActivity;
        hideKeyboard = new HideKeyboard(pageActivity);
        this.pageLayout = pageLayout;
    }

    //키보드 숨기기
    public void clickHideKeyboard(){
        pageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getHideKeyboard().hideKeyboard();
            }
        });
    }

    public HideKeyboard getHideKeyboard(){
        return hideKeyboard;
    }
    //권한 요청

    public void tedPermission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공
                isPermission = true;

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
                isPermission = false;

            }
        };

        TedPermission.with(pageActivity)
                .setPermissionListener(permissionListener)
                .setRationaleMessage("카메라 권한, 쓰기 권한 필요해요.")
                .setDeniedMessage("권한 거부하셨습니다. 설정에가서 변경해주세요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    public Boolean getPermission() {
        return isPermission;
    }
}
