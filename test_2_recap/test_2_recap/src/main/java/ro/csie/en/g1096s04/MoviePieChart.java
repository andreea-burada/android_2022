package ro.csie.en.g1096s04;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MoviePieChart extends View {

    private Map<String, Integer> stats;
    private Context context;
    private Random random;
    private Paint paint;
    private static final int squareSize = 80; // length of square for legend
    private static final int paddingLeft = 25;
    private static final int paddingTop = 30;
    private static final int paddingBetweenLegendItems = 20;
    private float radius = 0f;

    public MoviePieChart(Context context, List<Movie> allMovies) {
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
        int total = getTotal();
        drawValues(canvas, total);
    }

    private void drawValues(Canvas canvas, int total) {
        float squareBoundLength = (((float) getHeight() / 2) > getWidth()) ? ((float)getHeight() / 2) : getWidth();
        RectF pieChartBounds = new RectF(0, getHeight() - squareBoundLength, getWidth(), getHeight());

        int currSlice = 0;
        int noSlices = stats.size();

        float startAngle = 0.0f;   // start angle

        for(String genre : stats.keySet())
        {
            int color = generateColor();
            int value = stats.get(genre);
            paint.setColor(color);
            // start angle needs to be offset with the value of sweepAngle each iteration
            // so that pie slices are adjacent to each other
            startAngle += drawPieSlice(canvas, total, value, startAngle, pieChartBounds);
            drawLabel(canvas, currSlice, value, genre);
            currSlice++;
        }
    }

    private float drawPieSlice(Canvas canvas, int total, int value, float startAngle, RectF pieChartBounds) {
        // compute sweepAngle
        float sweepAngle = ((float) value / total ) * 360;
        canvas.drawArc(pieChartBounds, startAngle, sweepAngle , true, paint);
        return sweepAngle;
    }

    private void drawLabel(Canvas canvas, int currSlice, int value, String genre) {
        // draw small square for legend
        int x1 = paddingLeft;
        int y1 = currSlice * (squareSize + paddingBetweenLegendItems) + paddingTop;
        int x2 = paddingLeft + squareSize;
        int y2 = (currSlice + 1) * squareSize + currSlice * paddingBetweenLegendItems + paddingTop ;
        canvas.drawRect(x1, y1, x2, y2, paint);

        // move x to the left to write text
        x1 = squareSize + 45;   // 25 - padding to right
        // move y so text is showing
        y1 = y2;
        paint.setColor(Color.WHITE);
        paint.setTextSize(squareSize);
        canvas.drawText(genre + ":" + value, x1, y1, paint);
    }

    private int generateColor() {
        return Color.argb(255,
                random.nextInt(255),
                random.nextInt(255),
                random.nextInt(255));
    }

    private int getTotal() {
        int total = 0;
        for(Integer value: stats.values())
        {
            total += value;
        }
        return total;
    }
}
