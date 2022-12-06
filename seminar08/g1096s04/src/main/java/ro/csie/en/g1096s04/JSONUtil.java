package ro.csie.en.g1096s04;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class JSONUtil {
    public static ArrayList<Movie> getJsonFromString(String moviesJsonRaw) {
        ArrayList<Movie> moviesList = null;
        try {
            JSONArray moviesJsonArray = new JSONArray(moviesJsonRaw);
            for(int index = 0; index < moviesJsonArray.length(); index++)
            {
                JSONObject currentMovieJson = moviesJsonArray.getJSONObject(index);
                Movie currentMovie = getMovie(currentMovieJson);
                // add movie to list
                if (moviesList == null)
                    moviesList = new ArrayList<>();
                moviesList.add(currentMovie);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return moviesList;
    }

    private static Movie getMovie(JSONObject currentMovieJson) throws JSONException, ParseException {
        Movie currentMovie = new Movie();
        currentMovie.setTitle(currentMovieJson.get("title").toString());
        currentMovie.setGenre(Genre.valueOf(currentMovieJson.get("genre").toString()));
            currentMovie.setRelease(new SimpleDateFormat("dd-MM-yyyy").parse(currentMovieJson.get("release").toString()));
        currentMovie.setDuration(Integer.parseInt(currentMovieJson.get("duration").toString()));
        return currentMovie;
    }
}
