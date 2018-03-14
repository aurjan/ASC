package com.example.admin.sap.loadingScreen;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.pm.ApplicationInfo;
import android.text.method.ScrollingMovementMethod;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.Spinner;
import android.widget.TextView;

import com.example.admin.sap.R;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Admin on 12/9/2017.
 */

public class DisplayClass extends Activity implements AdapterView.OnItemSelectedListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_layout);


        //String tv;
        TextView tv = findViewById(R.id.textView3);
        tv.setMovementMethod( new ScrollingMovementMethod());
        ArrayList<String> progArray = new ArrayList<>();
        Spinner spinner = findViewById(R.id.spinner);

//        for (ApplicationInfo packageInfo : packages) {
//            //Log.d(TAG, "Installed package :" + packageInfo.packageName);
//            //Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
//            String stringName = ("Package : " + packageInfo);
//            progArray.add(stringName);
//            //Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
//        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, progArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

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

}
