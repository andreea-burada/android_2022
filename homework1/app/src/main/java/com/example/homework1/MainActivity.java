package com.example.homework1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton cat = findViewById(R.id.catButton);
        ImageButton dog = findViewById(R.id.dogButton);

        cat.setOnClickListener(this);
        dog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        int idClicked = view.getId();

        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        //i.setType("text/plain");

        if (idClicked == R.id.catButton)
        {
            i.putExtra(Intent.EXTRA_TEXT, "cat");
        }
        else if (idClicked == R.id.dogButton)
        {
            i.putExtra(Intent.EXTRA_TEXT, "dog");
        }

        Intent chooser = Intent.createChooser(i, "Select PetView App:");
        startActivity(chooser);
    }
}