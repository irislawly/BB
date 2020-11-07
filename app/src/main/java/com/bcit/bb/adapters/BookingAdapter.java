package com.bcit.bb.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bcit.bb.R;

import java.util.ArrayList;
/*
Adapter class
 */
public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
    private ArrayList<BookingTemplate> articleList;
    private Context context;

    public BookingAdapter(ArrayList<BookingTemplate> articleList, Context context) {
        this.articleList = articleList;
        this.context = context;
    }

    @NonNull
    @Override
    public BookingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_preview, parent, false);
        return new ViewHolder(v);
    }


    @Override
    public void onBindViewHolder(@NonNull BookingAdapter.ViewHolder holder, int position) {
        final BookingTemplate booking = articleList.get(position);
        holder.equipment.setText(booking.getEquipment());
        holder.date.setText(booking.getDate());
        holder.timeslot.setText(booking.getTimeslot());
         holder.id.setText(booking.getId());
         holder.gymname.setText(booking.getGymname());
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
        public TextView gymname;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            equipment = itemView.findViewById(R.id.equipment11);
            timeslot = itemView.findViewById(R.id.timeslot11);
            date = itemView.findViewById(R.id.book_date);
            articleItem = itemView.findViewById(R.id.previewSummary);
            update = itemView.findViewById(R.id.editBooking1);
            delete = itemView.findViewById(R.id.deleteBooking1);
            id = itemView.findViewById(R.id.timeslotID);
            gymname = itemView.findViewById(R.id.book_gymname);

        }
    }

}
