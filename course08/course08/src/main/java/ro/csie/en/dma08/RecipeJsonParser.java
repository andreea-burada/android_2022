package ro.csie.en.dma08;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeJsonParser {

    public static List<Recipe> fromJson(String recipeJSONArray) {
        List<Recipe> recipes = null;
        if (recipeJSONArray != null) {
            recipes = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(recipeJSONArray);
                for(int index = 0; index < jsonArray.length(); index++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(index);
                    Recipe recipe = readRecipe(jsonObject);
                    recipes.add(recipe);
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return recipes;
    }

    public static JSONArray toJson(List<Recipe> recipes) {
       if (recipes.size() > 0){
           JSONArray recipesJsonArray = new JSONArray();
           for (Recipe recipe : recipes) {
               JSONObject recipeJSON = new JSONObject();
               try {
                   recipeJSON.put("denumire", recipe.getDenumire());
                   recipeJSON.put("descriere", recipe.getDescriere());
                   recipeJSON.put("dataAdaugarii", recipe.getDataAdaugarii());
                   recipeJSON.put("calorii", recipe.getCalorii());
                   recipeJSON.put("categorie", recipe.getCategorie());
               } catch (JSONException e) {
                   e.printStackTrace();
               }
               // add to json array
               recipesJsonArray.put(recipeJSON);
           }
           return recipesJsonArray;
       }
       return null;
    }

    private static Recipe readRecipe(JSONObject jsonObject) throws JSONException {
        String name = jsonObject.getString("denumire");
        String description = jsonObject.getString("descriere");
        String date = jsonObject.getString("dataAdaugarii");
        int calories = jsonObject.getInt("calorii");
        String category = jsonObject.getString("categorie");

        return new Recipe(name, description, date, calories, category);
    }

}
