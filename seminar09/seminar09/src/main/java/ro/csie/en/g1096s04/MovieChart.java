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

public class MovieChart extends View {
    private Map<String, Integer> stats;
    private Context context;
    private Random random;
    private Paint paint;

    public MovieChart(Context context, List<Movie> movieListFromDB) {
        super(context);
        this.context = context;
        this.random = new Random();
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stats = crunchStats(movieListFromDB);

    }

    private Map<String, Integer> crunchStats(List<Movie> movieListFromDB) {
        Map<String, Integer> toReturn = new HashMap<>();
        for (Movie currentMovie : movieListFromDB)
        {
            if (toReturn.containsKey(currentMovie.getGenre().toString())) {
                Integer value = toReturn.get(currentMovie.getGenre().toString());
                toReturn.put(currentMovie.getGenre().toString(), value + 1);
            }
            else
            {
                toReturn.put(currentMovie.getGenre().toString(), 1);
            }
        }
        return toReturn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int maxValue = getMaxValue();
        float colWidth = getWidth() / stats.size();
        drawValues(canvas, maxValue, colWidth);
    }

    private void drawValues(Canvas canvas, int maxValue, float colWidth) {
        int currentColumn = 0;
        for (String genre : stats.keySet())
        {
            // get random color
            int color = generateColor();
            paint.setColor(color);
            drawColumn(canvas, stats.get(genre), maxValue, colWidth, currentColumn);
            currentColumn++;
        }
    }

    private void drawColumn(Canvas canvas, int value, int maxValue, float colWidth, int currentColumn) {
        float x1; float y1;
        float x2; float y2;
        x1 = currentColumn * colWidth;
        y1 = (getHeight() / maxValue) * ( maxValue - value);
        //y1 = (1 - (float) value / maxValue) * getHeight();
        x2 = (currentColumn + 1) * colWidth;
        y2 = getHeight();
        canvas.drawRect(x1, y1, x2, y2, paint);
    }

    private int generateColor() {
        return Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    private int getMaxValue() {
        int max = 0;
        for(Integer value : stats.values())
        {
            max = max < value ? value : max;
        }
        return max;
    }
}
