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
import com.jvmfrog.ffsettings.databinding.FragmentManufacturerBinding;
import com.jvmfrog.ffsettings.model.ManufacturersModel;
import com.jvmfrog.ffsettings.ui.dialog.ChangeUsernameDialog;
import com.jvmfrog.ffsettings.utils.CustomTabUtil;
import com.jvmfrog.ffsettings.utils.NavigationUtils;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

import java.util.Locale;

public class ManufacturerFragment extends Fragment {

    private FragmentManufacturerBinding binding;
    private FirestoreRecyclerAdapter<ManufacturersModel, ManufacturersHolder> adapter;

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

        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
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

        adapter = new FirestoreRecyclerAdapter<ManufacturersModel, ManufacturersHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ManufacturersHolder holder, int position, @NonNull ManufacturersModel model) {
                holder.manufacturerName.setText(model.getName());

                if (model.getShowInDashboard()) {
                    holder.manufacturerName.setText(model.getName());
                } else {
                    holder.itemView.setVisibility(View.GONE);
                }

                holder.itemView.setOnClickListener(v -> {
                    Bundle finalBundle = new Bundle();
                    finalBundle.putString("collection", model.getCollection().toLowerCase(Locale.ROOT));
                    NavigationUtils.navigateWithNavHost(
                            (FragmentActivity) v.getContext(),
                            R.id.nav_host_fragment,
                            R.id.action_manufacturerFragment_to_devicesFragment,
                            finalBundle);
                });
            }

            @NonNull
            @Override
            public ManufacturersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
                return new ManufacturersHolder(view);
            }
        };

        LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        binding.recview.setLayoutManager(layoutManager);
        binding.recview.setAdapter(adapter);

        binding.setUserNameBtn.setOnClickListener(view -> ChangeUsernameDialog.showDialog(getActivity()));
        binding.googleFormBtn.setOnClickListener(view -> new CustomTabUtil().OpenCustomTab(getActivity(), getString(R.string.google_form), R.color.md_theme_light_onSecondary));

        return binding.getRoot();
    }

    private class ManufacturersHolder extends RecyclerView.ViewHolder {
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