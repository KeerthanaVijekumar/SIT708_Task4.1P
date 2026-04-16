package com.keerthana.eventplannerapp.ui;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.keerthana.eventplannerapp.R;
import com.keerthana.eventplannerapp.db.Event;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_EVENT = 1;

    // List items can be either a String (header) or Event
    private List<Object> items = new ArrayList<>();

    public interface OnEventClickListener {
        void onEventClick(Event event);
    }

    public interface OnEventLongClickListener {
        void onEventLongClick(Event event);
    }

    private OnEventClickListener listener;
    private OnEventLongClickListener longClickListener;

    public EventAdapter(OnEventClickListener listener, OnEventLongClickListener longClickListener) {
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    public void setEvents(List<Event> events) {
        items.clear();
        long now = System.currentTimeMillis();

        List<Event> upcoming = new ArrayList<>();
        List<Event> past = new ArrayList<>();

        for (Event e : events) {
            if (e.getDateTime() >= now) {
                upcoming.add(e);
            } else {
                past.add(e);
            }
        }

        if (!upcoming.isEmpty()) {
            items.add("📅 Upcoming Events");
            items.addAll(upcoming);
        }

        if (!past.isEmpty()) {
            items.add("🕘 Past Events");
            items.addAll(past);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position) instanceof String ? TYPE_HEADER : TYPE_EVENT;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_event, parent, false);
            return new EventViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).tvHeader.setText((String) items.get(position));
        } else {
            Event event = (Event) items.get(position);
            EventViewHolder evHolder = (EventViewHolder) holder;

            evHolder.tvTitle.setText(event.getTitle());
            evHolder.tvCategory.setText(event.getCategory());
            evHolder.tvLocation.setText(event.getLocation());

            SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy  hh:mm a", Locale.getDefault());
            evHolder.tvDateTime.setText(sdf.format(new Date(event.getDateTime())));

            // Grey out past events slightly
            long now = System.currentTimeMillis();
            if (event.getDateTime() < now) {
                evHolder.tvTitle.setTextColor(Color.GRAY);
                evHolder.tvDateTime.setTextColor(Color.GRAY);
            } else {
                evHolder.tvTitle.setTextColor(Color.BLACK);
                evHolder.tvDateTime.setTextColor(Color.parseColor("#1976D2"));
            }

            evHolder.itemView.setOnClickListener(v -> listener.onEventClick(event));
            evHolder.itemView.setOnLongClickListener(v -> {
                longClickListener.onEventLongClick(event);
                return true;
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // Header ViewHolder
    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;
        HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(android.R.id.text1);
            tvHeader.setTextSize(16f);
            tvHeader.setTextColor(Color.parseColor("#1976D2"));
            tvHeader.setTypeface(null, android.graphics.Typeface.BOLD);
            tvHeader.setPadding(24, 24, 24, 8);
            tvHeader.setBackgroundColor(Color.parseColor("#F5F5F5"));
        }
    }

    // Event ViewHolder
    static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvLocation, tvDateTime;
        EventViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
        }
    }
}