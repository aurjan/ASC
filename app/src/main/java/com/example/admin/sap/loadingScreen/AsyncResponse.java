package com.example.admin.sap.loadingScreen;

import android.content.pm.ApplicationInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 5/12/2018.
 */

public interface AsyncResponse {
    void processFinish(Map<String,ApplicationInfo> output);
}