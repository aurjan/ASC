package com.example.admin.sap.loadingScreen;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.List;

/**
 * Created by Admin on 3/10/2018.
 */

public class Scanner extends Activity{
    private LoadingScreen loadingScreen = new LoadingScreen();
    List <android.content.pm.ApplicationInfo> Scan(){
        loadingScreen.setStatus("Scanning");
        loadingScreen.updateWidget();
        final PackageManager pm = getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        return packages;

    }

}
