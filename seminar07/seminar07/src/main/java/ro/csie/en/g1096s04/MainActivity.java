package ro.csie.en.g1096s04;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btnSave, btnRelease;
    Spinner spGenre;
    SeekBar sbDuration;
    RatingBar rbMovieRating;
    EditText etTitle;
    Switch aSwitch;

    Movie movie;

//    class MyOnClickListener implements View.OnClickListener
//    {
//        @Override
//        public void onClick(View view) {
//            if(view.getId() == R.id.btnSave)
//                Toast.makeText(getApplicationContext(), "Movie saved!", Toast.LENGTH_LONG).show();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movie = new Movie();
        initializeControls();

        getLifecycle().addObserver(new MyObserver());

//        btnSave.setOnClickListener(this);
//        btnSave.setOnClickListener(new MyOnClickListener());
//        btnSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(view.getId() == R.id.btnSave)
//                    Toast.makeText(getApplicationContext(), "Movie saved!", Toast.LENGTH_LONG).show();
//            }
//        });
        btnSave.setOnClickListener(view ->
        {
            if (btnRelease.getText().equals("Select Release Date"))
            {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setTitle("Invalid Release Date");
                alertDialog.setMessage("Please select a date for the release date of the movie.");
                alertDialog.setCancelable(false);
                alertDialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                alertDialog.create().show();
            }
            else {
                // set title
                movie.setTitle(etTitle.getText().toString());

                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });

    }

    private void initializeControls() {
        btnSave = findViewById(R.id.btnSave);
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

        // RATING
        rbMovieRating = findViewById(R.id.rbMovieRating);
        rbMovieRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                // set rating for movie
                movie.setRating((byte)rbMovieRating.getRating());
            }
        });

        // DURATION
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

        // RECOMMENDED
        aSwitch = findViewById(R.id.switchRecommended);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                movie.setRecommended(b);
            }
        });

    }

//    @Override
//    public void onClick(View view) {
//        if(view.getId() == R.id.btnSave)
//            Toast.makeText(this, "Movie saved!", Toast.LENGTH_LONG).show();
//    }
}