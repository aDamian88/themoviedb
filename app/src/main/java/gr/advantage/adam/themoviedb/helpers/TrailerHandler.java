package gr.advantage.adam.themoviedb.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrailerHandler {
    private final Activity activity;

    public TrailerHandler(Activity activity) {
        this.activity = activity;
    }

    public void decodingVideoResponse(String response) {

        try {
            JSONObject videoResponse = new JSONObject(response);
            JSONArray videoArray = videoResponse.getJSONArray("results");
            JSONObject videoObject = videoArray.getJSONObject(0);
            String site = videoObject.getString("site");
            String videoKey = videoObject.getString("key");
            if (site.equals("YouTube")) openTrailer(videoKey);
            Log.d("TAG", "decodingVideoResponse: site " + site + " key " + videoKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openTrailer(String videoKey) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoKey));
        activity.startActivity(browserIntent);
    }
}
