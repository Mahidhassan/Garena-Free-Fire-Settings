package com.jvmfrog.ffsettings.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jvmfrog.ffsettings.adapter.DevicesAdapter;
import com.jvmfrog.ffsettings.model.SensitivityModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SensitivitiesHelper {
    public static String GITHUB_SENSITIVITIES_FILES_PATH = "https://raw.githubusercontent.com/IbremMiner837/Garena-Free-Fire-Settings/master/app/src/main/assets/sensitivity_settings/";

    public void getSensitivitiesFromURL(Context context, Fragment fragment, RecyclerView recyclerView, String manufacturer, ShimmerFrameLayout shimmerFrameLayout) {
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GITHUB_SENSITIVITIES_FILES_PATH + manufacturer + ".json", null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("models");
                List<SensitivityModel> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String deviceName = jsonObject.getString("name");
                    String manufacturerName = jsonObject.getString("manufacturer");
                    String settingsSourceURL = jsonObject.getString("settings_source_url");
                    int dpi = jsonObject.getInt("dpi");
                    int fire_button = jsonObject.getInt("fire_button");
                    int review = jsonObject.getJSONObject("sensitivities").getInt("review");
                    int collimator = jsonObject.getJSONObject("sensitivities").getInt("collimator");
                    int x2_scope = jsonObject.getJSONObject("sensitivities").getInt("x2_scope");
                    int x4_scope = jsonObject.getJSONObject("sensitivities").getInt("x4_scope");
                    int sniper_scope = jsonObject.getJSONObject("sensitivities").getInt("sniper_scope");
                    int free_review = jsonObject.getJSONObject("sensitivities").getInt("free_review");
                    list.add(new SensitivityModel(deviceName, manufacturerName, settingsSourceURL, dpi, fire_button,
                            review, collimator, x2_scope, x4_scope, sniper_scope,free_review
                    ));
                }
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setAdapter(new DevicesAdapter(context, fragment, list));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Volley", error.toString()));
        queue.addRequestEventListener((request, event) -> {
            if (event == RequestQueue.RequestEvent.REQUEST_FINISHED) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsonObjectRequest);
    };

    public void getSensitivitiesFromAssets(Context context, Fragment fragment, RecyclerView recyclerView, String manufacturer, ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        try {
            String json = readJSONDataFromFile(context, manufacturer);
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("models");
            List<SensitivityModel> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String deviceName = jsonObject.getString("name");
                String manufacturerName = jsonObject.getString("manufacturer");
                String settingsSourceURL = jsonObject.getString("settings_source_url");
                int dpi = jsonObject.getInt("dpi");
                int fire_button = jsonObject.getInt("fire_button");
                int review = jsonObject.getJSONObject("sensitivities").getInt("review");
                int collimator = jsonObject.getJSONObject("sensitivities").getInt("collimator");
                int x2_scope = jsonObject.getJSONObject("sensitivities").getInt("x2_scope");
                int x4_scope = jsonObject.getJSONObject("sensitivities").getInt("x4_scope");
                int sniper_scope = jsonObject.getJSONObject("sensitivities").getInt("sniper_scope");
                int free_review = jsonObject.getJSONObject("sensitivities").getInt("free_review");
                list.add(new SensitivityModel(deviceName, manufacturerName, settingsSourceURL, dpi, fire_button,
                        review, collimator, x2_scope, x4_scope, sniper_scope,free_review
                ));
            }
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new DevicesAdapter(context, fragment, list));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            new MaterialAlertDialogBuilder(context)
                    .setTitle("Error")
                    .setMessage(e.getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private String readJSONDataFromFile(Context context, String manufacturer) throws IOException {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        try {
            String jsonString = null;
            inputStream = context.getAssets().open("sensitivity_settings/" + manufacturer + ".json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((jsonString = bufferedReader.readLine()) != null) {
                builder.append(jsonString);
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }
}
