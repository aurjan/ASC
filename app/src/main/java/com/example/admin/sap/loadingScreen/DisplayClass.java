package com.example.admin.sap.loadingScreen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.pm.ApplicationInfo;
import android.text.method.ScrollingMovementMethod;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.sap.R;
import com.google.android.gms.safetynet.HarmfulAppsData;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Admin on 12/9/2017.
 */

public class DisplayClass extends Activity implements AdapterView.OnItemSelectedListener{
    private boolean completed= false;
    boolean loaded = false;

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }


    private void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);

        SafetyNet.getClient(this)
                .listHarmfulApps()
                .addOnCompleteListener(new OnCompleteListener<SafetyNetApi.HarmfulAppsResponse>() {
                    @Override
                    public void onComplete(Task<SafetyNetApi.HarmfulAppsResponse> task) {
                        if (completed){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else {
                            Log.d(TAG, "Received listHarmfulApps() result");

                            if (task.isSuccessful()) {
                                com.google.android.gms.safetynet.SafetyNetApi.HarmfulAppsResponse result = task.getResult();
                                long scanTimeMs = result.getLastScanTimeMs();

                                List<HarmfulAppsData> appList = result.getHarmfulAppsList();
                                loadSpinner(appList);
                                setCompleted(true);
                                if (appList.isEmpty()) {
                                    Log.d("MY_APP_TAG", "There are no known " +
                                            "potentially harmful apps installed.");
                                } else {
                                    Log.e("MY_APP_TAG",
                                            "Potentially harmful apps are installed!");

                                    for (HarmfulAppsData harmfulApp : appList) {
                                        Log.e("MY_APP_TAG", "Information about a harmful app:");
                                        Log.e("MY_APP_TAG",
                                                "  APK: " + harmfulApp.apkPackageName);
                                        Log.e("MY_APP_TAG",
                                                "  SHA-256: " + harmfulApp.apkSha256);

                                        // Categories are defined in VerifyAppsConstants.
                                        Log.e("MY_APP_TAG",
                                                "  Category: " + harmfulApp.apkCategory);
                                    }
                                }
                            } else {
                                Log.d("MY_APP_TAG", "An error occurred. " +
                                        "Call isVerifyAppsEnabled() to ensure " +
                                        "that the user has consented.");
                            }
                        }


                    }
                });


        //String tv;
        TextView tv = findViewById(R.id.textView3);
        tv.setMovementMethod( new ScrollingMovementMethod());


        setLoaded(true);


    }
    private void loadSpinner(List<HarmfulAppsData> appList){


        ArrayList<String> progArray = new ArrayList<>();
        try {
            String len = "" +appList.size();
            Log.e("spinner", len);
            for (HarmfulAppsData harmfulApp : appList) {
                Log.e("testing", "Information about a harmful app:");
                Log.e("testing",
                        "  APK: " + harmfulApp.apkPackageName);
                Log.e("testing",
                        "  SHA-256: " + harmfulApp.apkSha256);

                // Categories are defined in VerifyAppsConstants.
                Log.e("testing",
                        "  Category: " + harmfulApp.apkCategory);
            }

            Spinner spinner = findViewById(R.id.spinner);
            for (HarmfulAppsData packageInfo : appList) {
                //Log.d(TAG, "Installed package :" + packageInfo.packageName);
                //Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
                String stringName = ("Package : " + packageInfo.apkPackageName);
                progArray.add(stringName);
                //Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, progArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spinner.setAdapter(adapter);
            spinner.setOnItemSelectedListener(this);
        }catch (Exception e){
            Log.e("spinner",e.getMessage());
        }


    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        Spinner mySpinner=(Spinner) findViewById(R.id.spinner);
        TextView tv = findViewById(R.id.selectedItem);
        String text = mySpinner.getSelectedItem().toString();
        //tv.append("Package : \n" + packageInfo.packageName+'\n');
        //tv.append("Dir : \n" + packageInfo.sourceDir+'\n');
        //File file = new File(packageInfo.sourceDir);
        //String md5 = MD5.calculateMD5(file);
        //String sha1 = SHA1.calculateSHA1(file);
        //tv.setText("MD5 : \n" +md5 +"\n " );
        //tv.append("SHA1 : \n" +sha1 +"\n \n" );

        tv.setText(text);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    protected void onStart() {
        super.onStart();

    }

}
