package ro.csie.en.dma08;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpConnectionService {

    private static final String TAG = HttpConnectionService.class.getName();
    private String recipeJsonUrl;

    public HttpConnectionService(String recipeJson) {

        this.recipeJsonUrl = recipeJson;
    }

    public String getData() {
        StringBuilder jsonFile = new StringBuilder();
        HttpURLConnection connection = null;
        try {
            Log.d(TAG, "Start" + jsonFile.toString());
            URL url = new URL(recipeJsonUrl);
            connection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = connection.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                jsonFile.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
        }
        Log.d(TAG, "Stop:" + jsonFile.toString());
        return jsonFile.toString();
    }

    public String postData(String jsonArray) {
        JSONObject recipesJsonData = new JSONObject();
        String result = null;
        try {
            // get json array from string
            JSONArray recipesJsonArray = new JSONArray(jsonArray);
            recipesJsonData.put("name", "Burada Andreea 1096");
            recipesJsonData.put("data", recipesJsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpURLConnection connection = null;
        try {
            URL url = new URL(recipeJsonUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);
            connection.connect();

            OutputStream dataOut = connection.getOutputStream();
            BufferedWriter dataWriter = new BufferedWriter(new OutputStreamWriter(dataOut, "UTF-8"));
            dataWriter.write(recipesJsonData.toString());
            dataWriter.close();
            dataOut.close();

            BufferedReader dataReader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line = null;
            StringBuilder stringBuilder = new StringBuilder();
            while((line = dataReader.readLine()) != null){
                stringBuilder.append(line);
            }
            dataReader.close();
            result = stringBuilder.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            connection.disconnect();
            return result;
        }
    }
}

