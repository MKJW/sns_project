package com.mksoft.sns_project.etcMethod;

import android.Manifest;
import android.app.Activity;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

public class PermissionMethod {
    private Boolean isPermission = true;
    Activity pageActivity;

    public PermissionMethod(Activity pageActivity) {
        this.pageActivity = pageActivity;
    }
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
