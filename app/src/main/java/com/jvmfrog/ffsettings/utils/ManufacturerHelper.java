package com.jvmfrog.ffsettings.utils;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.jvmfrog.ffsettings.adapter.ManufacturerAdapter;
import com.jvmfrog.ffsettings.model.ManufacturersModel;

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

public class ManufacturerHelper {
    public static String GITHUB_MANUFACTURERS_FILES_PATH = "https://raw.githubusercontent.com/IbremMiner837/Garena-Free-Fire-Settings/master/app/src/main/assets/sensitivity_settings/manufacturers.json";

    public void getManufacturersFromURL(Context context, Fragment fragment, RecyclerView recyclerView, ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.startShimmer();
        shimmerFrameLayout.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        RequestQueue queue = Volley.newRequestQueue(context);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GITHUB_MANUFACTURERS_FILES_PATH, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("manufacturers");
                List<ManufacturersModel> list = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String model = jsonObject.getString("model");
                    Boolean showInProductionApp = jsonObject.getBoolean("showInProductionApp");
                    Boolean isAvailable = jsonObject.getBoolean("isAvailable");
                    list.add(new ManufacturersModel(name, model, showInProductionApp, isAvailable));
                }
                recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
                recyclerView.setAdapter(new ManufacturerAdapter(fragment, list));
            } catch (JSONException e) {e.printStackTrace();}
        }, error -> Log.e("Volley", error.toString()));
        queue.addRequestEventListener((request, event) -> {
            if (event == RequestQueue.RequestEvent.REQUEST_FINISHED) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void getManufacturersFromAssets(Context context, Fragment fragment, RecyclerView recyclerView, ShimmerFrameLayout shimmerFrameLayout) {
        shimmerFrameLayout.stopShimmer();
        shimmerFrameLayout.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        try {
            String json = readJSONDataFromFile(context);
            JSONObject object = new JSONObject(json);
            JSONArray jsonArray = object.getJSONArray("manufacturers");
            List<ManufacturersModel> list = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String name = jsonObject.getString("name");
                String model = jsonObject.getString("model");
                Boolean showInProductionApp = jsonObject.getBoolean("showInProductionApp");
                Boolean isAvailable = jsonObject.getBoolean("isAvailable");
                list.add(new ManufacturersModel(name, model, showInProductionApp, isAvailable));
            }
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
            recyclerView.setAdapter(new ManufacturerAdapter(fragment, list));
        } catch (JSONException | IOException e) {e.printStackTrace();}
    }

    private String readJSONDataFromFile(Context context) throws IOException {
        InputStream inputStream = null;
        StringBuilder builder = new StringBuilder();
        try {
            String jsonString = null;
            inputStream = context.getAssets().open("sensitivity_settings/manufacturers.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((jsonString = bufferedReader.readLine()) != null) {builder.append(jsonString);}
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new String(builder);
    }
}
