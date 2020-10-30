package com.bcit.bb;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PolicyAdapter extends RecyclerView.Adapter<PolicyAdapter.ViewHolder> {
    private static final String TAG = "PolicyAdapter";
    //private ArrayList<String> policyListHeaders = new ArrayList<>();
    private ArrayList<String> policyList;
    private Context context;

    public PolicyAdapter(ArrayList<String> policies, Context context) {
        //policyListHeaders = policyHeaders;
        policyList = policies;
        this.context = context;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_policy_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        //holder.policy.setText(policyListHeaders.get(position));
        holder.policy.setText(policyList.get(position));
    }

    @Override
    public int getItemCount() {
        return policyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView policy;
        //TextView policyHeader;
        LinearLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //policyHeader = itemView.findViewById(R.id.policy_header2);
            policy = itemView.findViewById(R.id.policy_item);
            parentLayout = itemView.findViewById(R.id.parent_layout_policy);
        }
    }
}
