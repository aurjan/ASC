package com.example.admin.asc.displayPermissions;

/**
 * Created by Admin on 5/16/2018.
 */


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.admin.asc.R;

import java.util.List;

public class AppAdapter extends ArrayAdapter<ApplicationInfo> {

    private List<ApplicationInfo> appList = null;
    private Context context;
    private PackageManager packageManager;

    public AppAdapter(Context context, int resource,
                      List<ApplicationInfo> objects, PackageManager pm) {
        super(context, resource, objects);

        this.context = context;
        this.appList = objects;
        packageManager = pm;
    }

    @Override
    public int getCount() {
        return ((null != appList) ? appList.size() : 0);
    }

    @Override
    public ApplicationInfo getItem(int position) {
        return ((null != appList) ? appList.get(position) : null);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (null == view) {
            LayoutInflater layoutInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, null);
        }

        ApplicationInfo data = appList.get(position);

        if (null != data) {
            TextView appName = (TextView) view.findViewById(R.id.app_name);

            PackageInfo packageInfo = null;
            try {
                packageInfo = packageManager.getPackageInfo(data.packageName, PackageManager.GET_PERMISSIONS);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            int count;
            if (packageInfo.requestedPermissions != null) {
                count = packageInfo.requestedPermissions.length;
            } else {
                count = 0;
            }
            String string = "Permissions required: " + count;
            TextView packageName = (TextView) view.findViewById(R.id.app_package);
            ImageView iconView = (ImageView) view.findViewById(R.id.app_icon);

            appName.setText(data.loadLabel(packageManager));
            packageName.setText(string);
            iconView.setImageDrawable(data.loadIcon(packageManager));

        }
        return view;
    }
}
