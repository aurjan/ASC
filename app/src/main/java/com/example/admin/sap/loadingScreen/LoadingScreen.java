package com.example.admin.sap.loadingScreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.example.admin.sap.MainActivity;
import com.example.admin.sap.R;
import java.util.List;

import com.example.admin.sap.SplashScreen;
import com.google.android.gms.safetynet.HarmfulAppsData;
import com.google.android.gms.safetynet.SafetyNet;
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

public class LoadingScreen extends Activity {
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    private boolean isEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loadingscreen_layout);
        enable();
        new Handler().postDelayed(new Runnable() {

        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(LoadingScreen.this, DisplayClass.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, 2000);

    }

    public void test() {
        TextView txt = findViewById(R.id.statusView);
        txt.setText("Scanning");

        txt.setText("Done");

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
                                setEnabled(true);

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
}



    //
//    public void startLoadingScreen(){
//        try{
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    new ScanningOperation().execute("Scanning");
//                    //loadingScreen.setStatus("Scanning");
//                    final PackageManager pm = getPackageManager();
//                    //get a list of installed apps.
//                    List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//                    //changeIntent();
//                    //finish();
//
//                }
//            });
//        } catch (Exception e){
//            Log.e("Thread","Thread");
//        }
//
//    }
//
//    private class ScanningOperation extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            return params[0];
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            TextView txt = findViewById(R.id.statusView);
//            txt.setText(result);
//
//        }
//
//        @Override
//        protected void onPreExecute() {}
//
//        @Override
//        protected void onProgressUpdate(Void... values) {}
//    }
