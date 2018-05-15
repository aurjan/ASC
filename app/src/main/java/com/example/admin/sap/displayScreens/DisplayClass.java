package com.example.admin.sap.displayScreens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.admin.sap.MainActivity;
import com.example.admin.sap.R;
import com.google.android.gms.safetynet.HarmfulAppsData;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Admin on 12/9/2017.
 */

public class DisplayClass extends Activity implements AdapterView.OnItemSelectedListener {
    private double total;
    private double notfound;
    private HashMap <String,ApplicationInfo> appMap = new HashMap<>();

    ArrayList<String> progArray = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.display_layout);
        if (!haveNetworkConnection()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Scan cannot be done without internet connection").setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intent = new Intent(DisplayClass.this,MainActivity.class);
                            finish();
                            startActivity(intent);
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


        }
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        total = intent.getIntExtra("total",0);
        appMap = (HashMap<String,ApplicationInfo>) intent.getSerializableExtra("map");
        loadSpinner((HashMap<String,HarmfulAppsData>) intent.getSerializableExtra("harmfulAppsData"));


    }

    private void loadSpinner() {
        for (String md5:appMap.keySet()){
            progArray.add(appMap.get(md5).packageName);
            System.out.println(appMap.get(md5).packageName);
        }
        notfound = appMap.size();

        Spinner spinner = findViewById(R.id.spinner);
        if (progArray.isEmpty()){
            progArray.add("No threats found");
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, progArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }


    private void loadSpinner(HashMap<String,HarmfulAppsData> appList) {
        try {
            String len = "" + appList.size();
            Log.e("spinner", len);
            for (String string : appList.keySet()) {
                Log.e("testing", "Information about a harmful app:");
                Log.e("testing",
                        "  APK: " + appList.get(string).apkPackageName);
                Log.e("testing",
                        "  Category: " + appList.get(string).apkCategory);
            }

            if (appList.size() > 0) {
                for (String string : appList.keySet()) {
                    String stringName = ("Package : " + appList.get(string).apkPackageName);
                    progArray.add(stringName);
                }
            }


        } catch (Exception e) {
            Log.e("spinner", e.getMessage());
        }
        TextView tv = findViewById(R.id.selectedItem);
        tv.setText(total + "/" + notfound + " - dangerous " + appList.size());
        loadSpinner();


    }

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        try {
            Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
            String text = mySpinner.getSelectedItem().toString();
            PackageInfo info = getPackageManager().getPackageInfo(
                    text, PackageManager.GET_SIGNATURES);
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
            }


        } catch (PackageManager.NameNotFoundException e1) {
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }



}
