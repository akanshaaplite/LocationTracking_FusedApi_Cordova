package com.demo;

import android.os.StrictMode;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by akanshajain on 30/10/17.
 */

public class WebserviceHandler {
    public static String serverURL = "http://staging.talkontask.com/en/services/index.php/";
    String serverReponse = "0";
    public WebserviceHandler() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

       //Webservice for calling Login of user
    public String getUserLoginWithEmail(JSONObject jsonObjectLogin) {
        try {
            URL url = new URL(serverURL + "register/doLogin");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            // conn.setConnectTimeout(20000);
            // conn.setReadTimeout(30000);
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonObjectLogin.toString());
            wr.flush();

            // Get the server response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;


            while ((line = br.readLine()) != null) {
                // Append server response in string
                sb.append(line);
            }

            serverReponse = sb.toString();
            System.out.println(serverReponse + "********");
            url = null;
            sb = null;
            br = null;
            conn.disconnect();
        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return serverReponse;
    }
    public String Save_current_location(JSONObject jsonObjectsavecurrentlocation) {
        try {
            URL url = new URL(serverURL + "Main/updateUserLocation");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            // conn.setConnectTimeout(20000);
            // conn.setReadTimeout(30000);
            conn.setDoOutput(true);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(jsonObjectsavecurrentlocation.toString());
            wr.flush();

            // Get the server response
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;


            while ((line = br.readLine()) != null) {
                // Append server response in string
                sb.append(line);

            }

            serverReponse = sb.toString();
            System.out.println(serverReponse + "********");
            Log.e("",serverReponse);
            url = null;
            sb = null;
            br = null;
            conn.disconnect();
        } catch (UnknownHostException e) {
            e.printStackTrace();

        } catch (SocketTimeoutException e) {
            e.printStackTrace();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();

        }
        return serverReponse;
    }
}
