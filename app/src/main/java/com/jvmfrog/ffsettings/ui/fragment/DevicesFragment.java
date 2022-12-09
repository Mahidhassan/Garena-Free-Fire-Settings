package com.jvmfrog.ffsettings.ui.fragment;

import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.adapter.DevicesAdapter;
import com.jvmfrog.ffsettings.databinding.FragmentDevicesBinding;
import com.jvmfrog.ffsettings.model.ParamsModel;
import com.jvmfrog.ffsettings.utils.InterstitialAdHelper;
import com.jvmfrog.ffsettings.utils.NavigationUtils;

public class DevicesFragment extends Fragment {

    private FragmentDevicesBinding binding;
    private DevicesAdapter adapter;

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

        adapter = new DevicesAdapter(options, getActivity());
        LinearLayoutManager layoutManager = new GridLayoutManager(requireActivity(), 1);
        binding.recview.setLayoutManager(layoutManager);
        binding.recview.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        binding.recview.getRecycledViewPool().clear();
        adapter.notifyDataSetChanged();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }
}