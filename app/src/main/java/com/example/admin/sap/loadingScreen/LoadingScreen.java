package com.example.admin.sap.loadingScreen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.admin.sap.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.admin.sap.displayScreens.DisplayClass;
import com.google.android.gms.safetynet.HarmfulAppsData;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.content.ContentValues.TAG;

/**
 * Created by Admin on 3/10/2018.
 */

//class Scanner extends Activity implements Runnable {
//
//
//    @Override
//    public void run() {
//        //final PackageManager pm = getPackageManager();
//        //get a list of installed apps.
//        //List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//
//
//    }

public class LoadingScreen extends Activity implements  AsyncResponse{
    private Map<String,ApplicationInfo> appMap = new HashMap<>();
    int total;
    private HashMap <String, ApplicationInfo> map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingscreen_layout);
        enable();
        test();
        checkPackages();



    }

    @Override
    public void processFinish(Map <String,ApplicationInfo> returnedMap) {
        System.out.println("size of map: " + returnedMap.size());
        map = (HashMap <String, ApplicationInfo>) returnedMap;


//        startActivity(loadingIntent);
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
                    appMap.put(sb.toString(),pack);
                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            String method = "MD5";



        }
        backGroundTasks.execute(appMap);
    }

    public void test() {
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

//                                TextView tv = findViewById(R.id.textView3);
//                                tv.setText("List of threats");
//                                tv.setMovementMethod(new ScrollingMovementMethod());
                            List<HarmfulAppsData> appList = result.getHarmfulAppsList();
                            HashMap <String,HarmfulAppsData> harmfulAppsDataHashMap = new HashMap<>();

                            for (HarmfulAppsData harmfulAppsData: appList){
                                harmfulAppsDataHashMap.put(harmfulAppsData.toString(),harmfulAppsData);
                            }
                            Intent loadingIntent = new Intent(LoadingScreen.this, DisplayClass.class);
                            loadingIntent.putExtra("total",total);
                            loadingIntent.putExtra("map", map);
                            loadingIntent.putExtra("harmfulAppsData", harmfulAppsDataHashMap);
                            startActivity(loadingIntent);
//
//                            if (appList.isEmpty()) {
//                                Log.d("MY_APP_TAG", "There are no known " +
//                                        "potentially harmful apps installed.");
//                            } else {
//                                Log.e("MY_APP_TAG",
//                                        "Potentially harmful apps are installed!");
//
//                                for (HarmfulAppsData harmfulApp : appList) {
//                                    Log.e("MY_APP_TAG", "Information about a harmful app:");
//                                    Log.e("MY_APP_TAG",
//                                            "  APK: " + harmfulApp.apkPackageName);
//                                    Log.e("MY_APP_TAG",
//                                            "  SHA-256: " + harmfulApp.apkSha256);
//
//                                    // Categories are defined in VerifyAppsConstants.
//                                    Log.e("MY_APP_TAG",
//                                            "  Category: " + harmfulApp.apkCategory);
//                                }
//                            }
                        } else {
                            Log.d("MY_APP_TAG", "An error occurred. " +
                                    "Call isVerifyAppsEnabled() to ensure " +
                                    "that the user has consented.");
                        }
                    }
                });
    }

}

