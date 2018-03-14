package com.example.admin.sap.loadingScreen;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.admin.sap.R;

import java.util.List;

/**
 * Created by Admin on 3/10/2018.
 */

public class LoadingScreen extends Activity  {
    private String status="";
    List<ApplicationInfo> packages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.loadingscreen_layout);
        updateWidget();

        try {
            startLoadingScreen();
        }catch (Exception e){
            Log.e("Scanner","Scanner",e);
        }

    }

    public void startLoadingScreen(){

        new Thread(new Runnable() {
            @Override
            public void run() {

                Scanner scanner = new Scanner();
                setPackages(scanner.Scan());
                changeIntent();
                finish();

            }
        });

    }

    public void setStatus(String status) {
        this.status = status;
    }
    void updateWidget(){
        TextView scanStatus  =  findViewById(R.id.statusView);
        scanStatus.setText(status);
    }
    private void changeIntent(){
        Intent intent = new Intent(this,DisplayClass.class);
        startActivity(intent);
    }

    public void setPackages(List<ApplicationInfo> packages) {
        this.packages = packages;
    }
}
