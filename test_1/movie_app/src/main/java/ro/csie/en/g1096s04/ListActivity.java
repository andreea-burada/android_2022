package ro.csie.en.g1096s04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListActivity extends AppCompatActivity {

    static List<Movie> movieList = new ArrayList<>();
    RecyclerView recyclerView;
    ExecutorService executorService;
    Button btnToDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        Movie movie = extras.getParcelable("movie");
        movieList.add(movie);

        recyclerView = findViewById(R.id.rvMovies);
        executorService = Executors.newFixedThreadPool(4);
        MovieAdapter movieAdapter = new MovieAdapter(this, movieList, executorService);
        recyclerView.setAdapter(movieAdapter);

        btnToDelete = findViewById(R.id.btnToDelete);
        btnToDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // iterate through the list and delete all with toDelete = true
                boolean weDeleted = false;
                for(int i = 0; i < movieList.size(); i++)
                {
                    if (movieList.get(i).getToDelete() == true)
                    {
                        movieList.remove(i);
                        i--;
                        weDeleted = true;
                    }
                }
                // send new list ?
                MovieAdapter movieAdapter = new MovieAdapter(ListActivity.this, movieList, executorService);
                recyclerView.setAdapter(movieAdapter);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        executorService.shutdown();
    }
}