package com.bcit.bb;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bcit.bb.R;

import java.io.Serializable;
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
        holder.equipment.setText(booking.getEquiment());
        holder.date.setText(booking.getDate());
        holder.timeslot.setText(booking.getTimeslot());


//        holder.articleItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
//                Toast.makeText(context, "You just clicked some news", Toast.LENGTH_SHORT).show();
//                intent.putExtra("news", booking);
//                view.getContext().startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView equipment;
        public TextView timeslot;
        public CardView articleItem;
        public TextView date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            equipment = itemView.findViewById(R.id.equipment11);
            timeslot = itemView.findViewById(R.id.timeslot11);
            date = itemView.findViewById(R.id.date11);
            articleItem = itemView.findViewById(R.id.previewSummary);

        }
    }

}
