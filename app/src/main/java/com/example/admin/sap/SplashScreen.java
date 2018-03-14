package com.example.admin.sap;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        //showing splash screen for desired time
        new Handler().postDelayed(new Runnable() {

        /*
         * Showing splash screen with a timer. This will be useful when you
         * want to show case your app logo / company
         */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }}


//public class SplashScreen extends Activity {
//    private final int time = 2000;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.splash_screen);
//
//        //requestWindowFeature(Window.FEATURE_NO_TITLE);
//        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        SplashLauncher splashLauncher = new SplashLauncher();
//        splashLauncher.run();
//
//    }
//    private class SplashLauncher extends Thread{
//        @Override
//        public void run() {
//            try{
//                sleep(time);
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
//            Intent intent= new Intent(SplashScreen.this, MainActivity.class);
//            startActivity(intent);
//            SplashScreen.this.finish();
//        }
//    }





