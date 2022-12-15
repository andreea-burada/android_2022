package ro.csie.en.g1096s04;

import android.content.Context;
import android.content.res.Configuration;
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

    static private int paddingLeft = 85;
    static private int paddingRight = 85;
    static private int paddingTop = 0;
    static private int paddingBottom = 0;
    static private int paddingBetweenColumn = 15;

    public MovieChart(Context context, List<Movie> movieListFromDB) {
        super(context);
        this.context = context;
        this.random = new Random();
        this.paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        stats = crunchStats(movieListFromDB);

    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private Map<String, Integer> crunchStats(List<Movie> movieListFromDB) {
        Map<String, Integer> toReturn = new HashMap<>();
        for (Movie currentMovie : movieListFromDB)
        {
            if (currentMovie.getRecommended() == true) {
                if (toReturn.containsKey(currentMovie.getGenre().toString())) {
                    Integer value = toReturn.get(currentMovie.getGenre().toString());
                    toReturn.put(currentMovie.getGenre().toString(), value + 1);
                } else {
                    toReturn.put(currentMovie.getGenre().toString(), 1);
                }
            }
        }
        return toReturn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int maxValue = getMaxValue();
        float colWidth = (float) (getWidth() - paddingLeft - paddingRight) / stats.size();
        drawValues(canvas, maxValue, colWidth);
    }

    private void drawValues(Canvas canvas, int maxValue, float colWidth) {
        int currentColumn = 0;
        for (String genre : stats.keySet())
        {
            drawColumn(canvas, stats.get(genre), maxValue, colWidth, currentColumn, genre);
            currentColumn++;
        }
    }

    private void drawColumn(Canvas canvas, int value, int maxValue, float colWidth, int currentColumn, String label) {
        int color = generateColor();
        int offset = 20;
        paint.setColor(color);

        float unit = (float) (getHeight() - paddingBottom - paddingTop) / maxValue;
        float x1; float y1;
        float x2; float y2;
        x1 = currentColumn * colWidth + paddingLeft;
        //y1 = (1 - (float) value / maxValue) * getHeight();
        y1 = unit * (maxValue - value) + paddingTop;
        x2 = (currentColumn + 1) * colWidth + paddingLeft - paddingBetweenColumn;
        y2 = getHeight() - paddingBottom;
        canvas.drawRect(x1, y1, x2, y2, paint);

        //draw label
        paint.setColor(Color.WHITE);
        paint.setTextSize(65);
        canvas.rotate(-90, x1 + (colWidth - paddingBetweenColumn) / 2 + offset, y2 - offset * 3);
        canvas.drawText(label + ":" + value, x1 + (colWidth - paddingBetweenColumn) / 2 + offset, y2 - offset * 3, paint);
        canvas.rotate(90, x1 + (colWidth - paddingBetweenColumn) / 2 + offset, y2 - offset * 3);
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