package com.bcit.bb;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
/*
Adapter class
 */
public class currentBookingAdapter extends RecyclerView.Adapter<currentBookingAdapter.ViewHolder> {
    private ArrayList<BookingTemplate> articleList;
    private Context context;

    public currentBookingAdapter(ArrayList<BookingTemplate> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public currentBookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_preview, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull currentBookingAdapter.ViewHolder holder, int position) {
        final BookingTemplate booking = articleList.get(position);
        holder.equipment.setText(booking.getEquipment());
        holder.date.setText(booking.getDate());
        holder.timeslot.setText(booking.getTimeslot());
         holder.id.setText(booking.getId());
        Log.d("DEBUG", "msg" + booking.getTimeslot());

    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView equipment;
        public TextView timeslot;
        public LinearLayout articleItem;
        public TextView date;
        public Button delete;
        public Button update;
        public TextView id;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            equipment = itemView.findViewById(R.id.equipment11);
            timeslot = itemView.findViewById(R.id.timeslot11);
            date = itemView.findViewById(R.id.date11);
            articleItem = itemView.findViewById(R.id.previewSummary);
            update = itemView.findViewById(R.id.editBooking1);
            delete = itemView.findViewById(R.id.deleteBooking1);
            id = itemView.findViewById(R.id.timeslotID);

        }
    }

}
