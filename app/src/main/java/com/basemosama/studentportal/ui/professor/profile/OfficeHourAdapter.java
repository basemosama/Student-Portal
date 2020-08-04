package com.basemosama.studentportal.ui.professor.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.databinding.ItemOfficeHourBinding;
import com.basemosama.studentportal.model.professor.OfficeHour;

import java.util.List;

public class OfficeHourAdapter extends RecyclerView.Adapter<OfficeHourAdapter.OfficeHourViewHolder> {
    private Context context;
    private List<OfficeHour> officeHours;

    public OfficeHourAdapter(Context context, List<OfficeHour> officeHours) {
        this.context = context;
        this.officeHours = officeHours;
    }

    @NonNull
    @Override
    public OfficeHourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        ItemOfficeHourBinding officeHourBinding = ItemOfficeHourBinding.inflate(inflater, parent, false);
        return new OfficeHourViewHolder(officeHourBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OfficeHourViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return officeHours != null ? officeHours.size() : 0;
    }

    public void updateAdapter(List<OfficeHour> newOfficeHours) {
        officeHours.clear();
        officeHours.addAll(newOfficeHours);
        notifyDataSetChanged();
    }

    public class OfficeHourViewHolder extends RecyclerView.ViewHolder {
        private ItemOfficeHourBinding officeHourBinding;

        public OfficeHourViewHolder(@NonNull ItemOfficeHourBinding itemOfficeHourBinding) {
            super(itemOfficeHourBinding.getRoot());
            officeHourBinding = itemOfficeHourBinding;
        }

        private void bind(int position) {
            OfficeHour officeHour = officeHours.get(position);
            officeHourBinding.setOfficeHour(officeHour);
            if (position == getItemCount() - 1) {
                officeHourBinding.profileOfficeHourBorder.setVisibility(View.GONE);
            }
        }
    }

}
