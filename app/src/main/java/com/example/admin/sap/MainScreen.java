package com.example.admin.sap;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Admin on 3/10/2018.
 */

public class MainScreen extends Activity {

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
            //controller.startScan();
        }
    }

}
