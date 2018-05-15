package com.example.admin.sap.loadingScreen;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Admin on 5/12/2018.
 */

public class BackGroundTasks extends AsyncTask<Map<String,ApplicationInfo>,Void,Map <String,ApplicationInfo>>{
    private static final String MD5_URL = "http://193.219.91.103:5858/checkMD5.php";
    private Context cnx;
    AlertDialog alertDialog;
    public AsyncResponse mCallback;


    BackGroundTasks(Context cnx){
        this.mCallback = (AsyncResponse) cnx;
        this.cnx = cnx;
    }

    @Override
    public void onPreExecute() {
        super.onPreExecute();
        alertDialog = new AlertDialog.Builder(cnx).create();
    }

    @Override
    public Map <String,ApplicationInfo> doInBackground(Map <String,ApplicationInfo>... passing) {
        Map <String,ApplicationInfo> returnMap = new HashMap<>();
        Map <String,ApplicationInfo> iteratingMap = passing[0];
        String method = "MD5";
        if (method.equals("MD5")){
            for (String MD5key: iteratingMap.keySet()){
                String response = getResponse(MD5key);
                if (response.toLowerCase().contains("fail")){
                    returnMap.put(MD5key,iteratingMap.get(MD5key));
                }
            }

        }
        return returnMap;
    }

    private String getResponse(String MD5) {
        String response = "";
        try {
            URL url = new URL(MD5_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String data = URLEncoder.encode("MD5","UTF-8")+"="+URLEncoder.encode(MD5,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

            String line = "";
            int i =0;
            while ((line =bufferedReader.readLine())!= null){
                if(i>2) {
                    response += line + "\n";
                } else {
                    i++;
                }
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    @Override
    public void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    public void onPostExecute(Map <String,ApplicationInfo> returnMap) {
//        alertDialog.setMessage(string);
//        alertDialog.show();
        mCallback.processFinish(returnMap);
//        delegate.processFinish(string);


    }
}
