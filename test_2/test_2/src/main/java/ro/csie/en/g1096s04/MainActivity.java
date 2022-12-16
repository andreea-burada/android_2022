package ro.csie.en.g1096s04;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    Button btnSave, btnRelease;
    Spinner spGenre;
    SeekBar sbDuration;
    EditText etTitle;
    Switch aSwitch;
    View addedView;

    private static String MOVIE_GET_JSON = "https://jsonkeeper.com/b/708X";

    Movie movie;
    private List<Movie> movies = new ArrayList<>();

    public void readJson(View view) throws IOException {

        // get JSON through HTTP request
        Thread thread= new Thread()
        {
            @Override
            public void run() {
                HttpConnectionService httpConnectionService = new HttpConnectionService(MOVIE_GET_JSON);
                String movieJSONArray = httpConnectionService.getData();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //1.parsing the json result to a list of Movie objects
                        List<Movie> results = MovieJsonParser.fromJson(movieJSONArray);
                        if(!movies.containsAll(results))
                            movies.addAll(results);
                        for(Movie movie: movies)
                        {
                            Log.d(TAG, "Movie:" + movie);
                        }
                        Intent intent = new Intent(MainActivity.this, ListActivity.class);
                        intent.putParcelableArrayListExtra("movies", (ArrayList<? extends Parcelable>) movies);
                        startActivity(intent);
                    }
                });
            }
        };
        thread.start();
    }

    public void viewChart(View view) {
        DatabaseManager db = DatabaseManager.getInstance(this);
        MovieDao movieDao = db.getMovieDao();

        List<Movie> allMovies = movieDao.getAllMovies();
        MovieChart movieChart = new MovieChart(this, allMovies);
        addedView = movieChart;
        setContentView(movieChart);

    }

    @Override
    public void onBackPressed() {
        ViewGroup rootView = findViewById(android.R.id.content);
        if (rootView.getChildAt(0) != addedView)
            super.onBackPressed();
        else
        {
            rootView.removeViewAt(0);
            setContentView(R.layout.activity_main);
            initializeControls();
        }
    }

    class MyOnClickListener implements View.OnClickListener
    {
        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.btnSave)
                Toast.makeText(getApplicationContext(), "Movie saved!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movie = new Movie();
        initializeControls();

        getLifecycle().addObserver(new MyObserver());
        btnSave.setOnClickListener(this);
        btnSave.setOnClickListener(new MyOnClickListener());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btnSave)
                    Toast.makeText(getApplicationContext(), "Movie saved!", Toast.LENGTH_LONG).show();
            }
        });

    }

    private void initializeControls() {
        trustEveryone();

        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(view ->
        {
            movie.setTitle(etTitle.getText().toString());
            Intent intent = new Intent(MainActivity.this, ListActivity.class);
            intent.putExtra("movie",movie);
            startActivity(intent);
        });

        btnRelease = findViewById(R.id.btnRelease);
        btnRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dpd = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, day);
                        Date release = c.getTime();
                        movie.setRelease(release);
                        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy");
                        btnRelease.setText(sdf.format(release));
                    }
                }, year, month, day);
                dpd.show();
            }
        });
        etTitle = findViewById(R.id.etTitle);
        spGenre = findViewById(R.id.spGenre);
        spGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String s = adapterView.getItemAtPosition(i).toString();
                movie.setGenre(Genre.valueOf(s));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sbDuration = findViewById(R.id.sbDuration);
        sbDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                movie.setDuration(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        aSwitch = findViewById(R.id.switch1);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                movie.setRecommended(b);
            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSave)
            Toast.makeText(this, "Movie saved!", Toast.LENGTH_LONG).show();
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