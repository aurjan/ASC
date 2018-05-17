package com.example.admin.sap.displayScreens;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.pm.ApplicationInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.admin.sap.MainActivity;
import com.example.admin.sap.R;
import com.example.admin.sap.loadingScreen.BackgroudReport;
import com.example.admin.sap.loadingScreen.LoadingScreen;
import com.google.android.gms.safetynet.HarmfulAppsData;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Admin on 12/9/2017.
 */

public class DisplayClass extends Activity implements AdapterView.OnItemSelectedListener {
    private int total;
    private int notfound;
    private int dangerous;
    private HashMap <String,ApplicationInfo> appMap = new HashMap<>();
    protected Context context;
    ArrayList<String> progArray = new ArrayList<>();
    ArrayList<String> progArrayDangerous = new ArrayList<>();
    private static final String dangerousDefault = "No dangerous apps found";
    private static final String spinnerDefault = "No threats found";
    private String appSelected;

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
        context = this;

        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        total = intent.getIntExtra("total",0);
        appMap = (HashMap<String,ApplicationInfo>) intent.getSerializableExtra("map");
        notfound = appMap.size();
        loadSpinner((HashMap<String,HarmfulAppsData>) intent.getSerializableExtra("harmfulAppsData"));



    }

    private void loadSpinner() {
        for (String md5:appMap.keySet()){
            progArray.add(appMap.get(md5).packageName);
            System.out.println(appMap.get(md5).packageName);
        }


        Spinner spinner = findViewById(R.id.spinner);
        if (progArray.isEmpty()){
            progArray.add(spinnerDefault);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, progArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
                    progArrayDangerous.add(stringName);
                }
            }


        } catch (Exception e) {
            Log.e("spinner", e.getMessage());
        }
        Spinner spinner = findViewById(R.id.spinner2);
        if (progArrayDangerous.isEmpty()){
            progArrayDangerous.add(dangerousDefault);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, progArrayDangerous);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        dangerous = appList.size();

        List <String> list = new ArrayList<>();
        list.add(android.os.Build.MODEL);
        list.add(String.valueOf(total));
        list.add(String.valueOf(dangerous));
        list.add(String.valueOf(notfound));
        BackgroudReport backgroudReport = new BackgroudReport();
        backgroudReport.execute(list);
        TextView tv = findViewById(R.id.textViewApps);
        tv.setText("Total packages checked :" + total + ", not recognized: " + notfound + ", dangerous: " + dangerous);
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
        TextView textView = findViewById(R.id.textViewSelected);
        try {
            Spinner mySpinner = (Spinner) parent;
            String text = mySpinner.getSelectedItem().toString();
            if (!text.equalsIgnoreCase(dangerousDefault) && !text.equalsIgnoreCase(spinnerDefault)) {
                textView.setText(text);
                appSelected = text = mySpinner.getSelectedItem().toString();
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
            }


        } catch (PackageManager.NameNotFoundException e1) {
        } catch (NoSuchAlgorithmException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void onButtonClick(View view)
    {
        String button_text;
        button_text = ((Button) view).getText().toString();
        if (button_text.equalsIgnoreCase("Back")){
            Intent intent = new Intent(this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

        }
        if (button_text.equalsIgnoreCase("Delete")){
            Intent intent = new Intent(Intent.ACTION_DELETE);
            intent.setData(Uri.parse("package:"+appSelected));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
    }



}
