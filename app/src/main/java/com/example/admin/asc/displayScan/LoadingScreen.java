package com.example.admin.asc.displayScan;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.admin.asc.R;
import com.example.admin.asc.displayPermissions.DisplayClass;
import com.google.android.gms.safetynet.HarmfulAppsData;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Admin on 3/10/2018.
 */

public class LoadingScreen extends Activity implements AsyncResponse {
    int total;
    private Map<String, ApplicationInfo> appMap = new HashMap<>();
    private boolean ready = false;
    private HashMap<String, ApplicationInfo> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingscreen_layout);
        enable();
        checkEnabled();
        setStatus();
        checkPackages();

    }

    @Override
    public void processFinish(Map<String, ApplicationInfo> returnedMap) {
        map = (HashMap<String, ApplicationInfo>) returnedMap;
        ready = true;
        getGoogleAPI();

    }

    private void checkPackages() {
        BackGroundTasks backGroundTasks = new BackGroundTasks(this);
        final PackageManager pm = getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        //String tv;
        total = packages.size();
        for (ApplicationInfo pack : packages) {
            PackageInfo info = null;
            if ((pack.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) == 1) {
            } else if ((pack.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {

            } else {
                try {
                    info = getPackageManager().getPackageInfo(
                            pack.packageName, PackageManager.GET_SIGNATURES);

                    StringBuilder sb = new StringBuilder();
                    for (android.content.pm.Signature signature : info.signatures) {

                        MessageDigest md;
                        md = MessageDigest.getInstance("MD5");
                        md.update(signature.toByteArray());
                        byte[] digestByteArray = md.digest();

                        sb = new StringBuilder();
                        for (byte b : digestByteArray) {
                            sb.append(String.format("%02x", b & 0xff));
                        }
                        appMap.put(sb.toString(), pack);
                    }


                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }

        }
        backGroundTasks.execute(appMap);
    }

    public void setStatus() {
        TextView txt = findViewById(R.id.statusView);
        txt.setText("Scanning");

    }


    private void enable() {
        SafetyNet.getClient(this)
                .enableVerifyApps()
                .addOnCompleteListener(new OnCompleteListener<com.google.android.gms.safetynet.SafetyNetApi.VerifyAppsUserResponse>() {
                    @Override
                    public void onComplete(Task<com.google.android.gms.safetynet.SafetyNetApi.VerifyAppsUserResponse> task) {
                        if (task.isSuccessful()) {
                            com.google.android.gms.safetynet.SafetyNetApi.VerifyAppsUserResponse result = task.getResult();
                            if (result.isVerifyAppsEnabled()) {
                                Log.d("MY_APP_TAG", "The user gave consent " +
                                        "to enable the Verify Apps feature.");
                            } else {
                                Log.d("MY_APP_TAG", "The user didn't give consent " +
                                        "to enable the Verify Apps feature.");
                                checkEnabled();
                            }
                        } else {
                            Log.e("MY_APP_TAG", "A general error occurred.");

                        }
                    }
                });
    }

    private void checkEnabled() {
        SafetyNet.getClient(this)
                .isVerifyAppsEnabled()
                .addOnCompleteListener(new OnCompleteListener<com.google.android.gms.safetynet.SafetyNetApi.VerifyAppsUserResponse>() {
                    @Override
                    public void onComplete(Task<com.google.android.gms.safetynet.SafetyNetApi.VerifyAppsUserResponse> task) {
                        if (task.isSuccessful()) {
                            com.google.android.gms.safetynet.SafetyNetApi.VerifyAppsUserResponse result = task.getResult();
                            if (result.isVerifyAppsEnabled()) {
                                Log.d("MY_APP_TAG", "The Verify Apps feature is enabled.");

                            } else {
                                Log.d("MY_APP_TAG", "The Verify Apps feature is disabled.");
                                enable();
                                checkEnabled();

                            }
                        } else {
                            Log.e("MY_APP_TAG", "A general error occurred.");
                        }
                    }
                });
    }

    private void getGoogleAPI() {
        setContentView(R.layout.display_layout);
        SafetyNet.getClient(this)
                .listHarmfulApps()
                .addOnCompleteListener(new OnCompleteListener<SafetyNetApi.HarmfulAppsResponse>() {
                    @Override
                    public void onComplete(Task<SafetyNetApi.HarmfulAppsResponse> task) {

                        Log.d(TAG, "Received listHarmfulApps() result");
                        if (task.isSuccessful()) {
                            SafetyNetApi.HarmfulAppsResponse result = task.getResult();

                            List<HarmfulAppsData> appList = result.getHarmfulAppsList();
                            HashMap<String, HarmfulAppsData> harmfulAppsDataHashMap = new HashMap<>();

                            for (HarmfulAppsData harmfulAppsData : appList) {
                                harmfulAppsDataHashMap.put(harmfulAppsData.toString(), harmfulAppsData);
                            }

                            Intent loadingIntent = new Intent(LoadingScreen.this, DisplayClass.class);
                            loadingIntent.putExtra("total", total);
                            loadingIntent.putExtra("map", map);
                            loadingIntent.putExtra("harmfulAppsData", harmfulAppsDataHashMap);
                            startActivity(loadingIntent);
                            finish();


                        } else {
                            Log.d("MY_APP_TAG", "An error occurred. " +
                                    "Call isVerifyAppsEnabled() to ensure " +
                                    "that the user has consented.");
                        }
                    }
                });
    }

}

