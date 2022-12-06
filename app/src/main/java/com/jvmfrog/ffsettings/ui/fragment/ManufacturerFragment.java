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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.Query;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.adapter.ManufacturerAdapter;
import com.jvmfrog.ffsettings.databinding.FragmentManufacturerBinding;
import com.jvmfrog.ffsettings.model.ManufacturersModel;
import com.jvmfrog.ffsettings.ui.dialog.ChangeUsernameDialog;
import com.jvmfrog.ffsettings.utils.CustomTabUtil;
import com.jvmfrog.ffsettings.utils.NavigationUtils;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

import java.util.Locale;

public class ManufacturerFragment extends Fragment {

    private FragmentManufacturerBinding binding;
    private ManufacturerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentManufacturerBinding.inflate(inflater, container, false);

        if (SharedPreferencesUtils.getString(getActivity(), "user_name") == null || SharedPreferencesUtils.getString(getActivity(), "user_name").equals("")) {
            binding.welcomeAndUserName.setText(getString(R.string.welcome) + "," + "\n" + getString(R.string.user_name) + "!");
        } else {
            binding.welcomeAndUserName.setText(getString(R.string.welcome) + "," + "\n" + SharedPreferencesUtils.getString(getActivity(), "user_name") + "!");
        }

        try {
            FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
            FirebaseFirestore.setLoggingEnabled(true);
            FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(true)
                    .build();
            rootRef.setFirestoreSettings(settings);

            Query query = rootRef.collection("manufacturers")
                    .orderBy("name", Query.Direction.ASCENDING);

            FirestoreRecyclerOptions<ManufacturersModel> options =
                    new FirestoreRecyclerOptions.Builder<ManufacturersModel>()
                            .setQuery(query, ManufacturersModel.class)
                            .build();

            adapter = new ManufacturerAdapter(options);


            GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
            binding.recview.setLayoutManager(layoutManager);
            binding.recview.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Остановиста БеБи")
                    .setMessage("На, смотри что ты наделал балбес" + "\n" + e.getMessage())
                    .setPositiveButton("Посмотрел", null)
                    .show();
        }

        binding.setUserNameBtn.setOnClickListener(view -> ChangeUsernameDialog.showDialog(getActivity()));
        binding.googleFormBtn.setOnClickListener(view -> new CustomTabUtil().OpenCustomTab(getActivity(), getString(R.string.google_form), R.color.md_theme_light_onSecondary));

        return binding.getRoot();
    }

    private static class ManufacturersHolder extends RecyclerView.ViewHolder {
        TextView manufacturerName;
        public ManufacturersHolder(@NonNull View itemView) {
            super(itemView);
            manufacturerName = itemView.findViewById(R.id.categories);
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