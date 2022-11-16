package com.example.seminar04;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnSave, btnReleaseDate;
    Spinner spinnerGenre;
    SeekBar seekBarDuration;
    EditText editTextTitle;
    CheckBox checkBoxWatchlist;
    Switch switchRecommended;
    RatingBar ratingBarRating;
    TextView textViewDuration;
    TextView textViewRatingValue;
    ImageView imageViewPoster;
    static final int PICK_IMAGE = 1;
    Drawable imageViewInitial;

    Movie movie;

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

        initializeControls();

        // link activity to my observer
        getLifecycle().addObserver(new MyObserver());

        // initialize movie at app creation
        movie = new Movie();
    }

    private void initializeControls() {
        // SAVE BUTTON
        btnSave = findViewById(R.id.btnSave);
        imageViewInitial = ((ImageView)findViewById(R.id.imageViewPoster)).getDrawable();
        /* btnSave.setOnClickListener(this);
        btnReleaseDate.setOnClickListener(this);
        // another way
        btnSave.setOnClickListener(new MyOnClickListener());
        // another way -> anonymous class -> we can do this because OnClickListener is a functional interface
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(view.getId() == R.id.btnSave)
                   Toast.makeText(getApplicationContext(), "Movie saved!", Toast.LENGTH_LONG).show();
            }
        });*/

        // lambda method (needs if)
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // save movie title
                if (editTextTitle.getText().toString().equals(""))
                {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
                    // set title
                    alertDialogBuilder.setTitle("Invalid Movie Title");
                    // set dialog message
                    alertDialogBuilder
                            .setMessage("Movie Title cannot be blank. Please pick a valid Title.")
                            .setCancelable(false)
                            .setNegativeButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {
                                    // if this button is clicked, just close the popup
                                    dialog.cancel();
                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    // show it
                    alertDialog.show();
                }
                else {
                    movie.setTitle(editTextTitle.getText().toString());
                    // reset editText
                    editTextTitle.setText("");
                    // reset imageView
                    imageViewPoster.setImageDrawable(imageViewInitial);
                    // TO DO reset genre
                    
                    // TO DO reset duration

                    // TO DO reset rating

                    // TO DO reset release date

                    // TO DO recommended

                    // TO DO watchlist

                    // Display movie instance
                    Toast.makeText(getApplicationContext(), "Movie: " + movie, Toast.LENGTH_LONG).show();
                }
            }
        });

        // EDIT TEXT - TITLE
        editTextTitle = findViewById(R.id.editTextTitle);

        // DATA PICKER -> BUTTON
        btnReleaseDate = findViewById(R.id.buttonReleaseDate);
        btnReleaseDate.setOnClickListener(new View.OnClickListener() {
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
                        SimpleDateFormat buttonDateFormat = new SimpleDateFormat("MMM dd yyyy");
                        btnReleaseDate.setText(buttonDateFormat.format(release));
                    }
                }, year, month, day);
                dpd.show();
            }
        });
        editTextTitle = findViewById(R.id.editTextTitle);

        // IMAGE VIEW -> PICK POSTER FROM GALLERY
        imageViewPoster = findViewById(R.id.imageViewPoster);
        imageViewPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open intent to select gallery app
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                Intent chooser = Intent.createChooser(intent, "Select Picture for Movie Poster:");
                startActivityForResult(chooser, PICK_IMAGE);

            }
        });

        // SPINNER
        spinnerGenre = findViewById(R.id.spinnerGenre);
        // set spinner
        spinnerGenre.setAdapter(new ArrayAdapter<Genre>(this, android.R.layout.simple_spinner_item, Genre.values()));

        spinnerGenre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                movie.setGenre(Genre.valueOf(adapterView.getItemAtPosition(i).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // SEEK BAR
        seekBarDuration = findViewById(R.id.seekBarDuration);
        textViewDuration = findViewById(R.id.textViewDurationDisplay);
        seekBarDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // i -> value of the seek bar selected
                movie.setDuration(i);
                // TO DO: set the duration on screen
                textViewDuration.setText(String.valueOf(i) + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // CHECKBOX
         checkBoxWatchlist = findViewById(R.id.checkBoxWatchlist);
         checkBoxWatchlist.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 movie.setWatchlist(checkBoxWatchlist.isChecked());
             }
         });

         // SWITCH
        switchRecommended = findViewById(R.id.switchRecommended);
        switchRecommended.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                movie.setRecommended(b);
            }
        });

        // RATING
        ratingBarRating = findViewById(R.id.ratingBarRating);
        textViewRatingValue = findViewById(R.id.textViewRatingValue);
        ratingBarRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                movie.setRating((byte) v);
                textViewRatingValue.setText(String.valueOf(movie.getRating()));
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            // set imageView
            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == PICK_IMAGE) {
                // Get the url of the image from data
                Uri selectedPosterUri = data.getData();
                if (null != selectedPosterUri) {
                    // update the preview image in the layout
                    imageViewPoster.setImageURI(selectedPosterUri);
                    // set movie instance poster resource
                    movie.setPoster(selectedPosterUri.toString());
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btnSave)
            Toast.makeText(this, "Movie saved!", Toast.LENGTH_LONG).show();
    }
}