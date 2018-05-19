package com.example.admin.asc;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.admin.asc.displayPermissions.ViewAppsPermissions;
import com.example.admin.asc.displayScan.LoadingScreen;


public class MainActivity extends Activity {
    private static final int PERMS_REQUEST_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onButtonClick(View view) {
        String button_text;
        button_text = ((Button) view).getText().toString();
        if (hasPermissions()) {
            if (button_text.equals("Start Scan")) {
                Intent intent = new Intent(this, LoadingScreen.class);
                startActivity(intent);
            }
            if (button_text.equals("Check apps permissions")) {
                Intent intent = new Intent(this, ViewAppsPermissions.class);
                startActivity(intent);

            }
            if (button_text.equals("Exit")) {
                Intent a = new Intent(Intent.ACTION_MAIN);
                a.addCategory(Intent.CATEGORY_HOME);
                a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(a);
                finish();
            }
        } else {
            //app doesn't have permissions, so requesting permissions.
            requestPerms();
        }
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;

        switch (requestCode) {
            case PERMS_REQUEST_CODE:

                for (int res : grantResults) {
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }
                break;
            default:
                // if user not granted permissions.
                allowed = false;
                break;
        }
        if (!allowed) {
            // give warning to user that they haven't granted permissions.
            if (shouldShowRequestPermissionRationale(Manifest.permission.INTERNET)) {
                Toast.makeText(this, "Internet Permissions denied.", Toast.LENGTH_SHORT).show();
            }
            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_NETWORK_STATE)) {
                Toast.makeText(this, "Internet Permissions denied.", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private void requestPerms() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        requestPermissions(permissions, PERMS_REQUEST_CODE);

    }

    @SuppressLint("WrongConstant")
    private boolean hasPermissions() {
        int res = 0;
        //string array of permissions,
        String[] permissions = new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE};

        for (String perms : permissions) {
            res = checkCallingOrSelfPermission(perms);
            if (!(res == PackageManager.PERMISSION_GRANTED)) {
                return false;
            }
        }
        return true;
    }


}

