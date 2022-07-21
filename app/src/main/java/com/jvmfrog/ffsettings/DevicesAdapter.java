package com.jvmfrog.ffsettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class DevicesAdapter extends FirestoreRecyclerAdapter<ParamsModel, DevicesAdapter.holder> {

    private Context context;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public DevicesAdapter(@NonNull FirestoreRecyclerOptions<ParamsModel> options, Context context) {
        super(options);
        context = this.context;
    }

    @Override
    protected void onBindViewHolder(@NonNull DevicesAdapter.holder holder, int position, @NonNull ParamsModel model) {
        holder.device_name.setText(model.getManufacturer());

        holder.itemView.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), model.getManufacturer(), Toast.LENGTH_SHORT).show();
        });
    }

    @NonNull
    @Override
    public DevicesAdapter.holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new holder(view);
    }

    class holder extends RecyclerView.ViewHolder {

        TextView device_name;

        public holder(@NonNull View itemView) {
            super(itemView);
            device_name = itemView.findViewById(R.id.categories);
        }
    }
}
