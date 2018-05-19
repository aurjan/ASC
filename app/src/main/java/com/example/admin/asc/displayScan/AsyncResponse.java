package com.example.admin.asc.displayScan;

import android.content.pm.ApplicationInfo;

import java.util.Map;

/**
 * Created by Admin on 5/12/2018.
 */

public interface AsyncResponse {
    void processFinish(Map<String, ApplicationInfo> output);
}