package com.jvmfrog.ffsettings.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.model.ManufacturersModel;
import com.jvmfrog.ffsettings.utils.NavigationUtils;

import java.util.List;
import java.util.Locale;

public class ManufacturerAdapter extends RecyclerView.Adapter<ManufacturerAdapter.ManufacturersHolder> {

    private final List<ManufacturersModel> models;

    public ManufacturerAdapter(List<ManufacturersModel> models) {
        this.models = models;
    }

    @Override
    public void onBindViewHolder(@NonNull ManufacturersHolder holder, int position) {
        if (models.get(position).getShowInProductionApp()) {
            holder.manufacturerName.setText(models.get(position).getName());
        } else {
            holder.itemView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            Bundle finalBundle = new Bundle();
            finalBundle.putString("model", models.get(position).getModel().toLowerCase(Locale.ROOT));
            NavigationUtils.navigateWithNavHost(
                    (FragmentActivity) v.getContext(),
                    R.id.nav_host_fragment,
                    R.id.action_manufacturerFragment_to_devicesFragment,
                    finalBundle);
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    @NonNull
    @Override
    public ManufacturersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ManufacturersHolder(view);
    }

    public static class ManufacturersHolder extends RecyclerView.ViewHolder {
        TextView manufacturerName;
        public ManufacturersHolder(@NonNull View itemView) {
            super(itemView);
            manufacturerName = itemView.findViewById(R.id.categories);
        }
    }
}
