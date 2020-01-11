package gr.advantage.adam.themoviedb.helpers;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import gr.advantage.adam.themoviedb.api.Api;
import gr.advantage.adam.themoviedb.api.RetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrailerHandler {
    private static final String TAG = "TrailerHandler";

    public static void showTrailer(Integer movieId){
        Api api = RetrofitService.createService();


        Call<JsonObject> call = api.getSearchResult(Api.BASE_URL + "movie/" + movieId + "/" + "videos?api_key=" + Api.AUTH_KEY);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                String results = "";
                try {
                    if (response.body() != null) {
                        JSONObject responseObject = new JSONObject(response.body().toString());
                        results = responseObject.getString("results");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (results.isEmpty() || results.equals("[]")) {
                    Toast.makeText(MovieDBSession.getAppContext(), "Trailer is not available.", Toast.LENGTH_LONG).show();
                } else {
                    if(response.body()!=null)decodingVideoResponse(response.body().toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull Throwable t) {
                Log.e("error", "search response onFailure: " + t.toString());
            }
        });

    }

    private static void decodingVideoResponse(String response) {

        try {
            JSONObject videoResponse = new JSONObject(response);
            JSONArray videoArray = videoResponse.getJSONArray("results");
            JSONObject videoObject = videoArray.getJSONObject(0);
            String site = videoObject.getString("site");
            String videoKey = videoObject.getString("key");
            if (site.equals("YouTube")){
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoKey));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MovieDBSession.getAppContext().startActivity(browserIntent);
            }else{
                Log.d(TAG, "decodingVideoResponse: ");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
