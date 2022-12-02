package com.jvmfrog.ffsettings.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.databinding.FragmentDevicesBinding;
import com.jvmfrog.ffsettings.model.ParamsModel;
import com.jvmfrog.ffsettings.utils.InterstitialAdHelper;
import com.jvmfrog.ffsettings.utils.NavigationUtils;

import java.util.Locale;

public class DevicesFragment extends Fragment {

    private FragmentDevicesBinding binding;
    private FirestoreRecyclerAdapter<ParamsModel, DeviceViewHolder> adapter;

    private static final String TAG = "Interstitial Ad";
    private InterstitialAd mInterstitialAd;

    public DevicesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDevicesBinding.inflate(inflater, container, false);

        InterstitialAdHelper interstitialAdHelper = new InterstitialAdHelper(requireActivity());
        interstitialAdHelper.loadInterstitialAd();

        Bundle finalBundle = new Bundle();
        finalBundle.putAll(getArguments());

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        rootRef.setFirestoreSettings(settings);

        Query query = rootRef.collection(finalBundle.getString("collection"))
                //.whereEqualTo("serie", "A")
                .orderBy("device_name", Query.Direction.DESCENDING);

        binding.shimmerLayout.startShimmer();
        binding.shimmerLayout.setVisibility(View.VISIBLE);
        binding.recview.setVisibility(View.GONE);
        query.get().addOnCompleteListener(task -> {
            if (task.isComplete() || task.isSuccessful()) {
                if (task.getResult().isEmpty()) {
                    binding.shimmerLayout.startShimmer();
                    binding.shimmerLayout.setVisibility(View.VISIBLE);
                    binding.recview.setVisibility(View.GONE);
                } else {
                    binding.shimmerLayout.stopShimmer();
                    binding.shimmerLayout.setVisibility(View.GONE);
                    binding.recview.setVisibility(View.VISIBLE);
                }
            }
        });

        FirestoreRecyclerOptions<ParamsModel> options =
                new FirestoreRecyclerOptions.Builder<ParamsModel>()
                        .setQuery(query, ParamsModel.class)
                        .build();

        adapter = new FirestoreRecyclerAdapter<ParamsModel, DeviceViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull DeviceViewHolder holder, int position, @NonNull ParamsModel model) {
                holder.device_name.setText(model.getDevice_name());

                holder.itemView.setOnClickListener(v -> {
                    interstitialAdHelper.showInterstitial();
                    Bundle finalBundle = new Bundle();
                    finalBundle.putFloat("review", model.getReview());
                    finalBundle.putFloat("collimator", model.getCollimator());
                    finalBundle.putFloat("x2_scope", model.getX2_scope());
                    finalBundle.putFloat("x4_scope", model.getX4_scope());
                    finalBundle.putFloat("sniper_scope", model.getSniper_scope());
                    finalBundle.putFloat("free_review", model.getFree_review());
                    finalBundle.putFloat("dpi", model.getDpi());
                    finalBundle.putFloat("fire_button", model.getFire_button());
                    finalBundle.putString("settings_source_url", model.getSettings_source_url());
                    NavigationUtils.navigateWithNavHost(
                            (FragmentActivity) v.getContext(),
                            R.id.nav_host_fragment,
                            R.id.action_devicesFragment_to_deviceSettingsFragment,
                            finalBundle);
                });
            }

            @NonNull
            @Override
            public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new DeviceViewHolder(view);
            }
        };

        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        binding.recview.setLayoutManager(layoutManager);
        binding.recview.setAdapter(adapter);

        return binding.getRoot();
    }

    private class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView device_name;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.categories);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }
}