package com.example.admin.asc.displayPermissions;

/**
 * Created by Admin on 5/16/2018.
 */


import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.admin.asc.R;

import java.util.ArrayList;
import java.util.List;

public class ViewAppsPermissions extends ListActivity {

    private PackageManager packageManager = null;
    private List<ApplicationInfo> applist = null;
    private AppAdapter listadapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.permission__layout);

        packageManager = getPackageManager();
        new LoadApplications().execute();
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ApplicationInfo app = applist.get(position);
        ArrayAdapter arrayAdapter = null;

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(ViewAppsPermissions.this);
            View mView = getLayoutInflater().inflate(R.layout.popup_layout, null);

            builder.setView(mView);
            Button button = (Button) mView.findViewById(R.id.button7);
            ListView listView = (ListView) mView.findViewById(R.id.listView);
            try {

                PackageInfo packageInfo = packageManager.getPackageInfo(app.packageName, PackageManager.GET_PERMISSIONS);
                if (packageInfo.requestedPermissions == null) {
                    Toast.makeText(this, "This application doesn't require any permissions", Toast.LENGTH_LONG).show();
                } else {
                    String[] requestedPermissions = packageInfo.requestedPermissions;
                    System.out.println("lenght :" + requestedPermissions.length);
                    arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, requestedPermissions);
                    listView.setAdapter(arrayAdapter);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }


            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {

        ArrayList<ApplicationInfo> appList = new ArrayList<ApplicationInfo>();

        for (ApplicationInfo info : list) {
            try {
                if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    appList.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return appList;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress = null;

        @Override
        protected Void doInBackground(Void... params) {
            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            listadapter = new AppAdapter(ViewAppsPermissions.this, R.layout.list_item, applist, packageManager);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            setListAdapter(listadapter);
            progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(ViewAppsPermissions.this, null, "Loading apps info...");
            super.onPreExecute();
        }
    }


}
