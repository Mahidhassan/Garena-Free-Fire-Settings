package com.jvmfrog.ffsettings.utils;

import android.content.Context;
import android.util.Log;
import android.util.MutableBoolean;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.jvmfrog.ffsettings.MyApplication;
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
    private static final List<ManufacturersModel> manufacturersList = new ArrayList<>();
    private static final MutableLiveData<Boolean> isRequestFinished = new MutableLiveData<>();

    public List<ManufacturersModel> getManufacturersList() {
        return manufacturersList;
    }

    public MutableLiveData<Boolean> isRequestFinished() {
        isRequestFinished.setValue(false);
        return isRequestFinished;
    }

    public void updateAdapterDate(Context context) {
        if (getManufacturersList().isEmpty()) {
            isRequestFinished.setValue(false);
            RequestQueue queue = Volley.newRequestQueue(context);
            String GITHUB_MANUFACTURERS_FILES_PATH = "https://raw.githubusercontent.com/IbremMiner837/Garena-Free-Fire-Settings/master/app/src/main/assets/sensitivity_settings/manufacturers.json";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, GITHUB_MANUFACTURERS_FILES_PATH, null, response -> {
                try {
                    JSONArray jsonArray = response.getJSONArray("manufacturers");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String model = jsonObject.getString("model");
                        Boolean showInProductionApp = jsonObject.getBoolean("showInProductionApp");
                        Boolean isAvailable = jsonObject.getBoolean("isAvailable");
                        getManufacturersList().add(new ManufacturersModel(name, model, showInProductionApp, isAvailable));
                    }
                } catch (JSONException e) {e.printStackTrace();}
            }, error -> Log.e("Volley", error.toString()));
            queue.addRequestEventListener((request, event) -> {
                if (event == RequestQueue.RequestEvent.REQUEST_FINISHED) {
                    isRequestFinished.setValue(true);
                }
            });
            queue.add(jsonObjectRequest);
        }
    }
}
