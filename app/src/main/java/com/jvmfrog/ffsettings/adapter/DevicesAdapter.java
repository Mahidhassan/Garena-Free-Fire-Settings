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

import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.ObservableSnapshotArray;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jvmfrog.ffsettings.R;
import com.jvmfrog.ffsettings.model.ParamsModel;
import com.jvmfrog.ffsettings.ui.fragment.DevicesFragment;
import com.jvmfrog.ffsettings.utils.InterstitialAdHelper;
import com.jvmfrog.ffsettings.utils.NavigationUtils;

public class DevicesAdapter extends FirestoreRecyclerAdapter<ParamsModel, DevicesAdapter.DeviceViewHolder> {
    private Context context;

    public DevicesAdapter(@NonNull FirestoreRecyclerOptions<ParamsModel> options, Context context) {
        super(options);
        context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DevicesAdapter.DeviceViewHolder holder, int position, @NonNull ParamsModel model) {
        holder.device_name.setText(model.getDevice_name());

        holder.itemView.setOnClickListener(v -> {
            new InterstitialAdHelper(context).showInterstitial();
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
    public DevicesAdapter.DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new DeviceViewHolder(view);
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView device_name;
        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.categories);
        }
    }

    @NonNull
    @Override
    public ParamsModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void updateOptions(@NonNull FirestoreRecyclerOptions<ParamsModel> options) {
        super.updateOptions(options);
    }

    @Override
    public void onError(@NonNull FirebaseFirestoreException e) {
        super.onError(e);
    }

    @Override
    public void onChildChanged(@NonNull ChangeEventType type, @NonNull DocumentSnapshot snapshot, int newIndex, int oldIndex) {
        super.onChildChanged(type, snapshot, newIndex, oldIndex);
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
    }

    @Override
    public void startListening() {
        super.startListening();
    }

    @Override
    public void stopListening() {
        super.stopListening();
    }

    @NonNull
    @Override
    public ObservableSnapshotArray<ParamsModel> getSnapshots() {
        return super.getSnapshots();
    }
}
