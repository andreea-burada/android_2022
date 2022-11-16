package com.example.catapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String pet = new String();
        try {
            pet = new String(this.getIntent().getStringExtra(Intent.EXTRA_TEXT));
        }
        catch (Exception ex) {
        }

        ImageView petView = (ImageView)findViewById(R.id.imageView);
        if (pet.equals("cat"))
        {
            petView.setImageResource(getResources().getIdentifier("@drawable/cat_halloween",
                    null, this.getPackageName()));
        }
        else if(pet.equals("dog"))
        {
            petView.setImageResource(getResources().getIdentifier("@drawable/dog",
                    null, this.getPackageName()));
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}