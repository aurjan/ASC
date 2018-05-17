package com.example.admin.sap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.admin.sap.displayScreens.ViewAppsPermissions;
import com.example.admin.sap.loadingScreen.LoadingScreen;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onButtonClick(View view)
    {
        String button_text;
        //Controller controller = new Controller();
        button_text = ((Button) view).getText().toString();
        if (button_text.equals("Start Scan")){
            Intent intent = new Intent(this,LoadingScreen.class);
            startActivity(intent);


        }
        if (button_text.equals("Check apps permissions")){
            Intent intent = new Intent(this,ViewAppsPermissions.class);
            startActivity(intent);

        }
        if (button_text.equals("Exit")){
            Intent a = new Intent(Intent.ACTION_MAIN);
            a.addCategory(Intent.CATEGORY_HOME);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(a);
            finish();
        }
    }
    @Override
    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
        finish();

    }

}
