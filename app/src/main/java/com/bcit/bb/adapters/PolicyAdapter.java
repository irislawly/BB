package com.bcit.bb.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcit.bb.R;

import java.util.ArrayList;

/**
 * Policy adapter for holding regulations.
 */
public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.ViewHolder> {
    private static final String TAG = "PolicyAdapter";
    private ArrayList<String> policyList;
    private Context context;

    /**
     * Policity adapter constructor
     * @param policies policies
     * @param context context
     */
    public PolicyAdapter(ArrayList<String> policies, Context context) {
        policyList = policies;
        this.context = context;
    }

    /**
     * Holds view.
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_policy_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    /**
     * Binds to view holder.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.policy.setText(policyList.get(position));
    }

    /**
     * Get item count.
     * @return
     */
    @Override
    public int getItemCount() {
        return policyList.size();
    }

    /**
     * Holds view.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView policy;
        LinearLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            policy = itemView.findViewById(R.id.policy_item);
            parentLayout = itemView.findViewById(R.id.parent_layout_policy);
        }
    }
}
