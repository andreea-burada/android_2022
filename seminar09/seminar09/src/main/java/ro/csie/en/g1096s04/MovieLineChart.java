package ro.csie.en.g1096s04;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@SuppressLint("ViewConstructor")
public class MovieLineChart extends View {
    // attributes
    private Map<String, Integer> moviesStats;
    private Context context;
    private Paint paint;
    private Random random;
    static private int paddingLeft = 85;
    static private int paddingRight = 85;
    static private int paddingTop = 160;
    static private int paddingBottom = 0;

    public MovieLineChart(Context context, List<Movie> movieList) {
        super(context);
        this.context = context;
        random = new Random();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // get stats
        moviesStats = getStats(movieList);
    }

    private Map<String, Integer> getStats(List<Movie> movieList) {
        Map<String, Integer> toReturn = new HashMap<>();
        for(Movie currentMovie : movieList)
        {
            if (toReturn.containsKey(currentMovie.getGenre().toString()))
            {
                // increment value
                Integer value = toReturn.get(currentMovie.getGenre().toString());
                toReturn.put(currentMovie.getGenre().toString(), value + 1);
            }
            else
            {
                // add key to map with value 1
                toReturn.put(currentMovie.getGenre().toString(), 1);
            }
        }
        return toReturn;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLineGraph(canvas);
    }

    private void drawLineGraph(Canvas canvas) {
        // compute matrix of coordinates
        float[][] coordMatrix = new float[moviesStats.size()][2];
        float radius = 30;
        int maxStat = getMaxStat(moviesStats);
        float pointDistance = (float) (getWidth() - paddingLeft - paddingRight) / (moviesStats.size() - 1);
        float yUnit = (float) (getHeight() - paddingTop - paddingBottom) / maxStat;
        int currentPoint = 0;
        for (int currentStat : moviesStats.values())
        {
            coordMatrix[currentPoint][0] = currentPoint * pointDistance + paddingLeft;
            coordMatrix[currentPoint][1] = (maxStat - currentStat) * yUnit + paddingTop;
            currentPoint++;
        }
        int lineColor = Color.argb(255, random.nextInt(255), random.nextInt(255), random.nextInt(255));
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(8);
        paint.setTextSize(50);

        // offset for text
        int offsetText = 55;
        // get all labels
        Object[] labelList = moviesStats.keySet().toArray();
        // draw starting circle
        canvas.drawCircle(coordMatrix[0][0], coordMatrix[0][1], radius, paint);

        // paint all lines
        for (int i = 0; i < moviesStats.size() - 1; i++)
        {
            // set line color
            paint.setColor(lineColor);
            float startX = coordMatrix[i][0];
            float startY = coordMatrix[i][1];
            float endX = coordMatrix[i + 1][0];
            float endY = coordMatrix[i + 1][1];
            canvas.drawLine(startX, startY, endX, endY, paint);
            // draw circle for next point
            canvas.drawCircle(endX, endY, radius, paint);
            // draw label for current point
            paint.setColor(Color.WHITE);
            canvas.drawText(labelList[i].toString() + ":" + moviesStats.get(labelList[i]), startX - offsetText, startY - offsetText, paint);
        }

        // draw last label
        paint.setColor(Color.WHITE);
        canvas.drawText(labelList[moviesStats.size() - 1].toString() + ":" + moviesStats.get(labelList[moviesStats.size() - 1]), coordMatrix[moviesStats.size() - 1][0] - offsetText * 5, coordMatrix[moviesStats.size() - 1][1] - offsetText, paint);

    }

    private int getMaxStat(Map<String, Integer> moviesStats) {
        int max_ = 0;
        for (int currentStat : moviesStats.values())
        {
            if (max_ < currentStat)
            {
                max_ = currentStat;
            }
        }
        return max_;
    }
}
