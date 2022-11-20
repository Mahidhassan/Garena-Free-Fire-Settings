package com.jvmfrog.ffsettings.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.model.ManufacturersModel;
import com.jvmfrog.ffsettings.model.ParamsModel;
import com.jvmfrog.ffsettings.ui.fragment.DevicesFragment;
import com.jvmfrog.ffsettings.utils.FragmentUtils;
import com.jvmfrog.ffsettings.utils.NavigationUtils;
import com.jvmfrog.ffsettings.utils.SharedPreferencesUtils;

import java.util.ArrayList;
import java.util.Locale;

public class ManufacturersAdapter extends FirestoreRecyclerAdapter<ManufacturersModel, ManufacturersAdapter.holder> {

    private Context context;

    public ManufacturersAdapter(@NonNull FirestoreRecyclerOptions<ManufacturersModel> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ManufacturersAdapter.holder holder, int position, @NonNull ManufacturersModel model) {

        if (model.getShowInDashboard()) {
            holder.manufacturer_name.setText(model.getName());
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
    public ManufacturersAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ManufacturersAdapter.holder(view);
    }

    static class holder extends RecyclerView.ViewHolder {
        TextView manufacturer_name;
        public holder(@NonNull View itemView) {
            super(itemView);
            manufacturer_name = itemView.findViewById(R.id.categories);
        }
    }
}
