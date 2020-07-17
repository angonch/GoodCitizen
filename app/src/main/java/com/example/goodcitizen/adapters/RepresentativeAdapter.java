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
import com.example.goodcitizen.activities.RepresentativeDetailsActivity;
import com.example.goodcitizen.models.RepresentativeModel;

import org.parceler.Parcels;

import java.util.List;

public class RepresentativeAdapter extends RecyclerView.Adapter<RepresentativeAdapter.ViewHolder> {
    
    public static final String TAG = "RepresentativeAdapter";
    Context context;
    List<RepresentativeModel> representatives;

    public RepresentativeAdapter(Context context, List<RepresentativeModel> representatives) {
        this.context = context;
        this.representatives = representatives;
    }

    //  inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public RepresentativeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder");
        // Inflate item_election XML to get View
        View representativeView = LayoutInflater.from(context).inflate(R.layout.item_representative, parent, false);
        // Wrap view inside a holder
        return new RepresentativeAdapter.ViewHolder(representativeView);
    }

    // Populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull RepresentativeAdapter.ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + position);
        // Get representative at the position
        RepresentativeModel rep = representatives.get(position);
        // Bind representative data to holder
        holder.bind(rep);
    }

    // Returns total count of items in the list
    @Override
    public int getItemCount() { return representatives.size(); }
    
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvPosition;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPosition = itemView.findViewById(R.id.tvOfficialName);
            tvName = itemView.findViewById(R.id.tvName);
            itemView.setOnClickListener(this);
        }

        public void bind(RepresentativeModel rep) {
            tvPosition.setText(rep.getPosition());
            tvName.setText(rep.getOfficialName());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(position != RecyclerView.NO_POSITION) {
                RepresentativeModel rep = representatives.get(position);
                Intent i = new Intent(context, RepresentativeDetailsActivity.class);
                i.putExtra(RepresentativeModel.class.getSimpleName(), Parcels.wrap(rep));
                context.startActivity(i);
            }
        }
    }
}
