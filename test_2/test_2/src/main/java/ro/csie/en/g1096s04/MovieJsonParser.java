package ro.csie.en.g1096s04;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MovieJsonParser {

    public static List<Movie> fromJson(String recipeJSONArray) {
        List<Movie> recipes = null;
        if(recipeJSONArray != null)
        {
            recipes = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(recipeJSONArray);
                for(int index =0; index<jsonArray.length(); index++)
                {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    Movie movie = readMovie(jsonObject);
                    recipes.add(movie);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return recipes;
    }

    private static Movie readMovie(JSONObject jsonObject) throws JSONException, ParseException {
        String movieTitle = jsonObject.getString("title");

        String genre = jsonObject.getString("genre");
        Genre movieGenre = Genre.valueOf(genre);

        String release = jsonObject.getString("release");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date movieRelease = simpleDateFormat.parse(release);

        int movieDuration = jsonObject.getInt("duration");

        String moviePoster = jsonObject.getString("poster");

        boolean movieRecommended = jsonObject.getString("boxoffice").equals("true") ? true : false;

        return new Movie(movieTitle, movieGenre, movieRelease, movieDuration, moviePoster, movieRecommended);
    }
}
