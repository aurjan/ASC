package com.example.admin.sap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.example.admin.sap.loadingScreen.LoadingScreen;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void onButtonClick(View view)
    {
        System.out.println("main");
        String button_text;
        //Controller controller = new Controller();
        button_text = ((Button) view).getText().toString();
        if (button_text.equals("Start Scan")){
            Intent intent = new Intent(this,LoadingScreen.class);
            startActivity(intent);

        }
    }

}
