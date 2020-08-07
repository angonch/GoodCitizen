package com.example.goodcitizen.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.goodcitizen.GoogleClient;
import com.example.goodcitizen.R;
import com.example.goodcitizen.models.ElectionModel;

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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvElectionName;
        TextView tvDate;
        ImageView ivElectionIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvElectionName = itemView.findViewById(R.id.tvElectionName);
            tvDate = itemView.findViewById(R.id.tvDate);
            ivElectionIcon = itemView.findViewById(R.id.ivElectionIcon);
        }

        public void bind(ElectionModel election) {
            tvElectionName.setText(election.getElectionName());
            tvDate.setText(election.getDate());
            if(election.getState() != null) {
                Glide.with(context).load(GoogleClient.getStateFlagURL(election.getState())).into(ivElectionIcon);
            } else {
                Glide.with(context).load(R.drawable.usflag).into(ivElectionIcon);
            }
        }
    }
}
