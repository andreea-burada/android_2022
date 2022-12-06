package ro.csie.en.dma08;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.ListResourceBundle;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity {

    private static final String RECIPE_GET_JSON = "https://jsonkeeper.com/b/OCIE";
    //private static final String RECIPE_POST_JSON = "https://ptsv2.com/t/MDA2021/post";
    //private static final String RECIPE_POST_JSON = "https://ptsv2.com/t/w1m4t-1669191112/post";
    private static final String RECIPE_POST_JSON = "https://httpdump.app/dumps/9b132b52-b676-4d35-920a-bab9d728068e";
    private static final String TAG = MainActivity.class.getName();
    private Button btnGet;
    private Button btnPost;
    private TextView tvPostResult;
    private List<Recipe> recipes= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGet = findViewById(R.id.btnGet);
        btnPost = findViewById(R.id.btnPost);
        tvPostResult = findViewById(R.id.textView);
        trustEveryone();
    }

    public void getJson(View view)
    {
        Toast.makeText(MainActivity.this, "GET Request for Recipes", Toast.LENGTH_SHORT).show();
        Thread thread= new Thread()
        {
            @Override
            public void run() {
                HttpConnectionService httpConnectionService = new HttpConnectionService(RECIPE_GET_JSON);
                String recipeJSONArray = httpConnectionService.getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // parse the json
                        List<Recipe> results = RecipeJsonParser.fromJson(recipeJSONArray);
                        if (results.size() > 0) {
                            if (recipes.containsAll(results) == false)
                                recipes.addAll(results);
                            StringBuilder recipesString = new StringBuilder();
                            for (Recipe recipe : recipes) {
                                Log.d(TAG, "Recipe: " + recipe);
                                recipesString.append(recipe.toString()).append("\n");
                            }
                            tvPostResult.setText(recipesString.toString());
                        }
                        else
                        {
                            tvPostResult.setText("No data retrieved.");
                        }
                    }
                });
            }
        };
        thread.start();
    }

    public void postJson(View view) {
        Toast.makeText(MainActivity.this, "POST Request for Recipes", Toast.LENGTH_SHORT).show();
        if (recipes.size() > 0) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    JSONArray recipesJSONArray = RecipeJsonParser.toJson(recipes);
                    HttpConnectionService httpConnectionService = new HttpConnectionService(RECIPE_POST_JSON);
                    String postResult = httpConnectionService.postData(recipesJSONArray);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (postResult != null)
                                tvPostResult.setText(postResult);
                            else
                                tvPostResult.setText("URL could not be accessed.");
                        }
                    });
                }
            };
            thread.start();
        }
        else
        {
            Toast.makeText(this, "No data to send through POST request.", Toast.LENGTH_SHORT).show();
        }
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }});
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

}