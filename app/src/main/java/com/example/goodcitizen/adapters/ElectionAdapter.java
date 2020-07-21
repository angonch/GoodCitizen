package com.example.goodcitizen.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.goodcitizen.R;
import com.example.goodcitizen.activities.ElectionDetailsActivity;
import com.example.goodcitizen.models.ElectionModel;

import org.parceler.Parcels;

import java.util.List;

public class ElectionAdapter extends RecyclerView.Adapter<ElectionAdapter.ViewHolder> {

    public static final String TAG = "ElectionAdapter";
    Context context;
    List<ElectionModel> elections;

    public ElectionAdapter(Context context, List<ElectionModel> elections) {
        this.context = context;
        this.elections = elections;
    }

    //  inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        // Inflate item_election XML to get View
        View electionView = LayoutInflater.from(context).inflate(R.layout.item_election, parent, false);
        // Wrap view inside a holder
        return new ViewHolder(electionView);
    }

    // Populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        // Get the election at the position
        ElectionModel election = elections.get(position);
        // Bind election data to holder
        holder.bind(election);
    }

    // Returns total count of items in the list
    @Override
    public int getItemCount() {
        return elections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvElectionName;
        TextView tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvElectionName = itemView.findViewById(R.id.tvElectionName);
            tvDate = itemView.findViewById(R.id.tvDate);
            itemView.setOnClickListener(this);
        }

        public void bind(ElectionModel election) {
            tvElectionName.setText(election.getElectionName());
            tvDate.setText(election.getDate());
        }

        // TODO: REMOVE - no detail view, swipe to set notification instead
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                ElectionModel election = elections.get(position);
                Intent i = new Intent(context, ElectionDetailsActivity.class);
                i.putExtra(ElectionModel.class.getSimpleName(), Parcels.wrap(election));
                context.startActivity(i);
            }
        }
    }
}
