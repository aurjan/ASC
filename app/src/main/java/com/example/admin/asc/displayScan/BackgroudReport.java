package com.example.admin.asc.displayScan;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by Admin on 5/17/2018.
 */

public class BackgroudReport extends AsyncTask<List<String>, Void, Void> {
    private static final String REPORT_URL = "http://193.219.91.103:5858/report.php";
    private String total;
    private String dangerous;
    private String unrecognised;
    private String model;

    @Override
    protected Void doInBackground(List<String>... passing) {
        List<String> list = passing[0];
        model = list.get(0);
        total = list.get(1);
        dangerous = list.get(2);
        unrecognised = list.get(3);
        try {
            URL url = new URL(REPORT_URL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
            String data = URLEncoder.encode("model", "UTF-8") + "=" + URLEncoder.encode(model, "UTF-8") + "&" +
                    URLEncoder.encode("total", "UTF-8") + "=" + URLEncoder.encode(total, "UTF-8") + "&" +
                    URLEncoder.encode("dangerous", "UTF-8") + "=" + URLEncoder.encode(dangerous, "UTF-8") + "&" +
                    URLEncoder.encode("unrecognised", "UTF-8") + "=" + URLEncoder.encode(unrecognised, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            IS.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
