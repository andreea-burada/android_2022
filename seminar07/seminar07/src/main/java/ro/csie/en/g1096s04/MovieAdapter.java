package ro.csie.en.g1096s04;

import static android.os.Looper.getMainLooper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class MovieAdapter extends  RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private List<Movie> mItems;
    private Context mContext;
    ExecutorService executorService;
    Handler uiHandler;

    // constructor with parameters
    public MovieAdapter(Context context, List<Movie> movieList, ExecutorService executorService) {
        this.mContext = context;
        this.mItems = movieList;
        this.executorService = executorService;
    }

    // class for components holder FOR RECYCLE VIEW
    public static class MovieHolder extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvRelease;
        public TextView tvBudgetGenre;
        public TextView tvDuration;
        public TextView tvRating;
        public ImageView ivPoster;
        public View mView;
        public CheckBox checkboxMovieHolder;

        public MovieHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvRelease = itemView.findViewById(R.id.tvRelease);
            tvBudgetGenre = itemView.findViewById(R.id.tvBudgetGenre);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvRating = itemView.findViewById(R.id.tvRating);
            ivPoster = itemView.findViewById(R.id.imageView);
            checkboxMovieHolder = itemView.findViewById(R.id.checkBoxMovie);
        }
    }

    @NonNull
    @Override
    public MovieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View itemView = inflater.inflate(R.layout.movie_item, parent, false);
        // new instance of movie holder which is custom holder for all components
        // see movie_item.xml
        MovieHolder viewHolder = new MovieHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MovieHolder holder, int position) {
        // get movie item from the list
        Movie item = mItems.get(position);
        // set title
        holder.tvTitle.setText("Title: " + item.getTitle());
        // set release date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        holder.tvRelease.setText("Release date: " + sdf.format(item.getRelease()));
        // set genre
        String genre = String.format("Genre: %s", item.getGenre());
        holder.tvBudgetGenre.setText(genre);
        // set duration
        holder.tvDuration.setText("Duration: " + item.getDuration().toString() + " min");
        // set rating
        holder.tvRating.setText("Rating: " + item.getRating().toString() + " ☆");
        holder.checkboxMovieHolder.setChecked(item.getRecommended());

        // class extra task:
        holder.checkboxMovieHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get id of listed movie
                Toast.makeText(mContext, "Movie - " + item.getTitle() + (holder.checkboxMovieHolder.isChecked() ? " is recommended" : " is not recommended"), Toast.LENGTH_SHORT).show();

            }
        });

        /// handle image download
        int resid = mContext.getResources().getIdentifier(item.getTitle(), "string", mContext.getPackageName());
        if(resid != 0)
        {
            // download poster image
            uiHandler = new Handler(getMainLooper(), new Handler.Callback() {
                @Override
                public boolean handleMessage(@NonNull Message message) {
                    int receivedBitmapCode = 1122;
                    if(message.what == receivedBitmapCode)
                    {
                        holder.ivPoster.setImageBitmap((Bitmap) message.obj);
                    }
                    return  true;
                }
            });
            executorService.submit(new DownloadPosterTask(mContext.getString(resid), uiHandler));
        }
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "Clicked: " + item, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}
