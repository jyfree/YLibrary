package com.jy.baselibrary.acp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import com.jy.baselibrary.utils.YLogUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;


/**
 * @Author Administrator
 * @Date 2019/10/12-13:45
 * @TODO
 */
public class AcpManager {

    private final String TAG = "AcpManager";

    private final int REQUEST_CODE_PERMISSION = 0x38;
    private final int REQUEST_CODE_SETTING = 0x39;

    private AcpListener permissionListener;//授权回调
    private AcpOptions permissionOptions;//授权配置信息
    private boolean isShowRational = true;

    public AcpManager setAcPermissionListener(AcpListener permissionListener) {
        this.permissionListener = permissionListener;
        return this;
    }

    public AcpManager setAcPermissionOptions(AcpOptions permissionOptions) {
        this.permissionOptions = permissionOptions;
        return this;
    }

    public AcpManager setShowRational(boolean showRational) {
        isShowRational = showRational;
        return this;
    }

    public void request(Context context) {
        if (AcpConstant.manifestPermissionSet.isEmpty()) {
            getManifestPermissions(context);
        }
        checkSelfPermission(context, null);
    }


    private void getManifestPermissions(Context context) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_PERMISSIONS);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo != null) {
            String[] permissions = packageInfo.requestedPermissions;
            if (permissions != null) {
                Collections.addAll(AcpConstant.manifestPermissionSet, permissions);
            }
        }
    }

    /**
     * 检查权限
     */
    private void checkSelfPermission(Context context, Activity acpActivity) {
        if (permissionOptions == null) {
            throw new IllegalArgumentException("AcpOptions is null...");
        }
        AcpConstant.deniedPermissionList.clear();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            YLogUtils.INSTANCE.iTag(TAG, "Build.VERSION.SDK_INT < Build.VERSION_CODES.M");
            if (permissionListener != null) {
                permissionListener.onGranted();
            }
            onDestroy(acpActivity);
            return;
        }
        String[] permissions = permissionOptions.getPermissions();
        for (String permission : permissions) {
            //检查申请的权限是否在 AndroidManifest.xml 中
            if (AcpConstant.manifestPermissionSet.contains(permission)) {
                int checkSelfPermission = AcpService.checkSelfPermission(context, permission);
                YLogUtils.INSTANCE.iTag(TAG, "checkSelfPermission", checkSelfPermission);
                //如果是拒绝状态则装入拒绝集合中
                if (checkSelfPermission == PackageManager.PERMISSION_DENIED) {
                    AcpConstant.deniedPermissionList.add(permission);
                }
            }
        }
        //检查如果没有一个拒绝响应 onGranted 回调
        if (AcpConstant.deniedPermissionList.isEmpty()) {
            YLogUtils.INSTANCE.iTag(TAG, "deniedPermissionList.isEmpty()");
            if (permissionListener != null) {
                permissionListener.onGranted();
            }
            onDestroy(acpActivity);
            return;
        }
        startAcpActivity(context);
    }

    /**
     * 检查权限是否存在拒绝不再提示
     *
     * @param activity
     */
    public void checkRequestPermissionRationale(Activity activity) {
        boolean rationale = false;
        //如果有拒绝则提示申请理由提示框，否则直接向系统请求权限
        for (String permission : AcpConstant.deniedPermissionList) {
            rationale = rationale || AcpService.shouldShowRequestPermissionRationale(activity, permission);
        }
        YLogUtils.INSTANCE.iTag(TAG, "rationale", rationale);
        String[] permissions = AcpConstant.deniedPermissionList.toArray(new String[]{});
        if (rationale && isShowRational) {
            showRationalDialog(activity, permissions);
        } else {
            requestPermissions(activity, permissions);
        }
    }


    /**
     * 响应向系统请求权限结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(Activity activity, int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                LinkedList<String> grantedPermissions = new LinkedList<>();
                LinkedList<String> deniedPermissions = new LinkedList<>();
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        grantedPermissions.add(permission);
                    } else {
                        deniedPermissions.add(permission);
                    }
                }
                //全部允许才回调 onGranted 否则只要有一个拒绝都回调 onDenied
                if (!grantedPermissions.isEmpty() && deniedPermissions.isEmpty()) {
                    if (permissionListener != null) {
                        permissionListener.onGranted();
                    }
                    onDestroy(activity);
                } else if (!deniedPermissions.isEmpty()) {
                    showDeniedDialog(activity, deniedPermissions);
                }
                break;
        }
    }

    /**
     * 响应设置权限返回结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        if (permissionListener == null || permissionOptions == null || requestCode != REQUEST_CODE_SETTING) {
            onDestroy(activity);
            return;
        }
        checkSelfPermission(activity, activity);
    }


    /**
     * 申请理由对话框
     *
     * @param permissions
     */
    private void showRationalDialog(final Activity activity, final String[] permissions) {
        if (permissionOptions == null) {
            throw new IllegalArgumentException("AcpOptions is null...");
        }
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setMessage(permissionOptions.getRationalMessage())
                .setPositiveButton(permissionOptions.getRationalBtn(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermissions(activity, permissions);
                    }
                }).create();
        alertDialog.setCancelable(permissionOptions.isDialogCancelable());
        alertDialog.setCanceledOnTouchOutside(permissionOptions.isDialogCanceledOnTouchOutside());
        alertDialog.show();
    }


    /**
     * 拒绝权限提示框
     *
     * @param permissions
     */
    private void showDeniedDialog(final Activity activity, final List<String> permissions) {
        if (permissionOptions == null) {
            throw new IllegalArgumentException("AcpOptions is null...");
        }
        if (!permissionOptions.isCanShowDeniedDialog()) {
            onDestroy(activity);
            return;
        }
        AlertDialog alertDialog = new AlertDialog.Builder(activity)
                .setMessage(permissionOptions.getDeniedMessage())
                .setNegativeButton(permissionOptions.getDeniedCloseBtn(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (permissionListener != null) {
                            permissionListener.onDenied(permissions);
                        }
                        onDestroy(activity);
                    }
                })
                .setPositiveButton(permissionOptions.getDeniedSettingBtn(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startSetting(activity);
                    }
                }).create();
        alertDialog.setCancelable(permissionOptions.isDialogCancelable());
        alertDialog.setCanceledOnTouchOutside(permissionOptions.isDialogCanceledOnTouchOutside());
        alertDialog.show();
    }


    /**
     * 向系统请求权限
     *
     * @param permissions
     */
    private void requestPermissions(Activity activity, String[] permissions) {
        AcpService.requestPermissions(activity, permissions, REQUEST_CODE_PERMISSION);
    }


    /**
     * 启动处理权限过程的 Activity
     */
    private void startAcpActivity(Context context) {
        Intent intent = new Intent(context, AcpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到设置界面
     */
    private void startSetting(Activity activity) {
        if (MiuiOs.isMIUI()) {
            Intent intent = MiuiOs.getSettingIntent(activity);
            if (MiuiOs.isIntentAvailable(activity, intent)) {
                activity.startActivityForResult(intent, REQUEST_CODE_SETTING);
                return;
            }
        }
        try {
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            intent.setData(Uri.parse("package:" + activity.getPackageName()));
            activity.startActivityForResult(intent, REQUEST_CODE_SETTING);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                activity.startActivityForResult(intent, REQUEST_CODE_SETTING);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }

    }

    /**
     * 摧毁本库的 AcpActivity
     */
    private void onDestroy(Activity activity) {
        permissionListener = null;
        if (activity != null) {
            activity.finish();
        }
    }
}
