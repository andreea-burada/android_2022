package ro.csie.en.g1096s04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MovieBarChart extends View {

    private Map<String, Integer> stats;
    private Context context;
    private Random random;
    private Paint paint;

    public MovieBarChart(Context context, List<Movie> allMovies) {
        super(context);
        this.context = context;
        this.random = new Random();
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stats = crunchStats(allMovies);
    }

    private Map<String, Integer> crunchStats(List<Movie> allMovies) {
        Map<String, Integer> stats = new HashMap<>();
        for(Movie movie: allMovies)
        {
            if(stats.containsKey(movie.getGenre().toString()))
            {
                Integer value = stats.get(movie.getGenre().toString());
                stats.put(movie.getGenre().toString(), value+1);
            }
            else
                stats.put(movie.getGenre().toString(), 1);
        }
        return stats;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int maxValue = getMaxValue();
        float colHeight = getHeight() / stats.size();
        drawValues(canvas, maxValue, colHeight);
    }

    private void drawValues(Canvas canvas, int maxValue, float colHeight) {
        int currColumn = 0;
        int noColumns = stats.size();
        for(String genre:stats.keySet())
        {
            int color = generateColor();
            int value = stats.get(genre);
            paint.setColor(color);
            drawColumn(canvas, maxValue, colHeight, currColumn, value, noColumns);
            drawLabel(canvas, colHeight, currColumn, genre, value);
            currColumn++;
        }
    }

    private void drawLabel(Canvas canvas, float colHeight, int currColumn, String genre, int value) {
        paint.setColor(Color.WHITE);
        paint.setTextSize((float) (colHeight * 0.15));
        int offset = 55;
        float x = getWidth() * 0.1f;
        float y = (currColumn + 0.5f) * colHeight;
        canvas.drawText(genre + ":" + value, x, y, paint);
    }

    private void drawColumn(Canvas canvas, int maxValue, float colHeight, int currColumn, int value, int noColumns) {
        float x1 = 0;
        float y1 = currColumn * colHeight;
        float x2 = value * (getWidth() / maxValue);
        float y2 = (currColumn + 1) * colHeight;
        canvas.drawRect(x1,y1,x2, y2, paint);
    }

    private int generateColor() {
        return Color.argb(255,
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255));
    }

    private int getMaxValue() {
        int max = 0;
        for(Integer value: stats.values())
        {
            max = max<value ? value: max;
        }
        return max;
    }
}
