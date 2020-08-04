package com.basemosama.studentportal.ui.student.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.databinding.ItemEventBinding;
import com.basemosama.studentportal.model.student.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private Context context;
    private List<Event> events;
    private EventClickListener eventClickListener;

    public EventAdapter(Context context, List<Event> events, EventClickListener eventClickListener) {
        this.context = context;
        this.events = events;
        this.eventClickListener = eventClickListener;
    }

    public void updateAdapter(List<Event> eventList) {
        events.clear();
        events.addAll(eventList);
        /* This for showing only upcoming events only
        for (Event event :eventList) {
            if (event.getDate().after(new Date())){
                events.add(event);
            }
        }
         */
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemEventBinding itemEventBinding = ItemEventBinding.inflate(inflater, parent, false);
        return new EventViewHolder(itemEventBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public interface EventClickListener {
        void onEventClickListener(Event event, View view);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ItemEventBinding itemEventBinding;

        public EventViewHolder(@NonNull ItemEventBinding itemEventBinding) {
            super(itemEventBinding.getRoot());
            this.itemEventBinding = itemEventBinding;
            itemEventBinding.getRoot().setOnClickListener(this);
        }

        public void bind(int position) {
            Event event = events.get(position);
            itemEventBinding.setEvent(event);
        }

        @Override
        public void onClick(View view) {
            Event event = events.get(getAdapterPosition());
            eventClickListener.onEventClickListener(event, view);
        }
    }
}
