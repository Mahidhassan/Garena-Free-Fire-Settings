package com.jvmfrog.ffsettings.adapter;

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
import com.jvmfrog.ffsettings.model.ManufacturersModel;
import com.jvmfrog.ffsettings.ui.fragment.ManufacturerFragment;
import com.jvmfrog.ffsettings.utils.NavigationUtils;

import java.util.Locale;

public class ManufacturerAdapter extends FirestoreRecyclerAdapter<ManufacturersModel, ManufacturerAdapter.ManufacturersHolder> {

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ManufacturerAdapter(@NonNull FirestoreRecyclerOptions<ManufacturersModel> options) {
        super(options);
    }

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

    public static class ManufacturersHolder extends RecyclerView.ViewHolder {
        TextView manufacturerName;
        public ManufacturersHolder(@NonNull View itemView) {
            super(itemView);
            manufacturerName = itemView.findViewById(R.id.categories);
        }
    }

    @NonNull
    @Override
    public ManufacturersModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void updateOptions(@NonNull FirestoreRecyclerOptions<ManufacturersModel> options) {
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
    public ObservableSnapshotArray<ManufacturersModel> getSnapshots() {
        return super.getSnapshots();
    }
}
